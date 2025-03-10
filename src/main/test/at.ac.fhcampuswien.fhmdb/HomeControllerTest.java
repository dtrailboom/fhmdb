package at.ac.fhcampuswien.fhmdb;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.openapitools.client.model.Movie;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.openapitools.client.model.Movie.GenresEnum.*;
import static org.openapitools.client.model.Movie.GenresEnum;

class HomeControllerTest {
    private HomeController homeController;
    private final Movie bladeRunner = new Movie().title("Blade Runner").description("Beschreibung von Blade Runner").genres(List.of(ACTION));
    private final Movie coolWorld = new Movie().title("Cool World").description("Beschreibung von Cool World").genres(List.of(COMEDY));
    private final Movie inception = new Movie().title("Inception").description("Beschreibung von Inception").genres(List.of(SCIENCE_FICTION));
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
        Movie movie1 = new Movie().title("Apple").description("An Apple is tasty!").genres(List.of(ACTION));
        Movie movie2 = new Movie().title("banana").description("banana").genres(List.of(ACTION));
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
        Movie movie1 = new Movie().title("Apple").description("An Apple is tasty!").genres(List.of(ACTION));
        Movie movie2 = new Movie().title("banana").description("banana").genres(List.of(ACTION));
        movies = List.of(movie2, movie1);
        var expected = List.of(movie2, movie1);
        var result = homeController.sortMovies(movies, true);

        assertEquals(expected, result);
    }

    @Test
    void sortMovies_StableSort_asc() {
        Movie movie1 = new Movie().title("Inception").description("Test Description").genres(List.of(SCIENCE_FICTION));
        Movie movie2 = new Movie().title("Inception").description("Test Description").genres(List.of(SCIENCE_FICTION));
        movies = List.of(movie2, movie1);
        var expected = List.of(movie1, movie2);
        var result = homeController.sortMovies(movies, false);
        assertEquals(expected, result);
    }

    @Test
    void sortMovies_StableSort_desc() {
        Movie movie1 = new Movie().title("Inception").description("Test Description").genres(List.of(SCIENCE_FICTION));
        Movie movie2 = new Movie().title("Inception").description("Test Description").genres(List.of(SCIENCE_FICTION));
        movies = List.of(movie2, movie1);
        var expected = List.of(movie2, movie1);
        var result = homeController.sortMovies(movies, true);

        assertEquals(expected, result);
    }

    @Test
    void sortMovies_EmptyTitles_asc() {
        Movie movie1 = new Movie().title("").description("Test Description").genres(List.of(ACTION));
        Movie movie2 = new Movie().title("Blade Runner").description("Test Description").genres(List.of(ACTION));
        movies = List.of(movie2, movie1);
        var expected = List.of(movie1, movie2);
        var result = homeController.sortMovies(movies, false);

        assertEquals(expected, result);
    }

    @Test
    void sortMovies_EmptyTitles_desc() {
        Movie movie1 = new Movie().title("").description("Test Description").genres(List.of(ACTION));
        Movie movie2 = new Movie().title("Blade Runner").description("Test Description").genres(List.of(ACTION));
        movies = List.of(movie2, movie1);
        var expected = List.of(movie2, movie1);
        var result = homeController.sortMovies(movies, true);

        assertEquals(expected, result);
    }

    @Test
    void sortMovies_NonAlphanumericCharacters_asc() {
        Movie movie1 = new Movie().title("@Movie").description("Description 1").genres(List.of(ACTION));
        Movie movie2 = new Movie().title("!Movie").description("Description 2").genres(List.of(ACTION));
        Movie movie3 = new Movie().title("?Movie").description("Description 3").genres(List.of(ACTION));
        movies = List.of(movie2, movie3, movie1);
        var expected = List.of(movie2, movie3, movie1);
        var result = homeController.sortMovies(movies, false);

        assertEquals(expected, result);
    }

    @Test
    void sortMovies_NonAlphanumericCharacters_desc() {
        Movie movie1 = new Movie().title("@Movie").description("Description 1").genres(List.of(ACTION));
        Movie movie2 = new Movie().title("!Movie").description("Description 2").genres(List.of(ACTION));
        Movie movie3 = new Movie().title("?Movie").description("Description 3").genres(List.of(ACTION));
        movies = List.of(movie2, movie3, movie1);
        var expected = List.of(movie1, movie3, movie2);
        var result = homeController.sortMovies(movies, true);

        assertEquals(expected, result);
    }

    @Test
    void sortMovies_LongTitles_asc() {
        Movie movie1 = new Movie().title("A Very Long Movie Title That Tests Sorting").description("Description 1").genres(List.of(ACTION));
        Movie movie2 = new Movie().title("Another Long Movie Title").description("Description 2").genres(List.of(ACTION));
        movies = List.of(movie2, movie1);
        var result = homeController.sortMovies(movies, false);
        var expected = List.of(movie1, movie2);

        assertEquals(expected, result);
    }

    @Test
    void sortMovies_LongTitles_desc() {
        Movie movie1 = new Movie().title("A Very Long Movie Title That Tests Sorting").description("Description 1").genres(List.of(ACTION));
        Movie movie2 = new Movie().title("Another Long Movie Title").description("Description 2").genres(List.of(ACTION));
        movies = List.of(movie2, movie1);
        var result = homeController.sortMovies(movies, true);
        var expected = List.of(movie2, movie1);

        assertEquals(expected, result);
    }

    @Test
    void sortMovies_SpecialCharacters_asc() {
        Movie movie1 = new Movie().title("#Special Movie").description("Description 1").genres(List.of(ACTION));
        Movie movie2 = new Movie().title("123 Movie").description("Description 2").genres(List.of(ACTION));
        movies = List.of(movie2, movie1);
        var result = homeController.sortMovies(movies, false);
        var expected = List.of(movie1, movie2);

        assertEquals(expected, result);
    }

    @Test
    void sortMovies_SpecialCharacters_desc() {
        Movie movie1 = new Movie().title("#Special Movie").description("Description 1").genres(List.of(ACTION));
        Movie movie2 = new Movie().title("123 Movie").description("Description 2").genres(List.of(ACTION));
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
    void matchesGenre_noFilter_matchTrue() {
        boolean match = homeController.matchesGenre(bladeRunner, null);
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
    void filterMovies_SearchMovieWithGenre_matchTrue(String searchText, GenresEnum genre) {
        List<Movie> filteredMovies = homeController.filterMovies(movies, searchText, genre);

        assertThat(filteredMovies)
                .allMatch(movie -> movie.getTitle().contains(searchText),
                        "Every movie title should contain the search text: " + searchText);
    }

    @ParameterizedTest
    @CsvSource({
            "Acti, ACTION",
            "Dram, DRAMA",
            "Com, COMEDY"
    })
    void filterMovies_SearchMovieWithWrongGenre_matchFalse(String searchText, GenresEnum genre) {
        List<Movie> filteredMovies = homeController.filterMovies(movies, searchText, genre);

        assertThat(filteredMovies)
                .noneMatch(movie -> movie.getTitle().contains(searchText));
    }

    // Parameterized test case: Check if an empty search string still correctly filters by genre.
    @ParameterizedTest
    @EnumSource(value = GenresEnum.class, names = {"DRAMA", "ACTION"})
    void filterMovies_EmptySearchWithGenre_matchTrue(GenresEnum genre) {
        List<Movie> filteredMovies = homeController.filterMovies(movies, " ", genre);

        assertThat(filteredMovies)
                .allMatch(movie -> movie.getGenres().contains(genre),
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