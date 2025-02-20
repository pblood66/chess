package dataaccess;

import models.UserData;

public interface UserDAO {
    UserData getUser(String username);
    void createUser(UserData user) throws DataAccessException;
    void clear();
}
