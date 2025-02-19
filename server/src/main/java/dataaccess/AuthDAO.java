package dataaccess;

import models.AuthData;

public interface AuthDAO {
    AuthData getAuth(String authToken) throws DataAccessException;
    void createAuth(AuthData authData) throws DataAccessException;
    void deleteAuth(AuthData authData) throws DataAccessException;
    void clear();
}
