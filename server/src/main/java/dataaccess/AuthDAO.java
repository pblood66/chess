package dataaccess;

import dataaccess.exceptions.DataAccessException;
import models.AuthData;

public interface AuthDAO {
    AuthData getAuth(String authToken) throws DataAccessException;
    void createAuth(AuthData authData) throws DataAccessException;
    void deleteAuth(String authToken) throws DataAccessException;
    int size();
    void clear();
}
