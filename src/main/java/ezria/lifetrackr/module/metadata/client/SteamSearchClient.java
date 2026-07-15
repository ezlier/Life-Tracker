package ezria.lifetrackr.module.metadata.client;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class SteamSearchClient {
    public Document search(String keyword) throws IOException {
        String url = "https://store.steampowered.com/search/?term="
                        + URLEncoder.encode(keyword, StandardCharsets.UTF_8);
        return Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(10000).get();
    }
}
