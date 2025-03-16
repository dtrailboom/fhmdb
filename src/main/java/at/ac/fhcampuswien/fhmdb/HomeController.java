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
import java.util.stream.Collectors;

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
    private final List<Movie> allMovies = movieControllerApi.getMovies(null, null, null, null);
    private final ObservableList<Movie> observableMovies = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        observableMovies.setAll(allMovies);

        // Initialize UI components
        movieListView.setItems(observableMovies);
        movieListView.setCellFactory(movieListView -> new MovieCell());

        // Add genre filter items
        genreComboBox.getItems().setAll(GenresEnum.values());

        // Focus the search field when the app starts
        searchField.requestFocus();
    }

    @FXML
    public void applyFilters() {
        var searchText = searchField.getText().toLowerCase().trim();
        var selectedGenresEnum = genreComboBox.getSelectionModel().getSelectedItem();
        //extend by release year & rating
        var filteredMovies = filterMovies(searchText, selectedGenresEnum);

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

    // add parameters for release year & rating
    public List<Movie> filterMovies(String searchText, GenresEnum selectedGenresEnum) {
//        movieControllerApi.getMovies(searchText, ...);
        return null;
    }

    // 4 additional methods here

    public long countMoviesFrom(List<Movie> movies, String director) {
        return movies.stream().filter(movie -> movie.getDirectors().contains(director)).count();
    }

    public List<Movie> getMoviesBetweenYears(List<Movie> movies, int startYear, int endYear) {
        return movies.stream().filter(movie -> movie.getReleaseYear() >= startYear && movie.getReleaseYear() <= endYear).collect(Collectors.toList());
    }

    public List<Movie> sortMovies(List<Movie> movies, boolean desc) {
        if (desc) {
            return movies.stream().sorted(Comparator.comparing(Movie::getTitle).reversed()).toList();
        }

        return movies.stream().sorted(Comparator.comparing(Movie::getTitle)).toList();
    }
}