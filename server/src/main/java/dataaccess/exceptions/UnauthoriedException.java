package dataaccess.exceptions;

import com.google.gson.Gson;

import java.util.Map;

public class UnauthoriedException extends DataAccessException {
    private static final int StatusCode = 401;

    public UnauthoriedException(String message) {
        super(message);
    }

    public String toJson() {
        return new Gson().toJson(Map.of("message", getMessage(), "status", StatusCode));
    }

    public int getStatusCode() {
        return StatusCode;
    }
}
