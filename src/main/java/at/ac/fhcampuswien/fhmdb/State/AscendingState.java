package at.ac.fhcampuswien.fhmdb.State;

import org.openapitools.client.model.Movie;

import java.util.Comparator;
import java.util.List;


public class AscendingState implements SortState {

    public List<Movie> sort(List<Movie> movieList) {
        //operator for ascending
        Comparator<Movie> comparator = Comparator.comparing(
                Movie::getTitle,
                Comparator.nullsLast(String::compareToIgnoreCase)
        );

        return movieList.stream().sorted(comparator).toList();
    }
}
