package ezria.lifetrackr.module.metadata.dto.Bangum;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BangumAnime {
    @JsonProperty("name_cn")
    private String title;

    @JsonProperty("image")
    private String cover;

    @JsonProperty("summary")
    private String description;
}
