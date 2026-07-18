package ezria.lifetrackr.module.metadata.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.ProxyProvider;

@Configuration
public class TmdbConfig {
    @Bean
    public WebClient tmdbWebClient(
            @Value("${tmdb.base-url}") String baseUrl,
            @Value("${app.proxy.enabled:false}") boolean proxyEnabled,
            @Value("${app.proxy.host:127.0.0.1}") String proxyHost,
            @Value("${app.proxy.port:7897}") int proxyPort
    ) {
        HttpClient httpClient = HttpClient.create();
        if (proxyEnabled) {
            httpClient = httpClient.proxy(proxy -> proxy.type(ProxyProvider.Proxy.HTTP)
                    .host(proxyHost)
                    .port(proxyPort));
        }

        return WebClient.builder()
                .baseUrl(baseUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}
