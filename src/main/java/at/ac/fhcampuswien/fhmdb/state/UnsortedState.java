package at.ac.fhcampuswien.fhmdb.state;

import org.openapitools.client.model.Movie;

import java.util.List;

public class UnsortedState implements SortState {
    public List<Movie> sort(List<Movie> movieList) {
        return movieList;
    }
}
