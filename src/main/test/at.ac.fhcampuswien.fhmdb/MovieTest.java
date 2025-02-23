package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import org.junit.jupiter.api.Test;

import java.util.List;

import static at.ac.fhcampuswien.fhmdb.models.Genre.ACTION;
import static at.ac.fhcampuswien.fhmdb.models.Genre.DRAMA;
import static org.junit.jupiter.api.Assertions.*;

public class MovieTest {
    @Test
    void testMovieConstructor() {
        Movie movie = new Movie("Title", "Description", List.of(ACTION, DRAMA));
        assertEquals("Title", movie.title());
        assertEquals("Description", movie.description());
        assertTrue(movie.genres().contains(ACTION));
        assertTrue(movie.genres().contains(DRAMA));
    }

    @Test
    void testInitializeMoviesNotNull() {
        List<Movie> movies = Movie.initializeMovies();
        assertNotNull(movies);
    }




}
