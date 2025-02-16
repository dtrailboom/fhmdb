package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.ui.MovieCell;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MovieCellTest {

    @Test
    void testUpdateItem() {
        MovieCell movieCell = new MovieCell();
        Movie movie = new Movie("Title", "Description", List.of("ACTION", "DRAMA"));

        // Test non-empty cell
        movieCell.updateItem(movie, false);
        assertNotNull(movieCell.getGraphic());
        VBox layout = (VBox) movieCell.getGraphic();
        Label titleLabel = (Label) layout.getChildren().get(0);
        Label detailLabel = (Label) layout.getChildren().get(1);
        Label genresLabel = (Label) layout.getChildren().get(2);

        assertEquals("Title", titleLabel.getText());
        assertEquals("Description", detailLabel.getText());
        assertEquals("Genres: ACTION, DRAMA", genresLabel.getText());

        // Test empty cell
        movieCell.updateItem(null, true);
        assertNull(movieCell.getGraphic());
    }



}
