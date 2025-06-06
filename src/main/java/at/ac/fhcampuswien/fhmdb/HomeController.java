package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.state.*;
import at.ac.fhcampuswien.fhmdb.domain.MovieEntity;
import at.ac.fhcampuswien.fhmdb.domain.WatchlistMovieEntity;
import at.ac.fhcampuswien.fhmdb.eventHandler.ClickEventHandler;
import at.ac.fhcampuswien.fhmdb.exceptions.DataBaseException;
import at.ac.fhcampuswien.fhmdb.persistence.MovieRepository;
import at.ac.fhcampuswien.fhmdb.persistence.WatchlistRepository;
import at.ac.fhcampuswien.fhmdb.ui.MovieCell;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import org.openapitools.client.api.MovieControllerApi;
import org.openapitools.client.model.Movie;
import org.openapitools.client.model.Movie.GenresEnum;
import org.springframework.web.client.ResourceAccessException;

import java.net.URL;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class HomeController implements Initializable, InvalidationListener {
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
    @FXML
    private HBox filterBar;

    private final MovieControllerApi movieControllerApi = new MovieControllerApi();
    private final ObservableList<Movie> observableMovies = FXCollections.observableArrayList();
    private List<Movie> allMovies = new ArrayList<>();
    private List<Movie> watchListMovies = new ArrayList<>();
    private boolean isHomeView = true;
    private MovieRepository movieRepository;
    private WatchlistRepository watchlistRepository;
    private SortContext sortContext;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            movieRepository = MovieRepository.getInstance();
            watchlistRepository = WatchlistRepository.getInstance();
            watchlistRepository.addListener(this);
            allMovies = loadMovies();
            sortContext = new SortContext();
            sortContext.setState(new UnsortedState());
            observableMovies.setAll(allMovies);

        } catch (DataBaseException e) {
            showAlert("Database Error", "Could not load movies: " + e.getMessage(), Alert.AlertType.ERROR);
        }

        movieListView.setItems(observableMovies);
        movieListView.setCellFactory(param -> new MovieCell(true, addToWatchlistClicked, removeFromWatchlistClicked));
        genreComboBox.getItems().setAll(GenresEnum.values());
        searchField.requestFocus();
    }

    private final ClickEventHandler<Movie> addToWatchlistClicked = movie -> {
        try {
            watchlistRepository.addToWatchlist(new WatchlistMovieEntity(movie.getId().toString()));
        } catch (DataBaseException e) {
            showAlert("Watchlist Error", "Could not add to watchlist.", Alert.AlertType.ERROR);
        }
    };

    private final ClickEventHandler<Movie> removeFromWatchlistClicked = movie -> {
        try {
            watchlistRepository.removeFromWatchlist(movie.getId().toString());
            if (!isHomeView) {
                refreshWatchlistView();
            }
        } catch (DataBaseException e) {
            showAlert("Watchlist Error", "Could not remove from watchlist.", Alert.AlertType.ERROR);
        }
    };

    @FXML
    public void applyFilters() {
        String searchText = searchField.getText().toLowerCase().trim();
        GenresEnum selectedGenre = genreComboBox.getSelectionModel().getSelectedItem();
        String releaseYear = releaseYearField.getText().trim();
        String rating = ratingField.getText().trim();

        List<Movie> filtered = filterMovies(searchText, selectedGenre, releaseYear, rating);
        List<Movie> sorted = sortContext.sort(filtered);   //aktuelle Sortierung muss nach Filter erhalten bleiben
        observableMovies.setAll(sorted);
    }

    @FXML
    public void applySort() {
        if (sortBtn.getText().equals("Sort (asc)")) {
            //Aufsteigend Sort
            sortContext.setState(new AscendingState());
            List<Movie> sorted = sortContext.sort(observableMovies);
            observableMovies.setAll(sorted);

            sortBtn.setText("Sort (desc)");

        } else {
            //Absteigend Sort
            sortContext.setState(new DescendingState());
            List<Movie> sorted = sortContext.sort(observableMovies);
            observableMovies.setAll(sorted);

            sortBtn.setText("Sort (asc)");
        }
    }

    public List<Movie> filterMovies(String searchText, GenresEnum genre, String releaseYear, String rating) {
        String _searchText = (searchText != null && !searchText.trim().isEmpty()) ? searchText : null;
        String _genre = (genre != null) ? genre.getValue() : null;
        Integer _releaseYear = isValidYear(releaseYear);
        Double _rating = isValidRating(rating);

        try {
            return movieControllerApi.getMovies(_searchText, _genre, _releaseYear, _rating);
        } catch (ResourceAccessException e) {
            showAlert("Could not load movies: ", "Falling back to cached DB movies: " + e.getMessage(), Alert.AlertType.ERROR);
            return allMovies;
        }
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

    @FXML
    public void showHome(ActionEvent actionEvent) {
        isHomeView = true;
        observableMovies.setAll(allMovies);
        filterBar.setVisible(true);
        filterBar.setManaged(true);
        movieListView.setCellFactory(param -> new MovieCell(isHomeView, addToWatchlistClicked, removeFromWatchlistClicked));
    }

    @FXML
    public void showWatchList(ActionEvent actionEvent) {
        isHomeView = false;
        refreshWatchlistView();
        filterBar.setVisible(false);
        filterBar.setManaged(false);
        movieListView.setCellFactory(param -> new MovieCell(isHomeView, addToWatchlistClicked, removeFromWatchlistClicked));
    }

    private void refreshWatchlistView() {
        try {
            List<WatchlistMovieEntity> watchlist = watchlistRepository.getWatchlist();

            List<MovieEntity> movieEntities = new ArrayList<>();
            for (WatchlistMovieEntity entry : watchlist) {
                MovieEntity movieEntity = movieRepository.getMovieByApiId(entry.getApiId());
                if (movieEntity != null) {
                    movieEntities.add(movieEntity);
                }
            }

            watchListMovies = MovieEntity.toMovies(movieEntities);
            observableMovies.setAll(watchListMovies);
        } catch (DataBaseException e) {
            showAlert("Error", "Could not load watchlist: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private List<Movie> loadMovies() {
        try {
            List<Movie> movies = fetchRemoteMovies();
            cacheMovies(movies);
            return movies;
        } catch (ResourceAccessException e) {
            showAlert("Could not load remote movies", "Falling back to cached DB movies: " + e.getMessage(), Alert.AlertType.ERROR);
            return loadCachedMovies();
        }
    }

    private List<Movie> fetchRemoteMovies() throws ResourceAccessException {
        return movieControllerApi.getMovies(null, null, null, null);
    }

    private void cacheMovies(List<Movie> movies) {
        try {
            movieRepository.removeAll();
            movieRepository.addAllMovies(movies);
        } catch (DataBaseException e) {
            showAlert("Could not cache movies", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private List<Movie> loadCachedMovies() {
        try {
            return MovieEntity.toMovies(movieRepository.getAllMovies());
        } catch (DataBaseException e) {
            showAlert("Could not load cached movies", e.getMessage(), Alert.AlertType.ERROR);
            return Collections.emptyList();
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

    private void showAlert(String title, String msg, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    @Override
    public void invalidated(Observable observable) {
        showAlert("Watchlist", ((WatchlistRepository) observable).getMessage(), Alert.AlertType.INFORMATION);
    }
}
