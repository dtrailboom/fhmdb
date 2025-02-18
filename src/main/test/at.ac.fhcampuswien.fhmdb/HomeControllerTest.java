package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static at.ac.fhcampuswien.fhmdb.models.Genre.ACTION;
import static at.ac.fhcampuswien.fhmdb.models.Genre.NO_FILTER;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HomeControllerTest {
    private HomeController homeController;

    @BeforeEach
    void setUp() {
        homeController = new HomeController();
    }

    @Test
    void matchesGenre_genreAction_matchTrue() {
        var movie = new Movie("Test", "Bla", List.of(ACTION));
        var match = homeController.matchesGenre(movie, ACTION);
        assertTrue(match);
    }

    @Test
    void matchesGenre_noFilter_matchFalse() {
        var movie = new Movie("Test", "Bla", List.of(ACTION));
        var match = homeController.matchesGenre(movie, NO_FILTER);
        assertFalse(match);
    }


    @Test
    void testSortMovies() {
        // Test ascending sort
        homeController.sortBtn.setText("Sort (asc)");
        homeController.sortMovies();
        ObservableList<Movie> sortedMovies = homeController.movieListView.getItems();
        for (int i = 1; i < sortedMovies.size(); i++) {
            assertTrue(sortedMovies.get(i - 1).getTitle().compareToIgnoreCase(sortedMovies.get(i).getTitle()) <= 0);
        }

        // Test descending sort
        homeController.sortMovies();
        sortedMovies = homeController.movieListView.getItems();
        for (int i = 1; i < sortedMovies.size(); i++) {
            assertTrue(sortedMovies.get(i - 1).getTitle().compareToIgnoreCase(sortedMovies.get(i).getTitle()) >= 0);
        }
    }
}