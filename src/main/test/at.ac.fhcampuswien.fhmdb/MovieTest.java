package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MovieTest {
    @Test
    void testMovieConstructor() {
        Movie movie = new Movie("Title", "Description", List.of("ACTION", "DRAMA"));
        assertEquals("Title", movie.getTitle());
        assertEquals("Description", movie.getDescription());
        assertTrue(movie.getGenres().contains("ACTION"));
        assertTrue(movie.getGenres().contains("DRAMA"));
    }

    @Test
    void testInitializeMovies() {
        List<Movie> movies = Movie.initializeMovies();
        assertNotNull(movies);
        assertFalse(movies.isEmpty());
        assertEquals(10, movies.size()); // Assuming 10 movies are initialized
    }




}
