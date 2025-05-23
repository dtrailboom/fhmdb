package at.ac.fhcampuswien.fhmdb.ui;

import at.ac.fhcampuswien.fhmdb.domain.WatchlistMovieEntity;
import at.ac.fhcampuswien.fhmdb.eventHandler.ClickEventHandler;
import at.ac.fhcampuswien.fhmdb.exceptions.DataBaseException;
import at.ac.fhcampuswien.fhmdb.persistence.DatabaseManager;
import at.ac.fhcampuswien.fhmdb.persistence.MovieRepository;
import at.ac.fhcampuswien.fhmdb.persistence.WatchlistRepository;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import org.openapitools.client.model.Movie;

import java.sql.SQLException;
import java.util.stream.Collectors;

public class MovieCell extends ListCell<Movie> {
    private final Label title = new Label();
    private final Label detail = new Label();
    private final Label genres = new Label();
    private final Button watchListBtn = new Button();
    private final Button detailsBtn = new Button();
    private final HBox buttonBox = new HBox();
    private final VBox layout = new VBox(title, detail, genres, buttonBox);

    private final boolean isHomeView;

    public MovieCell(boolean isHomeView, ClickEventHandler<Movie> addToWatchlist, ClickEventHandler<Movie> removeFromWatchlist) {
        super();
        this.isHomeView = isHomeView;

        // Button initialisieren
        watchListBtn.setText(isHomeView ? "Add to Watchlist" : "Remove");
        watchListBtn.getStyleClass().add("background-yellow");

        detailsBtn.setText("Show Details");
        detailsBtn.getStyleClass().add("background-yellow");

        // Watchlist-Button-Logik
        watchListBtn.setOnAction(event -> {
            try {
                if (isHomeView) {
                    addToWatchlist.onClick(getItem());
                } else {
                    removeFromWatchlist.onClick(getItem());
                }
            } catch (DataBaseException e) {
                showAlert("Error", e.getMessage());
            }
        });

        // Details Button Logik
        detailsBtn.setOnAction(event -> {
            Movie movie = getItem();
            if (movie != null) {
                // Zeige Details Dialog an
                showMovieDetails(movie);
            }
        });

        // Buttons zum Layout hinzufügen
        buttonBox.getChildren().addAll(watchListBtn, detailsBtn);
        buttonBox.setSpacing(10);

        // Styling
        title.getStyleClass().add("text-yellow");
        detail.getStyleClass().add("text-white");
        genres.getStyleClass().add("text-white");
        layout.setBackground(new Background(new BackgroundFill(Color.web("#454545"), null, null)));

        title.setFont(Font.font(20));
        detail.setWrapText(true);
        genres.setFont(Font.font(genres.getFont().getFamily(), FontPosture.ITALIC, 15));
        genres.setWrapText(true);

        layout.setPadding(new Insets(10));
        layout.setSpacing(10);
    }

    @Override
    protected void updateItem(Movie movie, boolean empty) {
        super.updateItem(movie, empty);

        if (empty || movie == null) {
            setText(null);
            setGraphic(null);
            getStyleClass().clear();
        } else {
            getStyleClass().add("movie-cell");
            title.setText(movie.getTitle());
            detail.setText(movie.getDescription() != null ?
                    movie.getDescription() : "No description available");
            genres.setText(movie.getGenres().stream()
                    .map(Enum::name)
                    .collect(Collectors.joining(", ")));

            setGraphic(layout);
        }
    }

    private void showMovieDetails(Movie movie) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(movie.getTitle());
        alert.setHeaderText("Movie Details");
        alert.setContentText("Year: " + movie.getReleaseYear() + "\n" +
                           "Rating: " + movie.getRating() + "\n" +
                           "Length: " + movie.getLengthInMinutes() + " minutes");
        alert.showAndWait();

    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
