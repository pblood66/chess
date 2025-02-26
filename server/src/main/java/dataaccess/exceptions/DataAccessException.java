package dataaccess.exceptions;

import com.google.gson.Gson;

import java.util.Map;

/**
 * Indicates there was an error connecting to the database
 */
public class DataAccessException extends Exception{
    private static final int STATUS_CODE = 400;

    public DataAccessException(String message) {
        super(message);
    }

    public String toJson() {
        return new Gson().toJson(Map.of("message", getMessage(), "status", STATUS_CODE));
    }

    public int getStatusCode() {
        return STATUS_CODE;
    }
}
