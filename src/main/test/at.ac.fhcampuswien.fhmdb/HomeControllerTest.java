package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxToolkit;

import java.util.Collections;
import  java.util.List;
import java.util.concurrent.TimeoutException;
import static org.junit.jupiter.api.Assertions.*;

class HomeControllerTest {




    private HomeController homeController;

    @BeforeEach
    void setUp() throws TimeoutException {

        FxToolkit.registerPrimaryStage(); // Initialisiere JavaFX-Umgebung


        homeController = new HomeController();  // Manuelles Initialisieren der Steuerelemente
        homeController.sortBtn = new JFXButton();  //
        homeController.sortBtn.setText("Sort (asc)"); //Instanziere den Button
        homeController.movieListView = new JFXListView<>(); // Initialisiere das movieListView
        homeController.movieListView.setItems(homeController.observableMovies); // Dummy-Daten setzen

        // Dummy-Daten für die Filmliste
        homeController.observableMovies = FXCollections.observableArrayList(
                new Movie("Blade Runner", "Beschreibung von Blade Runner", Collections.singletonList("ACTION")),
                new Movie("Cool World", "Beschreibung von Cool World", Collections.singletonList("COMEDY")),
                new Movie("Inception", "Beschreibung von Inception", Collections.singletonList("SCIENCE_FICTION"))
        );


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
        homeController.genreComboBox.getSelectionModel().select("ACTION");
        homeController.applyFilters();
        filteredMovies = homeController.movieListView.getItems();
        assertTrue(filteredMovies.size() > 0);
        assertTrue(filteredMovies.stream().allMatch(movie -> movie.getGenres().contains("ACTION")));
    }

    //-----SORTING------

    @Test
    void testSortMoviesAsc() {
        // Test that sortedMoviesAsc is sorted in ascending order

        homeController.sortMovies(homeController.observableMovies, false); //

        for (int i = 1; i < homeController.observableMovies.size(); i++) {
            assertTrue(homeController.observableMovies.get(i - 1).getTitle()
                    .compareToIgnoreCase(homeController.observableMovies.get(i).getTitle()) <= 0);
        }
    }

    @Test
        void testSortMoviesDesc() {

        homeController.sortMovies(homeController.observableMovies, true); //

            for (int i = 1; i < homeController.observableMovies.size(); i++) {
                assertTrue(homeController.observableMovies.get(i - 1).getTitle()
                        .compareToIgnoreCase(homeController.observableMovies.get(i).getTitle()) >= 0);
            }
        }

    @Test
    void testSortButtonInitialState() {
        assertEquals("Sort (asc)", homeController.sortBtn.getText());
    }

    @Test
    void testSortButtonIsNotInitialState() {
        // Erwarteter Initialzustand des Buttons
        String initialState = "Sort (asc)";

        // Ändern Sie den Zustand des Buttons (beispielsweise durch Klick-Simulation)
        homeController.sortBtn.setText("Sort (desc)");

        // Überprüfen Sie, dass der Zustand des Buttons nicht mehr dem Initialzustand entspricht
        assertNotEquals(initialState, homeController.sortBtn.getText());
    }

    @Test
    void testSortButtonTextToggle() {

        homeController.applySort();
        assertEquals("Sort (desc)", homeController.sortBtn.getText());

        homeController.applySort();
        assertEquals("Sort (asc)", homeController.sortBtn.getText());
    }

    @Test
    void testSortMovies_EmptyList() {
        homeController.observableMovies.clear();
        assertDoesNotThrow(() -> homeController.applySort());
    }

    @Test
    void testSortMovies_SingleMovie() {

        homeController.observableMovies.setAll(new Movie("Test Movie", "Test Description", Collections.singletonList("ACTION")));
        //homeController.applySort();
        assertEquals(1, homeController.observableMovies.size());
    }

    @Test
    void testSortMovies_CaseInsensitive() {

        Movie movie1 = new Movie("Apple", "An Apple is tasty!", Collections.singletonList("FOO"));
        Movie movie2 = new Movie("banana", "banana", Collections.singletonList("FOO"));

        homeController.observableMovies.setAll(movie2, movie1);
        homeController.applySort();

        assertEquals("Apple", homeController.observableMovies.get(0).getTitle());
        assertEquals("banana", homeController.observableMovies.get(1).getTitle());
    }

    @Test
    void testSortMovies_StableSort() {

        Movie movie1 = new Movie("Inception", "Test Description", Collections.singletonList("SCIENCE_FICTION"));
        Movie movie2 = new Movie("Inception", "Test Description", Collections.singletonList("SCIENCE_FICTION"));

        homeController.observableMovies.setAll(movie1, movie2);
        homeController.applySort();

        assertSame(movie1, homeController.observableMovies.get(0));
        assertSame(movie2, homeController.observableMovies.get(1));
    }



    @Test
    void testSortMovies_EmptyTitles() {
        Movie movie1 = new Movie("", "Test Description", Collections.singletonList("ACTION"));
        Movie movie2 = new Movie("Blade Runner", "Test Description", Collections.singletonList("ACTION"));
        homeController.observableMovies.setAll(movie2, movie1);

        homeController.applySort(); // Sort ascending
        assertEquals("", homeController.observableMovies.get(0).getTitle());
        assertEquals("Blade Runner", homeController.observableMovies.get(1).getTitle());

        homeController.applySort(); // Sort descending
        assertEquals("Blade Runner", homeController.observableMovies.get(0).getTitle());
        assertEquals("", homeController.observableMovies.get(1).getTitle());
    }

    @Test
    void testSortMovies_NonAlphanumericCharacters() {
        Movie movie1 = new Movie("@Movie", "Description 1", Collections.singletonList("ACTION"));
        Movie movie2 = new Movie("!Movie", "Description 2", Collections.singletonList("ACTION"));
        Movie movie3 = new Movie("?Movie", "Description 3", Collections.singletonList("ACTION"));
        homeController.observableMovies.setAll(movie2, movie3, movie1);

        // Sort in ascending order
        homeController.applySort();
        assertEquals("!Movie", homeController.observableMovies.get(0).getTitle());
        assertEquals("?Movie", homeController.observableMovies.get(1).getTitle());
        assertEquals("@Movie", homeController.observableMovies.get(2).getTitle());

        // Sort in descending order
        homeController.applySort();
        assertEquals("@Movie", homeController.observableMovies.get(0).getTitle());
        assertEquals("?Movie", homeController.observableMovies.get(1).getTitle());
        assertEquals("!Movie", homeController.observableMovies.get(2).getTitle());


    }

    @Test
    void testSortMovies_LongTitles() {
        Movie movie1 = new Movie("A Very Long Movie Title That Tests Sorting", "Description 1", Collections.singletonList("ACTION"));
        Movie movie2 = new Movie("Another Long Movie Title", "Description 2", Collections.singletonList("ACTION"));
        homeController.observableMovies.setAll(movie2, movie1);

        // Sort in ascending order
        homeController.applySort();
        assertEquals("A Very Long Movie Title That Tests Sorting", homeController.observableMovies.get(0).getTitle());
        assertEquals("Another Long Movie Title", homeController.observableMovies.get(1).getTitle());

        // Sort in descending order
        homeController.applySort();
        assertEquals("Another Long Movie Title", homeController.observableMovies.get(0).getTitle());
        assertEquals("A Very Long Movie Title That Tests Sorting", homeController.observableMovies.get(1).getTitle());
    }

    @Test
    void testSortMovies_SpecialCharacters() {
        Movie movie1 = new Movie("#Special Movie", "Description 1", Collections.singletonList("ACTION"));
        Movie movie2 = new Movie("123 Movie", "Description 2", Collections.singletonList("ACTION"));
        homeController.observableMovies.setAll(movie1, movie2);

        // Sort in ascending order
        homeController.applySort();
        assertEquals("#Special Movie", homeController.observableMovies.get(0).getTitle());
        assertEquals("123 Movie", homeController.observableMovies.get(1).getTitle());

        // Sort in descending order
        homeController.applySort();
        assertEquals("123 Movie", homeController.observableMovies.get(0).getTitle());
        assertEquals("#Special Movie", homeController.observableMovies.get(1).getTitle());
    }


}