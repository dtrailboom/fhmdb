package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.List;

import static at.ac.fhcampuswien.fhmdb.models.Genre.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class HomeControllerTest {
    private HomeController homeController;
    private final Movie bladeRunner = new Movie("Blade Runner", "Beschreibung von Blade Runner", List.of(ACTION));
    private final Movie coolWorld = new Movie("Cool World", "Beschreibung von Cool World", List.of(COMEDY));
    private final Movie inception = new Movie("Inception", "Beschreibung von Inception", List.of(SCIENCE_FICTION));
    private List<Movie> movies = List.of(bladeRunner, coolWorld, inception);

    @BeforeEach
    void setUp() {
        homeController = new HomeController();
    }

    //-----SORTING------

    @Test
    void sortMoviesAsc() {
        var expected = List.of(bladeRunner, coolWorld, inception);
        var result = homeController.sortMovies(movies, false);

        assertEquals(expected, result);
    }

    @Test
    void sortMoviesDesc() {
        var expected = List.of(inception, coolWorld, bladeRunner);
        var result = homeController.sortMovies(movies, true);

        assertEquals(expected, result);
    }

    @Test
    void sortMovies_EmptyList() {
        assertDoesNotThrow(() -> homeController.sortMovies(List.of(), false));
    }

    @Test
    void sortMovies_CaseInsensitive_asc() {
        Movie movie1 = new Movie("Apple", "An Apple is tasty!", List.of(ACTION));
        Movie movie2 = new Movie("banana", "banana", List.of(ACTION));
        movies = List.of(movie2, movie1);
        var expected = List.of(movie1, movie2);
        var result = homeController.sortMovies(movies, false);

        assertEquals(expected, result);
    }

    // Parameterized test case: Check if the movie search does not return incorrect genres.
    // This ensures that the movie search doesn't incorrectly match a wrong genre.
    @ParameterizedTest
    @CsvSource({
            "ComedyMovie, ACTION",
            "Horror, DRAMA",
            "ActionMov, COMEDY"
    })
    void sortMovies_CaseInsensitive_desc() {
        Movie movie1 = new Movie("Apple", "An Apple is tasty!", List.of(ACTION));
        Movie movie2 = new Movie("banana", "banana", List.of(ACTION));
        movies = List.of(movie2, movie1);
        var expected = List.of(movie2, movie1);
        var result = homeController.sortMovies(movies, true);

        assertEquals(expected, result);
    }

    @Test
    void sortMovies_StableSort_asc() {
        Movie movie1 = new Movie("Inception", "Test Description", List.of(SCIENCE_FICTION));
        Movie movie2 = new Movie("Inception", "Test Description", List.of(SCIENCE_FICTION));
        movies = List.of(movie2, movie1);
        var expected = List.of(movie1, movie2);
        var result = homeController.sortMovies(movies, false);
        assertEquals(expected, result);
    }

    @Test
    void sortMovies_StableSort_desc() {
        Movie movie1 = new Movie("Inception", "Test Description", List.of(SCIENCE_FICTION));
        Movie movie2 = new Movie("Inception", "Test Description", List.of(SCIENCE_FICTION));
        movies = List.of(movie2, movie1);
        var expected = List.of(movie2, movie1);
        var result = homeController.sortMovies(movies, true);

        assertEquals(expected, result);
    }

    @Test
    void sortMovies_EmptyTitles_asc() {
        Movie movie1 = new Movie("", "Test Description", List.of(ACTION));
        Movie movie2 = new Movie("Blade Runner", "Test Description", List.of(ACTION));
        movies = List.of(movie2, movie1);
        var expected = List.of(movie1, movie2);
        var result = homeController.sortMovies(movies, false);

        assertEquals(expected, result);
    }

    @Test
    void sortMovies_EmptyTitles_desc() {
        Movie movie1 = new Movie("", "Test Description", List.of(ACTION));
        Movie movie2 = new Movie("Blade Runner", "Test Description", List.of(ACTION));
        movies = List.of(movie2, movie1);
        var expected = List.of(movie2, movie1);
        var result = homeController.sortMovies(movies, true);

        assertEquals(expected, result);
    }

    @Test
    void sortMovies_NonAlphanumericCharacters_asc() {
        Movie movie1 = new Movie("@Movie", "Description 1", List.of(ACTION));
        Movie movie2 = new Movie("!Movie", "Description 2", List.of(ACTION));
        Movie movie3 = new Movie("?Movie", "Description 3", List.of(ACTION));
        movies = List.of(movie2, movie3, movie1);
        var expected = List.of(movie2, movie3, movie1);
        var result = homeController.sortMovies(movies, false);

        assertEquals(expected, result);
    }

    @Test
    void sortMovies_NonAlphanumericCharacters_desc() {
        Movie movie1 = new Movie("@Movie", "Description 1", List.of(ACTION));
        Movie movie2 = new Movie("!Movie", "Description 2", List.of(ACTION));
        Movie movie3 = new Movie("?Movie", "Description 3", List.of(ACTION));
        movies = List.of(movie2, movie3, movie1);
        var expected = List.of(movie1, movie3, movie2);
        var result = homeController.sortMovies(movies, true);

        assertEquals(expected, result);
    }

    @Test
    void sortMovies_LongTitles_asc() {
        Movie movie1 = new Movie("A Very Long Movie Title That Tests Sorting", "Description 1", List.of(ACTION));
        Movie movie2 = new Movie("Another Long Movie Title", "Description 2", List.of(ACTION));
        movies = List.of(movie2, movie1);
        var result = homeController.sortMovies(movies, false);
        var expected = List.of(movie1, movie2);

        assertEquals(expected, result);
    }

    @Test
    void sortMovies_LongTitles_desc() {
        Movie movie1 = new Movie("A Very Long Movie Title That Tests Sorting", "Description 1", List.of(ACTION));
        Movie movie2 = new Movie("Another Long Movie Title", "Description 2", List.of(ACTION));
        movies = List.of(movie2, movie1);
        var result = homeController.sortMovies(movies, true);
        var expected = List.of(movie2, movie1);

        assertEquals(expected, result);
    }

    @Test
    void sortMovies_SpecialCharacters_asc() {
        Movie movie1 = new Movie("#Special Movie", "Description 1", List.of(ACTION));
        Movie movie2 = new Movie("123 Movie", "Description 2", List.of(ACTION));
        movies = List.of(movie2, movie1);
        var result = homeController.sortMovies(movies, false);
        var expected = List.of(movie1, movie2);

        assertEquals(expected, result);
    }

    @Test
    void sortMovies_SpecialCharacters_desc() {
        Movie movie1 = new Movie("#Special Movie", "Description 1", List.of(ACTION));
        Movie movie2 = new Movie("123 Movie", "Description 2", List.of(ACTION));
        movies = List.of(movie2, movie1);
        var result = homeController.sortMovies(movies, true);
        var expected = List.of(movie2, movie1);

        assertEquals(expected, result);
    }

    // Test case: Check if matchesGenre correctly matches the movie genre when it is ACTION.
    @Test
    void matchesGenre_genreAction_matchTrue() {
        boolean match = homeController.matchesGenre(bladeRunner, ACTION);
        assertTrue(match);
    }

    // Test case: Check if matchesGenre correctly matches the movie genre when it is ACTION.
    @Test
    void matchesGenre_genreDrama_matchFalse() {
        boolean match = homeController.matchesGenre(bladeRunner, DRAMA);
        assertFalse(match);
    }

    // Test case: Check if matchesGenre returns false when the genre is not ACTION.
    @Test
    void matchesGenre_noFilter_matchFalse() {
        boolean match = homeController.matchesGenre(bladeRunner, NO_FILTER);
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
                .allMatch(movie -> movie.title().contains(searchText),
                        "Every movie title should contain the search text: " + searchText);
    }

    @ParameterizedTest
    @CsvSource({
            "Acti, ACTION",
            "Dram, DRAMA",
            "Com, COMEDY"
    })
    void filterMovies_SearchMovieWithWrongGenre_matchFalse(String searchText, Genre genre) {
        List<Movie> filteredMovies = homeController.filterMovies(movies, searchText, genre);

        assertThat(filteredMovies)
                .noneMatch(movie -> movie.title().contains(searchText));
    }

    // Parameterized test case: Check if an empty search string still correctly filters by genre.
    @ParameterizedTest
    @EnumSource(value = Genre.class, names = {"DRAMA", "ACTION"})
    void filterMovies_EmptySearchWithGenre_matchTrue(Genre genre) {
        List<Movie> filteredMovies = homeController.filterMovies(movies, " ", genre);

        assertThat(filteredMovies)
                .allMatch(movie -> movie.genres().contains(genre),
                        "Every movie should contain the genre: " + genre);
    }

    @Test
    void testSearchMovies_SearchText_GenreNull() {
        //Wenn findet dann true
        var expected = List.of(bladeRunner);
        var result = homeController.filterMovies(movies, "Runner", null);
        assertEquals(expected, result);
    }

    @Test
    void matchesSearchQuery_WithEmptySearchText_isTrue() {
        assertTrue(homeController.matchesSearchQuery(movies.get(2), ""));
    }

    @Test
    void testMatchesSearchQuery_WithCaseInsensitiveSearch() {
        //Überprüfe, dass die Suche nicht case sensitive ist
        assertTrue(homeController.matchesSearchQuery(movies.get(2), "INCEPTION"));
        assertTrue(homeController.matchesSearchQuery(movies.get(2), "Beschreibung von Inception"));
    }

    @Test
    void testMatchesSearchQuery_WithTitleMatch() {
        //Wenn Suche im Titel enthalten ist true
        assertTrue(homeController.matchesSearchQuery(movies.get(2), "inception"));
    }

    @Test
    void testMatchesSearchQuery_WithDescriptionMatch() {
        //Wenn Suche in der Beschreibung enthalten ist, sollte das Ergebnis true sein
        assertTrue(homeController.matchesSearchQuery(movies.get(2), "Beschreibung von Inception"));
    }

    @Test
    void testMatchesSearchQuery_WithNoMatch() {
        //Wenn Suche weder im Titel noch in der Beschreibung enthalten ist, sollte das Ergebnis false sein
        assertFalse(homeController.matchesSearchQuery(movies.get(2), "comedy"));
    }
}