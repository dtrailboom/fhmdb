package at.ac.fhcampuswien.fhmdb.exceptions;

public class DataBaseException extends Throwable {
    public DataBaseException(String message) {
        super(message);
    }

    public DataBaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
