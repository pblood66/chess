package dataaccess;

public class DuplicatedException extends DataAccessException {
    public DuplicatedException(String message) {
        super(message);
    }
}
