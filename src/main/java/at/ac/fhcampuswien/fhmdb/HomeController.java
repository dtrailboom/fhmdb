package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Genre;
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
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

import static at.ac.fhcampuswien.fhmdb.models.Genre.*;

public class HomeController implements Initializable {
    @FXML
    public JFXButton searchBtn;

    @FXML
    public TextField searchField;

    @FXML
    public JFXListView<Movie> movieListView;

    @FXML
    public JFXComboBox<Genre> genreComboBox;

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
                NO_FILTER, ACTION, ADVENTURE, ANIMATION, BIOGRAPHY, COMEDY,
                CRIME, DRAMA, DOCUMENTARY, FAMILY, FANTASY,
                HISTORY, HORROR, MUSICAL, MYSTERY, ROMANCE,
                SCIENCE_FICTION, SPORT, THRILLER, WAR, WESTERN
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
        observableMovies.clear();

        var searchText = searchField.getText().toLowerCase().trim();
        var selectedGenre = genreComboBox.getSelectionModel().getSelectedItem();
        var filteredMovies = filterMovies(allMovies, searchText, selectedGenre);

        observableMovies.addAll(filteredMovies);
        //movieListView.refresh();

    }

    public List<Movie> filterMovies(List<Movie> movies, String searchText, Genre selectedGenre) {
        return movies.stream()
                .filter(movie -> matchesSearchQuery(movie, searchText))
                .filter(movie -> matchesGenre(movie, selectedGenre))
                .toList();

    }

    public boolean matchesSearchQuery(Movie movie, String searchText) {
        return searchText.isEmpty() ||
                movie.title().toLowerCase().contains(searchText.toLowerCase()) ||
                movie.description().toLowerCase().contains(searchText.toLowerCase());
    }

    public boolean matchesGenre(Movie movie, Genre selectedGenre) {
        if(selectedGenre == null){
            return true;
        }
        return selectedGenre.equals(NO_FILTER) || movie.genres() != null && movie.genres().contains(selectedGenre);
    }

    @FXML
    void applySort() {
        if (sortBtn.getText().equals("Sort (asc)")) {
            var sortedMovies = sortMovies(observableMovies, true);
            observableMovies.setAll(sortedMovies);
            sortBtn.setText("Sort (desc)");
        } else {
            var sortedMovies = sortMovies(observableMovies, false);
            observableMovies.setAll(sortedMovies);
            sortBtn.setText("Sort (asc)");
        }
    }

    public List<Movie> sortMovies(List<Movie> movies, boolean desc) {
        if (desc) {
            return movies.stream().sorted(Comparator.comparing(Movie::title).reversed()).toList();
        }

        return movies.stream().sorted(Comparator.comparing(Movie::title)).toList();
    }
}