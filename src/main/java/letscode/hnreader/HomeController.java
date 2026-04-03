package letscode.hnreader;

import letscode.hnreader.api.HnClient;
import letscode.hnreader.api.PostService;
import letscode.hnreader.api.dto.HnItem;
import letscode.hnreader.domain.Post;
import letscode.hnreader.domain.RoutingMode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Flux;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {

    private static final int PAGE_SIZE = 10;
    @Value("${view.rendering.mode}")
    public String renderingMode;

    private final HnClient hnClient;
    private final PostService postService;

    @GetMapping("/")
    public String index(
        @RequestParam(required = false, defaultValue = "1") int page,
        Model model) {

        // if first page or param is missed -> call to API
        if (page == 1) {
            return handleApiRequest(model, page);
        }

        // for the rest -> read from DB
        return handleDbRequest(model, page);
    }

    private String handleApiRequest(Model model, int page) {
        // Get IDs from API
        List<Integer> storyIds = hnClient.getTopStories()
            .take(100)
            .collectList()
            .block();

        storyIds = storyIds == null ? Collections.emptyList() : storyIds;

        // get saved posts from DB
        List<Post> cachedPosts = postService.findByIds(storyIds);
        Set<Integer> cachedIds = cachedPosts.stream()
            .map(Post::getId)
            .collect(Collectors.toSet());

        // find which IDs isn't at DB
        Set<Integer> missingIds = storyIds.stream()
                .filter(id -> !cachedIds.contains(id))
                .collect(Collectors.toSet());

        if (missingIds.isEmpty()) {
            // all posts at DB -> use them
            log.info("All {} posts found in cache", cachedPosts.size());
            return paginateAndRender(cachedPosts, storyIds, page, model);
        }

        // if missed -> get from API
        log.info("Loading {} missing posts from API", missingIds.size());

        List<HnItem> loadedItems = Flux.fromIterable(missingIds)
            .flatMapSequential(hnClient::getItem)
            .collectList()
            .block();

        // Async save posts to DB
        List<Post> postsToSave = postService.toEntityList(loadedItems);
        postService.saveAsync(postsToSave);

        List<Post> allPosts = Stream.concat(cachedPosts.stream(), postsToSave.stream())
            .sorted(Comparator.comparing(Post::getId).reversed())
            .collect(Collectors.toList());

        return paginateAndRender(allPosts, storyIds, page, model);
    }

    private String handleDbRequest(Model model, int page) {
        // Get all posts from DB
        List<Post> allPosts = postService.findAll();
        List<Integer> allIds = allPosts.stream()
            .map(Post::getId)
            .toList();

        log.info("Loading page {} from database, total posts: {}", page, allIds.size());

        return paginateAndRender(allPosts, allIds, page, model);
    }

    private String paginateAndRender(List<Post> posts, List<Integer> allIds, int page, Model model) {
        int totalPosts = allIds.size();
        int totalPages = (int) Math.ceil((double) totalPosts / PAGE_SIZE);

        // Limit page
        if (page < 1) page = 1;
        if (page > totalPages && totalPages > 0) page = totalPages;

        int fromIndex = (page - 1) * PAGE_SIZE;

        List<Post> pagePosts = posts.stream()
            .sorted(Comparator.comparing(Post::getId).reversed())
            .skip(fromIndex)
            .limit(PAGE_SIZE)
            .toList();

        var items = pagePosts.stream()
            .map(postService::toDto)
            .collect(Collectors.toList());

        model.addAttribute("stories", items);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageSize", PAGE_SIZE);

        return getRenderingMode(renderingMode);
    }

    private String getRenderingMode(String renderingMode) {
        var mode = RoutingMode.fromString(renderingMode.trim());
        return switch (mode) {
            case THYMELEAF -> "index";
            case JTE -> "next/index";
        };
    }
}
