package ezria.lifetrackr.module.metadata.provider;

import ezria.lifetrackr.module.metadata.client.TmdbClient;
import ezria.lifetrackr.module.metadata.dto.MetadataDTO;
import ezria.lifetrackr.module.metadata.dto.tmdb.TmdbMovie;
import ezria.lifetrackr.module.metadata.dto.tmdb.TmdbSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class TmdbProvider implements MetadataProvider{
    private final TmdbClient client;


    @Override
    public boolean supports(String type) {
        return Objects.equals(type, "movie");
    }

    @Override
    public List<MetadataDTO> search(String keyword) {
        TmdbSearchResponse response =
                client.searchMovie(keyword);
        return response.getResults()
                .stream()
                .map(this::convert)
                .toList();
    }

    @Override
    public MetadataDTO detail(String sourceId) {
        return null;
    }

    private MetadataDTO convert(TmdbMovie movie){
        MetadataDTO dto =
                new MetadataDTO();
        dto.setTitle(movie.getTitle());
        dto.setDescription(movie.getOverview());
        dto.setCover(
                "https://image.tmdb.org/t/p/w500"
                        +movie.getPosterPath());
        dto.setRating(movie.getVoteAverage());
        dto.setSource("TMDB");
        dto.setSourceId(
                movie.getId().toString());
        return dto;

    }
}
