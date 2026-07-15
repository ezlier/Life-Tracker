package ezria.lifetrackr.module.metadata.dto.Steam;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class SteamGameData {
    private String name;

    @JsonProperty("short_description")
    private String description;

    @JsonProperty("header_image")
    private String cover;

    @JsonProperty("developers")
    private List<String> developers;

    private List<SteamGenre> genres;
}