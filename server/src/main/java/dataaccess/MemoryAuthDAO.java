package dataaccess;

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

        throw new DataAccessException("No such auth token");
    }

    @Override
    public void createAuth(AuthData authData) throws DataAccessException {
        database.add(authData);
    }

    @Override
    public void deleteAuth(String AuthToken) throws DataAccessException {
        for (AuthData authData : database) {
            if (authData.authToken().equals(AuthToken)) {
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
