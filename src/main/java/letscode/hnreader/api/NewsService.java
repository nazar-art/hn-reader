package letscode.hnreader.api;

import letscode.hnreader.api.dto.HnItem;
import letscode.hnreader.api.mapper.PostMapper;
import letscode.hnreader.domain.Post;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NewsService {
    private static final int TOP_STORY_LIMIT = 100;

    private final HnClient hnClient;
    private final PostService postService;
    private final PostMapper postMapper;

    public List<HnItem> getPage(int page, int pageSize) {
        List<Post> all = postService.findAll();
        int from = (page - 1) * pageSize;
        List<Post> posts = all.stream()
            .sorted(Comparator.comparing(Post::getId).reversed())
            .skip(from)
            .limit(pageSize)
            .toList();
        return postMapper.toDtoList(posts);
    }

    public int getTotalPages(int pageSize) {
        return (int) Math.ceil((double) postService.count() / pageSize);
    }

    public void syncFromApi() {
        List<Integer> ids = hnClient.getTopStories()
            .take(TOP_STORY_LIMIT)
            .collectList()
            .block();

        if (ids == null || ids.isEmpty()) return;

        Set<Integer> cachedIds = postService.findByIds(ids)
            .stream().map(Post::getId).collect(Collectors.toSet());

        List<Integer> missingIds = ids.stream()
            .filter(id -> !cachedIds.contains(id))
            .toList();

        if (missingIds.isEmpty()) {
            log.info("All {} stories already cached", ids.size());
            return;
        }

        log.info("Fetching {} missing stories from API", missingIds.size());
        List<HnItem> items = Flux.fromIterable(missingIds)
            .flatMapSequential(hnClient::getItem)
            .collectList()
            .block();
        List<Post> posts = postMapper.toEntityList(items);
        postService.saveAsync(posts);
    }
}
