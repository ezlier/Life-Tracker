package ezria.lifetrackr.module.metadata.dto.Bangum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BangumSearchResponse {
    private List<BangumAnime> data;
}
