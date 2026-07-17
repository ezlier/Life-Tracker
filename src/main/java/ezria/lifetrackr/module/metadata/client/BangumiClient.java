package ezria.lifetrackr.module.metadata.client;

import ezria.lifetrackr.module.metadata.dto.Bangum.BangumSearchResponse;
import ezria.lifetrackr.module.metadata.dto.Bangum.BangumiSearchRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class BangumiClient {
    private final WebClient webClient;

    public BangumiClient(@Qualifier("bangumiWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public BangumSearchResponse searchAnime(String keyword){
        BangumiSearchRequest request = new BangumiSearchRequest();
        request.setKeyword(keyword);
        return webClient.post()
                .uri("/v0/search/subjects")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(BangumSearchResponse.class)
                .block();
    }
}
