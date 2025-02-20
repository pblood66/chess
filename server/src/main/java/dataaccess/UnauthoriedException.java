package dataaccess;

import dataaccess.exceptions.DataAccessException;

public class UnauthoriedException extends DataAccessException {
    public UnauthoriedException(String message) {
        super(message);
    }
}
