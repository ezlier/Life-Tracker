package ezria.lifetrackr.module.metadata.Parser;

import ezria.lifetrackr.module.metadata.dto.Steam.SteamSearchItem;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class SteamHtmlParser {
    public List<SteamSearchItem> parse(Document document) {
        List<SteamSearchItem> result = new ArrayList<>();
        Elements games = document.select("a.search_result_row");
        for (Element game : games) {
            SteamSearchItem item = new SteamSearchItem();
            //名称
            item.setName(game.select(".title").text());
            //图片
            item.setCover(game.select("img").attr("src"));
            //详情URL
            String url = game.attr("href");
            item.setUrl(url);
            //解析appid
            String appId = extractAppId(url);
            item.setAppId(appId);
            result.add(item);
        }
        return result;
    }

    private String extractAppId(String url){
        Pattern pattern = Pattern.compile("/app/(\\d+)");
        Matcher matcher = pattern.matcher(url);
        if(matcher.find()){return matcher.group(1);}
        return null;
    }
}