package ezria.lifetrackr.module.metadata.client;

import ezria.lifetrackr.module.metadata.dto.tmdb.TmdbSearchResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class TmdbClient {
    private final WebClient webClient;

    public TmdbClient(@Qualifier("tmdbWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    @Value("${tmdb.api-key}")
    private String apiKey;

    public TmdbSearchResponse searchMovie(String keyword){
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search/movie")
                        .queryParam("query",keyword)
                        .queryParam("language","zh-CN")
                        .queryParam("api_key",apiKey)
                        .build())
                .retrieve()
                .bodyToMono(TmdbSearchResponse.class)
                .block();

    }
}
