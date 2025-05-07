package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.domain.MovieEntity;
import at.ac.fhcampuswien.fhmdb.domain.WatchlistMovieEntity;
import at.ac.fhcampuswien.fhmdb.eventHandler.ClickEventHandler;
import at.ac.fhcampuswien.fhmdb.exceptions.DataBaseException;
import at.ac.fhcampuswien.fhmdb.persistence.MovieRepository;
import at.ac.fhcampuswien.fhmdb.persistence.WatchlistRepository;
import at.ac.fhcampuswien.fhmdb.ui.MovieCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.openapitools.client.api.MovieControllerApi;
import org.openapitools.client.model.Movie;
import org.openapitools.client.model.Movie.GenresEnum;

import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class HomeController implements Initializable {
    @FXML public Button searchBtn;
    @FXML public TextField searchField;
    @FXML public ListView<Movie> movieListView;
    @FXML public ComboBox<GenresEnum> genreComboBox;
    @FXML public TextField releaseYearField;
    @FXML public TextField ratingField;
    @FXML public Button sortBtn;

    private final MovieControllerApi movieControllerApi = new MovieControllerApi();
    private final ObservableList<Movie> observableMovies = FXCollections.observableArrayList();
    private List<Movie> allMovies = new ArrayList<>();
    private List<Movie> watchListMovies = new ArrayList<>();
    private boolean isHomeView = true;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            allMovies = loadMovies();
            observableMovies.setAll(allMovies);
            WatchlistRepository.getInstance().clearWatchlist();
        } catch (SQLException e) {
            showAlert("Database Error", "Could not load movies: " + e.getMessage());
        }

        movieListView.setItems(observableMovies);
        movieListView.setCellFactory(param -> new MovieCell(true, addToWatchlistClicked, removeFromWatchlistClicked));
        genreComboBox.getItems().setAll(GenresEnum.values());
        searchField.requestFocus();
    }

    private final ClickEventHandler<Movie> addToWatchlistClicked = movie -> {
        try {
            WatchlistRepository.getInstance().addToWatchlist(new WatchlistMovieEntity(movie.getId().toString()));
        } catch (SQLException e) {
            showAlert("Watchlist Error", "Could not add to watchlist.");
        }
    };

    private final ClickEventHandler<Movie> removeFromWatchlistClicked = movie -> {
        try {
            WatchlistRepository.getInstance().removeFromWatchlist(movie.getId().toString());
            if (!isHomeView) {
                refreshWatchlistView();
            }
        } catch (SQLException e) {
            showAlert("Watchlist Error", "Could not remove from watchlist.");
        }
    };

    @FXML
    public void applyFilters() {
        String searchText = searchField.getText().toLowerCase().trim();
        GenresEnum selectedGenre = genreComboBox.getSelectionModel().getSelectedItem();
        String releaseYear = releaseYearField.getText().trim();
        String rating = ratingField.getText().trim();

        List<Movie> filtered = filterMovies(searchText, selectedGenre, releaseYear, rating);
        observableMovies.setAll(filtered);
    }

    @FXML
    void applySort() {
        if (sortBtn.getText().equals("Sort (asc)")) {
            observableMovies.setAll(sortMovies(observableMovies, true));
            sortBtn.setText("Sort (desc)");
        } else {
            observableMovies.setAll(sortMovies(observableMovies, false));
            sortBtn.setText("Sort (asc)");
        }
    }

    public List<Movie> filterMovies(String searchText, GenresEnum genre, String releaseYear, String rating) {
        String _searchText = (searchText != null && !searchText.trim().isEmpty()) ? searchText : null;
        String _genre = (genre != null) ? genre.getValue() : null;
        Integer _releaseYear = isValidYear(releaseYear);
        Double _rating = isValidRating(rating);

        return movieControllerApi.getMovies(_searchText, _genre, _releaseYear, _rating);

    }

    public Double isValidRating(String rating) {
        try {
            double r = Double.parseDouble(rating);
            return (r >= 0 && r <= 10) ? r : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public Integer isValidYear(String year) {
        try {
            int y = Integer.parseInt(year);
            return (y > 1900) ? y : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public List<Movie> sortMovies(List<Movie> movies, boolean descending) {
        return movies.stream()
                .sorted(Comparator.comparing(Movie::getTitle, Comparator.nullsLast(String::compareToIgnoreCase))
                        .reversed().reversed()) // correct asc/desc
                .sorted(descending ? Comparator.comparing(Movie::getTitle).reversed()
                        : Comparator.comparing(Movie::getTitle))
                .toList();
    }

    public void showHome(ActionEvent actionEvent) {
        isHomeView = true;
        observableMovies.setAll(allMovies);
        movieListView.setCellFactory(param -> new MovieCell(isHomeView, addToWatchlistClicked, removeFromWatchlistClicked));
    }

    public void showWatchList(ActionEvent actionEvent) {
        isHomeView = false;
        refreshWatchlistView();
        movieListView.setCellFactory(param -> new MovieCell(isHomeView, addToWatchlistClicked, removeFromWatchlistClicked));
    }

    private void refreshWatchlistView() {
        try {
            List<WatchlistMovieEntity> watchlist = WatchlistRepository.getInstance().getWatchlist();

            List<MovieEntity> movieEntities = new ArrayList<>();
            for (WatchlistMovieEntity entry : watchlist) {
                MovieEntity movieEntity = MovieRepository.getInstance().getMovieByApiId(entry.getApiId());
                if (movieEntity != null) {
                    movieEntities.add(movieEntity);
                }
            }

            watchListMovies = MovieEntity.toMovies(movieEntities);
            observableMovies.setAll(watchListMovies);
        } catch (Exception e) {
            showAlert("Error", "Could not load watchlist: " + e.getMessage());
        }
    }


    private List<Movie> loadMovies() throws SQLException {
        try {
            List<Movie> movies = movieControllerApi.getMovies(null, null, null, null);
            MovieRepository.getInstance().addAllMovies(movies);
            return movies;
        }
        catch (Exception e)
        {
            System.out.println("Falling back to cached DB movies: " + e.getMessage());

            List<MovieEntity> entities = MovieRepository.getInstance().getAllMovies();
            return MovieEntity.toMovies(entities);
        }
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

    public int getLongestMovieTitle(List<Movie> movies) {
        return movies.stream()
                .map(Movie::getTitle)
                .max(Comparator.comparingInt(String::length))
                .map(String::length)
                .orElse(0);
    }

    public long countMoviesFrom(List<Movie> movies, String director) {
        return movies.stream().filter(movie -> movie.getDirectors().contains(director)).count();
    }

    public List<Movie> getMoviesBetweenYears(List<Movie> movies, int startYear, int endYear) {
        return movies.stream().filter(movie -> movie.getReleaseYear() >= startYear && movie.getReleaseYear() <= endYear).collect(Collectors.toList());
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
