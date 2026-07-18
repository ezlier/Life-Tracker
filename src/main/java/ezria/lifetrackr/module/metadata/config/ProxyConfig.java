package ezria.lifetrackr.module.metadata.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProxyConfig {
    @Value("${app.proxy.enabled:false}")
    private boolean proxyEnabled;

    @Value("${app.proxy.host:127.0.0.1}")
    private String proxyHost;

    @Value("${app.proxy.port:7897}")
    private String proxyPort;

    @PostConstruct
    public void setProxy() {
        if (proxyEnabled) {
            System.setProperty("http.proxyHost", proxyHost);
            System.setProperty("http.proxyPort", proxyPort);
            System.setProperty("https.proxyHost", proxyHost);
            System.setProperty("https.proxyPort", proxyPort);
        }
    }
}
