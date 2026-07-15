package ezria.lifetrackr.module.metadata.dto.Steam;

import lombok.Data;

@Data
public class SteamSearchItem {
    private String appId;
    private String name;
    private String cover;
    private String url;
}