package at.ac.fhcampuswien.fhmdb;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.openapitools.client.model.Movie;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.openapitools.client.model.Movie.GenresEnum.*;
import static org.openapitools.client.model.Movie.GenresEnum;

class HomeControllerTest {
    private HomeController homeController;
    private final Movie bladeRunner = new Movie().title("Blade Runner").description("Beschreibung von Blade Runner").genres(List.of(ACTION)).directors(List.of("Ridley Scott")).releaseYear(1982);
    private final Movie coolWorld = new Movie().title("Cool World").description("Beschreibung von Cool World").genres(List.of(COMEDY)).directors(List.of("Ralph Bakshi")).releaseYear(1992);
    private final Movie inception = new Movie().title("Inception").description("Beschreibung von Inception").genres(List.of(SCIENCE_FICTION)).directors(List.of("Christopher Nolan")).releaseYear(2010);
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

    @Test
    void countMoviesFrom_oneDirector_isOne(){
        var movies = List.of(bladeRunner, inception, coolWorld);
        var expected = 1;

        assertEquals(expected, homeController.countMoviesFrom(movies, "Christopher Nolan"));
    }

    @Test
    void countMoviesFrom_noDirector_isZero(){
        var movies = List.of(bladeRunner, inception, coolWorld);
        var expected = 0;

        assertEquals(expected, homeController.countMoviesFrom(movies, ""));
    }

    @Test
    void countMoviesFrom_directorWithoutMovie_isZero(){
        var movies = List.of(bladeRunner, inception, coolWorld);
        var expected = 0;

        assertEquals(expected, homeController.countMoviesFrom(movies, "Steve Jobs"));
    }

    @Test
    void countMoviesFrom_emptyMovieList_isZero(){
        var movies = new ArrayList<Movie>();
        var expected = 0;

        assertEquals(expected, homeController.countMoviesFrom(movies, "Christopher Nolan"));
    }

    @Test
    void getMoviesBetweenYears_emptyMovieList_isEmpty(){
        var movies = new ArrayList<Movie>();
        var expected = List.of();

        assertEquals(expected, homeController.getMoviesBetweenYears(movies, 1990, 2004));
    }

    @Test
    void getMoviesBetweenYears_noMatch_isEmpty(){
        var movies = List.of(bladeRunner, inception, coolWorld);
        var expected = List.of();

        assertEquals(expected, homeController.getMoviesBetweenYears(movies, 1312, 1529));
    }

    @Test
    void getMoviesBetweenYears_allMatch_movieListIsSame(){
        var movies = List.of(bladeRunner, inception, coolWorld);

        assertEquals(movies, homeController.getMoviesBetweenYears(movies, 1900, 2100));
    }

    @Test
    void getMoviesBetweenYears_singleMatch_matchesMovie(){
        var movies = List.of(bladeRunner, inception, coolWorld);
        var expected = List.of(coolWorld);

        assertEquals(expected, homeController.getMoviesBetweenYears(movies, 1992, 1993));
    }

    @Test
    void getMoviesBetweenYears_startYearBiggerThanEndYear_noMatch(){
        var movies = List.of(bladeRunner, inception, coolWorld);
        var expected = List.of();

        assertEquals(expected, homeController.getMoviesBetweenYears(movies, 1993, 1992));
    }

    @Test
    void getMoviesBetweenYears_startYearEqualToEndYear_matchesMovie(){
        var movies = List.of(bladeRunner, inception, coolWorld);
        var expected = List.of(coolWorld);

        assertEquals(expected, homeController.getMoviesBetweenYears(movies, 1992, 1992));
    }
}