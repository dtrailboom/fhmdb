package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import  java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HomeControllerTest {
    private HomeController homeController;

    @BeforeEach
    void setUp() {
        // Create or inject the homeController
        if (homeController == null) {
            // Initialize  homeController
        }
    }


    @Test
    void testApplyFilterWithValue(/*String filter*/) {
        // Test filtering by search text

        //homeController.applyFilters(filter);
        ObservableList<Movie> filteredMovies = homeController.movieListView.getItems();
        assertEquals(1, filteredMovies.size());
        assertEquals("Blade Runner", filteredMovies.get(0).getTitle());
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