package dataaccess.exceptions;

import com.google.gson.Gson;

import java.util.Map;

/**
 * Indicates there was an error connecting to the database
 */
public class DataAccessException extends Exception{
    private static final int StatusCode = 400;

    public DataAccessException(String message) {
        super(message);
    }

    public String toJson() {
        return new Gson().toJson(Map.of("message", getMessage(), "status", StatusCode));
    }

    public int getStatusCode() {
        return StatusCode;
    }
}
