package ezria.lifetrackr.module.metadata.provider;

import ezria.lifetrackr.module.metadata.Parser.SteamHtmlParser;
import ezria.lifetrackr.module.metadata.client.SteamDetailClient;
import ezria.lifetrackr.module.metadata.client.SteamSearchClient;
import ezria.lifetrackr.module.metadata.dto.MetadataDTO;
import ezria.lifetrackr.module.metadata.dto.Steam.SteamGameData;
import ezria.lifetrackr.module.metadata.dto.Steam.SteamDetailResponse;
import ezria.lifetrackr.module.metadata.dto.Steam.SteamSearchItem;
import lombok.RequiredArgsConstructor;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class SteamProvider implements MetadataProvider{
    private final SteamSearchClient searchClient;
    private final SteamHtmlParser parser;
    private final SteamDetailClient detailClient;

    @Override
    public boolean supports(String type) {
        return Objects.equals(type, "game");
    }

    public List<MetadataDTO> search(String keyword) {
        try {
            Document document = searchClient.search(keyword);
            List<SteamSearchItem> items = parser.parse(document);

            return items.stream().map(item -> {
                MetadataDTO dto = new MetadataDTO();
                dto.setSource("STEAM");
                dto.setSourceId(item.getAppId());
                dto.setTitle(item.getName());
                dto.setCover(item.getCover());
                return dto;
            }).toList();
        } catch (IOException e) {
            throw new RuntimeException("Steam搜索失败: " + e.getMessage(), e);
        }
    }

    public MetadataDTO detail(String appId){
        SteamDetailResponse response = detailClient.getDetail(appId);
        SteamGameData game = response.getResult().get(appId).getData();
        MetadataDTO dto = new MetadataDTO();
        dto.setSource("STEAM");
        dto.setSourceId(appId);
        dto.setTitle(game.getName());
        dto.setDescription(game.getDescription());
        dto.setCover(game.getCover());
        return dto;
    }
}
