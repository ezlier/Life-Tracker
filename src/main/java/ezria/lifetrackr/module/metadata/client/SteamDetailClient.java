package ezria.lifetrackr.module.metadata.client;

import ezria.lifetrackr.module.metadata.dto.Steam.SteamDetailResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class SteamDetailClient {
    private final WebClient steamWebClient;

    public SteamDetailClient(@Qualifier("steamWebClient") WebClient steamWebClient) {
        this.steamWebClient = steamWebClient;
    }

    public SteamDetailResponse getDetail(String appId){
        return steamWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/appdetails")
                        .queryParam("appids", appId)
                        .queryParam("l", "schinese")
                        .build())
                .retrieve()
                .bodyToMono(SteamDetailResponse.class)
                .block();
    }
}