package at.ac.fhcampuswien.fhmdb.state;

import org.openapitools.client.model.Movie;

import java.util.List;

public class SortContext {

    private SortState currentState;

    public void setState(SortState state) {
        this.currentState = state;
    }

    public List<Movie> sort(List<Movie> movieList) {
        if (currentState == null) {
            throw new IllegalStateException("No sorting order.");
        }
        return currentState.sort(movieList);
    }
}
