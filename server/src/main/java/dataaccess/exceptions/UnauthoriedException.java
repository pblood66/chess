package dataaccess.exceptions;

import com.google.gson.Gson;

import java.util.Map;

public class UnauthoriedException extends DataAccessException {
    private static final int STATUS_CODE = 401;

    public UnauthoriedException(String message) {
        super(message);
    }

    public String toJson() {
        return new Gson().toJson(Map.of("message", getMessage(), "status", STATUS_CODE));
    }

    public int getStatusCode() {
        return STATUS_CODE;
    }
}
