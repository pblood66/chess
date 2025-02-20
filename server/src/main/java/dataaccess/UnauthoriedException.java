package dataaccess;

public class UnauthoriedException extends DataAccessException {
    public UnauthoriedException(String message) {
        super(message);
    }
}
