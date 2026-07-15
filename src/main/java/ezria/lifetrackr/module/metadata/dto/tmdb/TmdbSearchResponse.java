package ezria.lifetrackr.module.metadata.dto.tmdb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TmdbSearchResponse {
    private Integer page;
    private List<TmdbMovie> results;
    private Integer totalPages;
    private Integer totalResults;
}
