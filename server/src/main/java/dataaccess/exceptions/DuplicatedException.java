package dataaccess.exceptions;

import com.google.gson.Gson;

import java.util.Map;

public class DuplicatedException extends DataAccessException {
    private static final int StatusCode = 403;

    public DuplicatedException(String message) {
        super(message);
    }

    public String toJson() {
        return new Gson().toJson(Map.of("message", getMessage(), "status", StatusCode));
    }

    public int getStatusCode() {
        return StatusCode;
    }
}
