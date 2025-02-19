package at.ac.fhcampuswien.fhmdb.ui;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.FontPosture;

public class MovieCell extends ListCell<Movie> {
    private final Label title = new Label();
    private final Label detail = new Label();
    private final Label genres = new Label();
    private final VBox layout = new VBox(title, detail, genres);


    @Override
    public void updateItem(Movie movie, boolean empty) {
        super.updateItem(movie, empty);
        System.out.println("Updating cell: " + (empty ? "empty" : movie.getTitle())); // Debug

        if (empty || movie == null) {
            setText(null);
            setGraphic(null);
            this.getStyleClass().clear();

        } else {
            this.getStyleClass().add("movie-cell");
            title.setText(movie.getTitle());
            detail.setText(
                    movie.getDescription() != null
                            ? movie.getDescription()
                            : "No description available"
            );

            genres.setText("Genres: " + String.join(", ", movie.getGenres()));


            // color scheme
            title.getStyleClass().add("text-yellow");
            detail.getStyleClass().add("text-white");
            genres.getStyleClass().add("text-white");    //++++++++++++++++++++++++
            layout.setBackground(new Background(new BackgroundFill(Color.web("#454545"), null, null)));

            // layout
            title.fontProperty().set(title.getFont().font(20));
            detail.setMaxWidth(this.getScene().getWidth() - 30);
            detail.setWrapText(true);
            //+++++++++++++++++++++++++++++++++++++++++++++++++++++
            genres.fontProperty().set(genres.getFont().font(15)); //D
            genres.fontProperty().set(Font.font(genres.getFont().getFamily(), FontPosture.ITALIC, genres.getFont().getSize())); //D
            genres.setWrapText(true); //D

            layout.setPadding(new Insets(10));
            layout.spacingProperty().set(10);
            layout.alignmentProperty().set(javafx.geometry.Pos.CENTER_LEFT);
            setGraphic(layout);
        }
    }



}


