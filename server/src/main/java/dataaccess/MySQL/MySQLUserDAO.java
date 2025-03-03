package dataaccess.MySQL;

import dataaccess.UserDAO;
import dataaccess.exceptions.DataAccessException;
import models.UserData;

public class MySQLUserDAO implements UserDAO {
    @Override
    public UserData getUser(String username) {
        return null;
    }

    @Override
    public void createUser(UserData user) throws DataAccessException {

    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public void clear() {

    }
}

