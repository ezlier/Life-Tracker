package ezria.lifetrackr.module.metadata.dto.Bangum;

import lombok.Data;

@Data
public class BangumiSearchRequest {
    private String keyword;
    private String sort="rank";
}