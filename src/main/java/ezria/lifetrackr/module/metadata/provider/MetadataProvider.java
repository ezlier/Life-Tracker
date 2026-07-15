package ezria.lifetrackr.module.metadata.provider;

import ezria.lifetrackr.module.metadata.dto.MetadataDTO;

import java.io.IOException;
import java.util.List;

public interface MetadataProvider {

    /**
     * 当前Provider支持哪些类型
     */
    boolean supports(String type);

    /**
     * 搜索
     */
    List<MetadataDTO> search(String keyword) throws IOException;

    /**
     * 根据第三方ID获取详情
     */
    MetadataDTO detail(String sourceId);

}