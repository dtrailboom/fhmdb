package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.ui.MovieCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.openapitools.client.api.MovieControllerApi;
import org.openapitools.client.model.Movie;
import org.openapitools.client.model.Movie.GenresEnum;

import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    @FXML
    public Button searchBtn;

    @FXML
    public TextField searchField;

    @FXML
    public ListView<Movie> movieListView;

    @FXML
    public ComboBox<GenresEnum> genreComboBox;

    @FXML
    public ComboBox<GenresEnum> releaseYearComboBox;

    @FXML
    public ComboBox<GenresEnum> ratingComboBox;

    @FXML
    public Button sortBtn;

    private final MovieControllerApi movieControllerApi = new MovieControllerApi();
    public List<Movie> allMovies = movieControllerApi.getMovies(null, null, null, null);

    public ObservableList<Movie> observableMovies = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        observableMovies.addAll(allMovies); // Add all movies to the observable list

        // Initialize UI components
        movieListView.setItems(observableMovies);
        movieListView.setCellFactory(movieListView -> new MovieCell());

        // Add genre filter items
        genreComboBox.getItems().setAll(GenresEnum.values());

        // Focus the search field when the app starts
        searchField.requestFocus();

        var x = new MovieControllerApi();
    }

    @FXML
    public void applyFilters() {
        var searchText = searchField.getText().toLowerCase().trim();
        var selectedGenresEnum = genreComboBox.getSelectionModel().getSelectedItem();
        var filteredMovies = filterMovies(allMovies, searchText, selectedGenresEnum);

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

    public List<Movie> filterMovies(List<Movie> movies, String searchText, GenresEnum selectedGenresEnum) {
        return movies.stream()
                .filter(movie -> matchesSearchQuery(movie, searchText))
                .filter(movie -> matchesGenre(movie, selectedGenresEnum))
                .toList();
    }

    public boolean matchesSearchQuery(Movie movie, String searchText) {
        return searchText.isEmpty() ||
                movie.getTitle().toLowerCase().contains(searchText.toLowerCase()) ||
                movie.getDescription().toLowerCase().contains(searchText.toLowerCase());
    }

    public boolean matchesGenre(Movie movie, GenresEnum selectedGenresEnum) {
        return selectedGenresEnum == null || (movie.getGenres() != null && movie.getGenres().contains(selectedGenresEnum));
    }

    public List<Movie> sortMovies(List<Movie> movies, boolean desc) {
        if (desc) {
            return movies.stream().sorted(Comparator.comparing(Movie::getTitle).reversed()).toList();
        }

        return movies.stream().sorted(Comparator.comparing(Movie::getTitle)).toList();
    }
}