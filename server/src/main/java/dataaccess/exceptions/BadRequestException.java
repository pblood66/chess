package dataaccess.exceptions;

import com.google.gson.Gson;

import java.util.Map;

public class BadRequestException extends DataAccessException {
    private static final int statusCode = 400;

    public BadRequestException(String message) {
        super(message);
    }

    public String toJson() {
        return new Gson().toJson(Map.of("message", getMessage(), "status", statusCode));
    }

    public int getStatusCode() {
        return statusCode;
    }
}
