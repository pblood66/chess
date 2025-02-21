package dataaccess;

import dataaccess.exceptions.DataAccessException;
import models.UserData;

public interface UserDAO {
    UserData getUser(String username);
    void createUser(UserData user) throws DataAccessException;
    int size();
    void clear();
}
