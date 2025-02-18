package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static at.ac.fhcampuswien.fhmdb.models.Genre.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HomeControllerTest {
    private HomeController homeController;
    private Movie movie;
    private List<Movie> movies;

    // This method will run before each test to initialize the homeController object.
    @BeforeEach
    void setUp() {
        homeController = new HomeController();
        movie = new Movie("TestMovie", "Information", List.of(ACTION));
        movies = List.of(
                new Movie("DramaMovie", "Information", List.of(Genre.DRAMA)),
                new Movie("ActionMovie", "Information", List.of(Genre.ACTION)),
                new Movie("ComedyMovie", "Information", List.of(Genre.COMEDY))
        );
    }

    // Test case: Check if matchesGenre correctly matches the movie genre when it is ACTION.
    @Test
    void matchesGenre_genreAction_matchTrue() {
        boolean match = homeController.matchesGenre(movie, ACTION);
        assertTrue(match);
    }

    // Test case: Check if matchesGenre correctly matches the movie genre when it is ACTION.
    @Test
    void matchesGenre_genreDrama_matchFalse() {
        boolean match = homeController.matchesGenre(movie, DRAMA);
        assertFalse(match);
    }

    // Test case: Check if matchesGenre returns false when the genre is not ACTION.
    @Test
    void matchesGenre_noFilter_matchFalse() {
        boolean match = homeController.matchesGenre(movie, NO_FILTER);
        assertTrue(match);
    }

    // Parameterized test case: Check if the movie search filters correctly by genre and title.
    // The test runs with the specified pairs of search text and genre.
    @ParameterizedTest
    @CsvSource({
            "Acti, ACTION",
            "Dram, DRAMA",
            "Com, COMEDY"
    })
    void filterMovies_SearchMovieWithGenre_matchTrue(String searchText, Genre genre) {
        List<Movie> filteredMovies = homeController.filterMovies(movies, searchText, genre);

        assertThat(filteredMovies)
                .allMatch(movie -> movie.getTitle().contains(searchText),
                        "Every movie title should contain the search text: " + searchText);
    }

    // Parameterized test case: Check if the movie search does not return incorrect genres.
    // This ensures that the movie search doesn't incorrectly match a wrong genre.
    @ParameterizedTest
    @CsvSource({
            "ComedyMovie, ACTION",
            "Horror, DRAMA",
            "ActionMov, COMEDY"
    })

    void filterMovies_SearchMovieWithWrongGenre_matchFalse(String searchText, Genre genre) {
        List<Movie> filteredMovies = homeController.filterMovies(movies, searchText, genre);

        assertThat(filteredMovies)
                .noneMatch(movie -> movie.getTitle().contains(searchText));
    }

    // Parameterized test case: Check if an empty search string still correctly filters by genre.
    @ParameterizedTest
    @EnumSource(value = Genre.class, names = { "DRAMA", "ACTION" })
    void filterMovies_EmptySearchWithGenre_matchTrue(Genre genre) {
        List<Movie> filteredMovies = homeController.filterMovies(movies," ", genre);

        assertThat(filteredMovies)
                .allMatch(movie -> movie.getGenres().contains(genre),
                        "Every movie should contain the genre: " + genre);
    }
}