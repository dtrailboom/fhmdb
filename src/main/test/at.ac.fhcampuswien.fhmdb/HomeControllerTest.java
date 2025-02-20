package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HomeControllerTest {
    private HomeController homeController;
    private List<Movie> movies;
    private final Movie bladeRunner = new Movie("Blade Runner", "Beschreibung von Blade Runner", Collections.singletonList("ACTION"));
    private final Movie coolWorld = new Movie("Cool World", "Beschreibung von Cool World", Collections.singletonList("COMEDY"));
    private final Movie inception = new Movie("Inception", "Beschreibung von Inception", Collections.singletonList("SCIENCE_FICTION"));

    @BeforeEach
    void setUp() {
        homeController = new HomeController();
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

    @Test
    void testSortMovies_CaseInsensitive_asc() {
        Movie movie1 = new Movie("Apple", "An Apple is tasty!", Collections.singletonList("FOO"));
        Movie movie2 = new Movie("banana", "banana", Collections.singletonList("FOO"));
        movies = List.of(movie2, movie1);
        var expected = List.of(movie1, movie2);
        var result = homeController.sortMovies(movies, false);

        assertEquals(expected, result);
    }

    @Test
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