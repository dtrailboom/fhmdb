package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import  java.util.List;

import static at.ac.fhcampuswien.fhmdb.models.Genre.*;
import static org.junit.jupiter.api.Assertions.*;

class HomeControllerTest {




    private HomeController homeController;

    @BeforeEach
    void setUp() {
        homeController = new HomeController();




    }

    @Test
    void testApplyFilters() {
        // Test filtering by search text
        homeController.searchField.setText("");
        homeController.applyFilters();
        ObservableList<Movie> filteredMovies = homeController.movieListView.getItems();
        assertEquals(1, filteredMovies.size());
        assertEquals("Blade Runner", filteredMovies.get(0).getTitle());

        // Test filtering by genre
        homeController.genreComboBox.getSelectionModel().select(ACTION);
        homeController.applyFilters();
        filteredMovies = homeController.movieListView.getItems();
        assertTrue(filteredMovies.size() > 0);
        assertTrue(filteredMovies.stream().allMatch(movie -> movie.getGenres().contains(ACTION)));
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