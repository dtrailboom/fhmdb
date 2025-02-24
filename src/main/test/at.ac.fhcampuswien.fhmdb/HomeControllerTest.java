package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.Collections;

import static at.ac.fhcampuswien.fhmdb.models.Genre.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

class HomeControllerTest {
    private HomeController homeController;
    private List<Movie> movies;
  private Movie movie;
    private final Movie bladeRunner = new Movie("Blade Runner", "Beschreibung von Blade Runner", Collections.singletonList("ACTION"));
    private final Movie coolWorld = new Movie("Cool World", "Beschreibung von Cool World", Collections.singletonList("COMEDY"));
    private final Movie inception = new Movie("Inception", "Beschreibung von Inception", Collections.singletonList("SCIENCE_FICTION"));

    // This method will run before each test to initialize the homeController object.
    @BeforeEach
    void setUp() {
        homeController = new HomeController();
        movie = new Movie("TestMovie", "Information", List.of(ACTION));
        movies = List.of(
                new Movie("Blade Runner", "Beschreibung von Blade Runner", Collections.singletonList("ACTION")),
                new Movie("Cool World", "Beschreibung von Cool World", Collections.singletonList("COMEDY")),
                new Movie("Inception", "Beschreibung von Inception", Collections.singletonList("SCIENCE_FICTION"))
        );
    }

    //-----SORTING------

    @Test
    void testSortMoviesAsc() {
        var expected = List.of(bladeRunner, coolWorld, inception);
        var result = homeController.sortMovies(movies, false);

        assertEquals(expected, result);
    }

    @Test
    void testSortMoviesDesc() {
        var expected = List.of(inception, coolWorld, bladeRunner);
        var result = homeController.sortMovies(movies, true);

        assertEquals(expected, result);
    }

    @Test
    void testSortMovies_EmptyList() {
        assertDoesNotThrow(() -> homeController.sortMovies(List.of(), false));
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
    void testSortMovies_CaseInsensitive_asc() {
        Movie movie1 = new Movie("Apple", "An Apple is tasty!", Collections.singletonList("FOO"));
        Movie movie2 = new Movie("banana", "banana", Collections.singletonList("FOO"));
        movies = List.of(movie2, movie1);
        var expected = List.of(movie1, movie2);
        var result = homeController.sortMovies(movies, false);

        assertEquals(expected, result);
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
    void testSortMovies_CaseInsensitive_desc() {
        Movie movie1 = new Movie("Apple", "An Apple is tasty!", Collections.singletonList("FOO"));
        Movie movie2 = new Movie("banana", "banana", Collections.singletonList("FOO"));
        movies = List.of(movie2, movie1);
        var expected = List.of(movie2, movie1);
        var result = homeController.sortMovies(movies, true);

        assertEquals(expected, result);
    }

    @Test
    void testSortMovies_StableSort_asc() {
        Movie movie1 = new Movie("Inception", "Test Description", Collections.singletonList("SCIENCE_FICTION"));
        Movie movie2 = new Movie("Inception", "Test Description", Collections.singletonList("SCIENCE_FICTION"));
        movies = List.of(movie2, movie1);
        var expected = List.of(movie1, movie2);
        var result = homeController.sortMovies(movies, false);
        assertEquals(expected, result);
    }

    @Test
    void testSortMovies_StableSort_desc() {
        Movie movie1 = new Movie("Inception", "Test Description", Collections.singletonList("SCIENCE_FICTION"));
        Movie movie2 = new Movie("Inception", "Test Description", Collections.singletonList("SCIENCE_FICTION"));
        movies = List.of(movie2, movie1);
        var expected = List.of(movie2, movie1);
        var result = homeController.sortMovies(movies, true);

        assertEquals(expected, result);
    }

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
      
    @Test
    void testSortMovies_EmptyTitles_asc() {
        Movie movie1 = new Movie("", "Test Description", Collections.singletonList("ACTION"));
        Movie movie2 = new Movie("Blade Runner", "Test Description", Collections.singletonList("ACTION"));
        movies = List.of(movie2, movie1);
        var expected = List.of(movie1, movie2);
        var result = homeController.sortMovies(movies, false);

        assertEquals(expected, result);
    }

    @Test
    void testSortMovies_EmptyTitles_desc() {
        Movie movie1 = new Movie("", "Test Description", Collections.singletonList("ACTION"));
        Movie movie2 = new Movie("Blade Runner", "Test Description", Collections.singletonList("ACTION"));
        movies = List.of(movie2, movie1);
        var expected = List.of(movie2, movie1);
        var result = homeController.sortMovies(movies, true);

        assertEquals(expected, result);
    }

    @Test
    void testSortMovies_NonAlphanumericCharacters_asc() {
        Movie movie1 = new Movie("@Movie", "Description 1", Collections.singletonList("ACTION"));
        Movie movie2 = new Movie("!Movie", "Description 2", Collections.singletonList("ACTION"));
        Movie movie3 = new Movie("?Movie", "Description 3", Collections.singletonList("ACTION"));
        movies = List.of(movie2, movie3, movie1);
        var expected = List.of(movie2, movie3, movie1);
        var result = homeController.sortMovies(movies, false);

        assertEquals(expected, result);
    }

    @Test
    void testSortMovies_NonAlphanumericCharacters_desc() {
        Movie movie1 = new Movie("@Movie", "Description 1", Collections.singletonList("ACTION"));
        Movie movie2 = new Movie("!Movie", "Description 2", Collections.singletonList("ACTION"));
        Movie movie3 = new Movie("?Movie", "Description 3", Collections.singletonList("ACTION"));
        movies = List.of(movie2, movie3, movie1);
        var expected = List.of(movie1, movie3, movie2);
        var result = homeController.sortMovies(movies, true);

        assertEquals(expected, result);
    }

    @Test
    void testSortMovies_LongTitles_asc() {
        Movie movie1 = new Movie("A Very Long Movie Title That Tests Sorting", "Description 1", Collections.singletonList("ACTION"));
        Movie movie2 = new Movie("Another Long Movie Title", "Description 2", Collections.singletonList("ACTION"));
        movies = List.of(movie2, movie1);
        var result = homeController.sortMovies(movies, false);
        var expected = List.of(movie1, movie2);

        assertEquals(expected, result);
    }

    @Test
    void testSortMovies_LongTitles_desc() {
        Movie movie1 = new Movie("A Very Long Movie Title That Tests Sorting", "Description 1", Collections.singletonList("ACTION"));
        Movie movie2 = new Movie("Another Long Movie Title", "Description 2", Collections.singletonList("ACTION"));
        movies = List.of(movie2, movie1);
        var result = homeController.sortMovies(movies, true);
        var expected = List.of(movie2, movie1);

        assertEquals(expected, result);
    }

    @Test
    void testSortMovies_SpecialCharacters_asc() {
        Movie movie1 = new Movie("#Special Movie", "Description 1", Collections.singletonList("ACTION"));
        Movie movie2 = new Movie("123 Movie", "Description 2", Collections.singletonList("ACTION"));
        movies = List.of(movie2, movie1);
        var result = homeController.sortMovies(movies, false);
        var expected = List.of(movie1, movie2);

        assertEquals(expected, result);
    }

    @Test
    void testSortMovies_SpecialCharacters_desc() {
        Movie movie1 = new Movie("#Special Movie", "Description 1", Collections.singletonList("ACTION"));
        Movie movie2 = new Movie("123 Movie", "Description 2", Collections.singletonList("ACTION"));
        movies = List.of(movie2, movie1);
        var result = homeController.sortMovies(movies, true);
        var expected = List.of(movie2, movie1);

        assertEquals(expected, result);
    }
}