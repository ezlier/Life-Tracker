package ezria.lifetrackr.module.metadata.dto.Steam;

import lombok.Data;

@Data
public class SteamResult {
    private Boolean success;
    private SteamGameData data;
}
