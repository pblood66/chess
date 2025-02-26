package dataaccess.memory;

import dataaccess.AuthDAO;
import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.UnauthoriedException;
import models.AuthData;

import java.util.HashSet;

public class MemoryAuthDAO implements AuthDAO {
    private final HashSet<AuthData> database;
    public MemoryAuthDAO() {
        database = new HashSet<>();
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        for (AuthData authData : database) {
            if (authData.authToken().equals(authToken)) {
                return authData;
            }
        }

        throw new UnauthoriedException("Error: unauthorized");
    }

    @Override
    public void createAuth(AuthData authData) throws DataAccessException {
        database.add(authData);
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        for (AuthData authData : database) {
            if (authData.authToken().equals(authToken)) {
                database.remove(authData);
                return;
            }
        }
        throw new DataAccessException("No such auth token");
    }

    @Override
    public int size() {
        return database.size();
    }

    @Override
    public void clear() {
        database.clear();
    }
}
