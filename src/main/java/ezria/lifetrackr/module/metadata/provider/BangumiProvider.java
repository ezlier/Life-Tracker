package ezria.lifetrackr.module.metadata.provider;

import ezria.lifetrackr.module.metadata.client.BangumiClient;
import ezria.lifetrackr.module.metadata.dto.Bangum.BangumAnime;
import ezria.lifetrackr.module.metadata.dto.Bangum.BangumSearchResponse;
import ezria.lifetrackr.module.metadata.dto.MetadataDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class BangumiProvider implements MetadataProvider{
    private final BangumiClient client;


    @Override
    public boolean supports(String type) {
        return (Objects.equals(type, "anime")|| Objects.equals(type, "book"));
    }

    @Override
    public List<MetadataDTO> search(String keyword) throws IOException {
        BangumSearchResponse response = client.searchAnime(keyword);
        return response.getData()
                .stream()
                .map(this::convert)
                .toList();
    }

    @Override
    public MetadataDTO detail(String sourceId) {
        return null;
    }

    private MetadataDTO convert(BangumAnime anime) {
        MetadataDTO dto = new MetadataDTO();
        dto.setTitle(anime.getTitle());
        dto.setCover(anime.getCover());
        dto.setDescription(anime.getDescription());
        return dto;
    }
}
