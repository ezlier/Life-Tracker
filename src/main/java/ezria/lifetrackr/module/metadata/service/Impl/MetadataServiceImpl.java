package ezria.lifetrackr.module.metadata.service.Impl;

import ezria.lifetrackr.module.metadata.dto.MetadataDTO;
import ezria.lifetrackr.module.metadata.provider.MetadataProvider;
import ezria.lifetrackr.module.metadata.service.MetadataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class MetadataServiceImpl implements MetadataService {
    @Autowired
    private List<MetadataProvider> providers;

    @Override
    public List<MetadataDTO> search(String type, String keyword) throws IOException {
        MetadataProvider provider = providers.stream()
                        .filter(p -> p.supports(type))
                        .findFirst()
                        .orElseThrow();

        return provider.search(keyword);

    }

    @Override
    public MetadataDTO detail(String type, String sourceId) {
        MetadataProvider provider = providers.stream()
                .filter(p -> p.supports(type))
                .findFirst()
                .orElseThrow();

        return provider.detail(sourceId);
    }
}
