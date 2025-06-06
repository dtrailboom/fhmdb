package at.ac.fhcampuswien.fhmdb.State;

import org.openapitools.client.model.Movie;

import java.util.List;

public interface SortState {
    List<Movie> sort(List<Movie> movieList);
}







