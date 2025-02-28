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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

import static at.ac.fhcampuswien.fhmdb.models.Genre.*;

public class HomeController implements Initializable {
    @FXML
    public Button searchBtn;

    @FXML
    public TextField searchField;

    @FXML
    public ListView<Movie> movieListView;

    @FXML
    public ComboBox<Genre> genreComboBox;

    @FXML
    public ComboBox<Genre> releaseYearComboBox;

    @FXML
    public ComboBox<Genre> ratingComboBox;

    @FXML
    public Button sortBtn;

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

        // Focus the search field when the app starts
        searchField.requestFocus();
    }

    @FXML
    public void applyFilters() {
        var searchText = searchField.getText().toLowerCase().trim();
        var selectedGenre = genreComboBox.getSelectionModel().getSelectedItem();
        var filteredMovies = filterMovies(allMovies, searchText, selectedGenre);

        observableMovies.setAll(filteredMovies);
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

    public List<Movie> sortMovies(List<Movie> movies, boolean desc) {
        if (desc) {
            return movies.stream().sorted(Comparator.comparing(Movie::title).reversed()).toList();
        }

        return movies.stream().sorted(Comparator.comparing(Movie::title)).toList();
    }
}