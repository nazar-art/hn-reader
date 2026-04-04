package letscode.hnreader.rest;

import letscode.hnreader.api.PostService;
import letscode.hnreader.api.NewsService;
import letscode.hnreader.domain.RenderingMode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {
    private static final int PAGE_SIZE = 10;

    @Value("${view.rendering.mode}")
    private String renderingMode;

    private final PostService postService;
    private final NewsService newsService;

    @GetMapping("/")
    public String index(@RequestParam(defaultValue = "1") int page,
                        @RequestParam(defaultValue = "false") boolean refresh,
                        Model model) {

        if (refresh || postService.count() == 0) {
            newsService.syncFromApi();
        }

        int totalPages = newsService.getTotalPages(PAGE_SIZE);
        int safePage = Math.max(1, Math.min(page, Math.max(1, totalPages)));

        model.addAttribute("stories", newsService.getPage(safePage, PAGE_SIZE));
        model.addAttribute("currentPage", safePage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageSize", PAGE_SIZE);

        return resolveRenderingMode(renderingMode);
    }

    private String resolveRenderingMode(String renderingMode) {
        var mode = RenderingMode.fromString(renderingMode.trim());
        log.debug("Rendering mode: {}", mode);
        return switch (mode) {
            case THYMELEAF -> "index";
            case JTE -> "next/index";
        };
    }
}
