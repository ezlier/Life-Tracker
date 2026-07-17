package ezria.lifetrackr.module.metadata.service;

import ezria.lifetrackr.module.metadata.dto.MetadataDTO;

import java.io.IOException;
import java.util.List;

public interface MetadataService {
    public List<MetadataDTO> search(String type, String keyword) throws IOException;

    public MetadataDTO detail(String type, String sourceId);
}
