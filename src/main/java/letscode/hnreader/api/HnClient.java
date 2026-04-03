package letscode.hnreader.api;

import letscode.hnreader.api.dto.HnItem;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
public class HnClient {
    private static final String BASE_URL = "https://hacker-news.firebaseio.com/v0";

    private final WebClient webClient;

    public HnClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl(BASE_URL)
                .build();
    }

    public Flux<Integer> getTopStories() {
        return webClient.get()
                .uri("/topstories.json")
                .retrieve()
                .bodyToMono(Integer[].class)
                .flatMapMany(Flux::fromArray);
    }

    public Flux<Integer> getNewStories() {
        return webClient.get()
                .uri("/newstories.json")
                .retrieve()
                .bodyToMono(Integer[].class)
                .flatMapMany(Flux::fromArray);
    }

    public Flux<Integer> getBestStories() {
        return webClient.get()
                .uri("/beststories.json")
                .retrieve()
                .bodyToMono(Integer[].class)
                .flatMapMany(Flux::fromArray);
    }

    public Mono<HnItem> getItem(Integer id) {
        return webClient.get()
                .uri("/item/{id}.json", id)
                .retrieve()
                .bodyToMono(HnItem.class);
    }
}
