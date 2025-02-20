package dataaccess;

import models.UserData;

public interface UserDAO {
    UserData getUser(String username);
    UserData authenticateUser(String username, String password) throws DataAccessException;
    void createUser(UserData user) throws DataAccessException;
    void clear();
}
