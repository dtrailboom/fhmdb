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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HomeControllerTest {
    private HomeController homeController;

    // This method will run before each test to initialize the homeController object.
    @BeforeEach
    void setUp() {
        homeController = new HomeController();
    }

    // Test case: Check if matchesGenre correctly matches the movie genre when it is ACTION.
    @Test
    void matchesGenre_genreAction_matchTrue() {
        // Create a Movie object with ACTION genre.
        Movie movie = new Movie("TestMovie", "Information", List.of(ACTION));

        // Check if the movie's genre matches ACTION.
        boolean match = homeController.matchesGenre(movie, ACTION);

        // Assert that the genre matches correctly.
        assertTrue(match);
    }

    // Test case: Check if matchesGenre returns false when the genre is not ACTION.
    @Test
    void matchesGenre_noFilter_matchFalse() {
        // Create a Movie object with NO_FILTER genre.
        Movie movie = new Movie("TestMovie", "Information", List.of(NO_FILTER));

        // Check if the movie's genre matches ACTION.
        boolean match = homeController.matchesGenre(movie, ACTION);

        // Assert that the genre doesn't match, as NO_FILTER is not ACTION.
        assertFalse(match);
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
        // Create a list of movies with different genres.
        List<Movie> allMovies = List.of(
                new Movie("DramaMovie", "Information", List.of(Genre.DRAMA)),
                new Movie("ActionMovie", "Information", List.of(Genre.ACTION)),
                new Movie("ComedyMovie", "Information", List.of(Genre.COMEDY))
        );

        // Call the filterMovies method on homeController.
        List<Movie> filteredMovies = homeController.filterMovies(searchText, genre);

        // Iterate over the filtered movies and assert that the title contains the search text.
        for (Movie movie : filteredMovies) {
            assertTrue(movie.getTitle().contains(searchText));
        }
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
        // Create a list of movies with different genres.
        List<Movie> allMovies = List.of(
                new Movie("DramaMovie", "Information", List.of(Genre.DRAMA)),
                new Movie("ActionMovie", "Information", List.of(Genre.ACTION)),
                new Movie("ComedyMovie", "Information", List.of(Genre.COMEDY))
        );

        // Call the filterMovies method on homeController.
        List<Movie> filteredMovies = homeController.filterMovies(searchText, genre);

        // Iterate over the filtered movies and assert that the title does not contain the search text.
        // This ensures the movie's genre is being properly filtered out.
        for (Movie movie : filteredMovies) {
            assertFalse(movie.getTitle().contains(searchText));
        }
    }

    // Parameterized test case: Check if an empty search string still correctly filters by genre.
    @ParameterizedTest
    @EnumSource(value = Genre.class, names = { "DRAMA", "ACTION" })
    void filterMovies_EmptySearchWithGenre_matchTrue(Genre genre) {
        // Create a list of movies with DRAMA and ACTION genres.
        List<Movie> allMovies = List.of(
                new Movie("DramaMovie", "Information", List.of(Genre.DRAMA)),
                new Movie("ActionMovie", "Information", List.of(Genre.ACTION))
        );

        // Call the filterMovies method with an empty search string
        List<Movie> filteredMovies = homeController.filterMovies(" ", genre);

        // Iterate over the filtered movies and assert that the genre matches the passed genre.
        // This ensures the filter works even when there is no search text (just filtering by genre).
        for (Movie movie : filteredMovies) {
            assertTrue(movie.getGenres().contains(genre));
        }
    }
}