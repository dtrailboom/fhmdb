package at.ac.fhcampuswien.fhmdb.State;

import org.openapitools.client.model.Movie;

import java.util.Comparator;
import java.util.List;

public class DescendingState implements SortState {

    public List<Movie> sort(List<Movie> movieList) {
        //operator for ascending
        Comparator<Movie> comparator = Comparator.comparing(
                Movie::getTitle,
                Comparator.nullsLast(String::compareToIgnoreCase)
        );

        comparator = comparator.reversed(); //reverse for descending! -> desc

        return movieList.stream().sorted(comparator).toList();
    }
}
