package ezria.lifetrackr.module.metadata.dto.Steam;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class SteamDetailResponse {
    private Map<String,SteamResult> result = new HashMap<>();

    @JsonAnySetter
    public void add(String key, SteamResult value){
        result.put(key, value
        );
    }
}
