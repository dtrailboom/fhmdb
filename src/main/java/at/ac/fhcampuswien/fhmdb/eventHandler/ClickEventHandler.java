package at.ac.fhcampuswien.fhmdb.eventHandler;

import at.ac.fhcampuswien.fhmdb.exceptions.DataBaseException;

@FunctionalInterface
public interface ClickEventHandler<T> {
    void onClick(T item) throws DataBaseException;
}
