package at.ac.fhcampuswien.fhmdb.domain;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.openapitools.client.model.Movie;
import org.openapitools.client.model.Movie.GenresEnum;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@DatabaseTable(tableName = "movies")
public class MovieEntity {

    @DatabaseField(generatedId = true)
    public long id;

    @DatabaseField(unique = true, canBeNull = false)
    public String apiId;

    @DatabaseField(canBeNull = false)
    public String title;

    @DatabaseField
    public String description;

    @DatabaseField
    public String genres;

    @DatabaseField
    public int releaseYear;

    @DatabaseField
    public String imgUrl;

    @DatabaseField
    public int lengthInMinutes;

    @DatabaseField
    public double rating;

    public MovieEntity() {
        // ORMLite benötigt einen no-arg Konstruktor
    }

    public MovieEntity(long id, String apiId, String title, String description, String genres, int releaseYear, String imgUrl, int lengthInMinutes, double rating) {
        this.id = id;
        this.apiId = apiId;
        this.title = title;
        this.description = description;
        this.genres = genres;
        this.releaseYear = releaseYear;
        this.imgUrl = imgUrl;
        this.lengthInMinutes = lengthInMinutes;
        this.rating = rating;
    }

    // ---------- Hilfsmethoden ----------
    public static String genresToString(List<GenresEnum> genres) {
        if (genres == null || genres.isEmpty()) {
            return "";
        }
        String[] genresArray = genres.stream().map(GenresEnum::toString).toArray(String[]::new);
        return String.join(",", genresArray);
    }

    public static List<MovieEntity> fromMovies(List<Movie> movies) {
        return movies.stream().map(movie -> {
            MovieEntity entity = new MovieEntity();
            assert movie.getId() != null;
            entity.id = movie.getId().getMostSignificantBits();
            entity.apiId = movie.getId().toString();
            entity.title = movie.getTitle();
            entity.description = movie.getDescription();
            entity.genres = genresToString(movie.getGenres());
            entity.releaseYear = movie.getReleaseYear();
            entity.imgUrl = movie.getImgUrl();
            entity.lengthInMinutes = movie.getLengthInMinutes();
            entity.rating = movie.getRating();
            return entity;

        }).collect(Collectors.toList());



    }

    public static List<Movie> toMovies(List<MovieEntity> entities) {
        return entities.stream().map(entity -> {
            Movie movie = new Movie();
            movie.setId(UUID.fromString(entity.apiId));
            movie.setTitle(entity.title);
            movie.setDescription(entity.description);
            if (entity.genres != null && !entity.genres.isBlank()) {
                movie.setGenres(
                    Arrays.stream(entity.genres.split(","))
                            .map(String::trim)
                            .map(Movie.GenresEnum::fromValue)
                            .collect(Collectors.toList())
            );
            } else {
                movie.setGenres(Collections.emptyList());
            }
            movie.setReleaseYear(entity.releaseYear);
            movie.setImgUrl(entity.imgUrl);
            movie.setLengthInMinutes(entity.lengthInMinutes);
            movie.setRating(entity.rating);
            return movie;
        }).collect(Collectors.toList());

    }

// GETTER Methoden

    public long getId() {
        return id;
    }
    public String getApiId() {
        return apiId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getGenres() {
        return genres;
    }
    public int getReleaseYear() {
        return releaseYear;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public int getLengthInMinutes() {
        return lengthInMinutes;
    }

    public double getRating() {
        return rating;
    }
}
