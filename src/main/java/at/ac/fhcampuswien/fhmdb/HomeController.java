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
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    public TextField releaseYearField;

    @FXML
    public TextField ratingField;

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
        var releaseYear = releaseYearField.getText().trim();
        var rating = ratingField.getText().trim();

        var filteredMovies = filterMovies(searchText, selectedGenresEnum, releaseYear, rating);
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
    public List<Movie> filterMovies(String searchText, GenresEnum selectedGenresEnum, String releaseYear, String rating)
    {
        String _searchText = (searchText != null && !searchText.trim().isEmpty()) ? searchText.trim() : null;
        String _genre = (selectedGenresEnum != null) ? selectedGenresEnum.getValue() : null;
        Integer _releaseYear = isValidYear(releaseYear);
        Double _rating = isValidRating(rating);

        // Hand over parameters, nullable are ignored
        return movieControllerApi.getMovies(_searchText, _genre, _releaseYear, _rating);
    }

    public Double isValidRating(String rating)
    {
        try {
            double _rating = Double.parseDouble(rating);
            if (_rating >= 0 && _rating <= 10) {
                return _rating;
            }
        } catch (NumberFormatException e) {
            // exception gets nullable
        }
        return null;
    }

    public Integer isValidYear(String year)
    {
        try {
            int _year = Integer.parseInt(year);
            if (_year > 1900) {
                return _year;
            }
        } catch (NumberFormatException e) {
            // exception gets nullable
        }
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

    public String getMostPopularActor(List<Movie> movies) {
        if (movies == null || movies.isEmpty()) {
            return null;
        }

        return movies.stream()
                .filter(Objects::nonNull)
                .map(Movie::getMainCast)
                .filter(Objects::nonNull)
                .flatMap(cast -> cast.stream())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }
    public int getLongestMovieTitle(List<Movie> movies){
        return movies.stream()
                .map(Movie::getTitle) // Titel extrahieren
                .max(Comparator.comparingInt(String::length)) // Längsten Titel suchen
                .map(String::length)
                .orElse(0); // Falls Liste leer ist, 0 zurückgeben
    }
}