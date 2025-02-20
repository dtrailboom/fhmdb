package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.ui.MovieCell;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    @FXML
    public JFXButton searchBtn;

    @FXML
    public TextField searchField;

    @FXML
    public JFXListView<Movie> movieListView;

    @FXML
    public JFXComboBox<String> genreComboBox;

    @FXML
    public JFXButton sortBtn;

    public List<Movie> allMovies = Movie.initializeMovies();

    public ObservableList<Movie> observableMovies = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        observableMovies.addAll(allMovies); // Add all movies to the observable list

        // Initialize UI components
        movieListView.setItems(observableMovies);
        movieListView.setCellFactory(movieListView -> new MovieCell());

        // Add genre filter items
        genreComboBox.getItems().addAll(
                "ACTION", "ADVENTURE", "ANIMATION", "BIOGRAPHY", "COMEDY",
                "CRIME", "DRAMA", "DOCUMENTARY", "FAMILY", "FANTASY",
                "HISTORY", "HORROR", "MUSICAL", "MYSTERY", "ROMANCE",
                "SCIENCE_FICTION", "SPORT", "THRILLER", "WAR", "WESTERN"
        );
        genreComboBox.setPromptText("Filter by Genre");

        // Add event handlers
        searchBtn.setOnAction(actionEvent -> applyFilters());
        sortBtn.setOnAction(actionEvent -> applySort());

        // Add Enter key support for the search field
        searchField.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                applyFilters(); // Trigger filtering
            } else if (keyEvent.getCode() == KeyCode.ESCAPE) {
                searchField.clear(); // Clear the search field
                applyFilters(); // Reapply filters (to show all movies)
            }
        });

        // Focus the search field when the app starts
        searchField.requestFocus();

    }

    public void applyFilters() {
        // 1. Benutzer-Eingaben aus Suchfeld und Genre-Filter lesen
        String searchText = searchField.getText().toLowerCase().trim();
        // Suchtext in Kleinbuchstaben umwandeln
        String selectedGenre = genreComboBox.getSelectionModel().getSelectedItem();


        List<Movie> filteredMovies = allMovies.stream()
                .filter(movie -> matchesSearchQuery(movie, searchText))
                .filter(movie -> matchesGenre(movie, selectedGenre))
                .toList();
        System.out.println("Vorher: " + observableMovies); // Debug-Ausgabe
        observableMovies.clear();
        System.out.println("Nach clear(): " + observableMovies); //

        observableMovies.addAll(filteredMovies);
        movieListView.refresh();
        System.out.println("Nach Add: " + observableMovies);
        System.out.println("Nach Add: " + observableMovies.size());
        System.out.println("Nach Add: " + filteredMovies.size());


        System.out.println(movieListView.getItems());

        System.out.println("Search Text: " + searchText);
        System.out.println("Selected Genre: " + selectedGenre);
        System.out.println("Filtered Movies: " + filteredMovies);


    }


    private boolean matchesSearchQuery(Movie movie, String searchText) {
        return searchText.isEmpty() ||
                movie.getTitle().toLowerCase().contains(searchText) ||
                movie.getDescription().toLowerCase().contains(searchText);
    }

    private boolean matchesGenre(Movie movie, String selectedGenre) {
        return selectedGenre == null || selectedGenre.isEmpty() ||
                (movie.getGenres() != null && movie.getGenres().contains(selectedGenre));
    }

    @FXML
    void applySort() {

        if (sortBtn.getText().equals("Sort (asc)")) {
            FXCollections.sort(observableMovies, (movie1, movie2) ->
                    movie1.getTitle().compareToIgnoreCase(movie2.getTitle()));
            sortBtn.setText("Sort (desc)");
        } else {
            FXCollections.sort(observableMovies, (movie1, movie2) ->
                    movie2.getTitle().compareToIgnoreCase(movie1.getTitle()));
            sortBtn.setText("Sort (asc)");
        }
    }

    public List<Movie> sortMovies(List<Movie> movies, boolean desc) {
        if (desc) {
            return movies.stream().sorted(Comparator.comparing(Movie::getTitle).reversed()).toList();
        }

        return movies.stream().sorted(Comparator.comparing(Movie::getTitle)).toList();
    }
}