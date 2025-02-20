package dataaccess;

import dataaccess.exceptions.DataAccessException;
import models.UserData;

import java.util.HashSet;

public class MemoryUserDAO implements UserDAO {
    HashSet<UserData> users;

    public MemoryUserDAO() {
        users = new HashSet<>();
    }

    @Override
    public UserData getUser(String username) {
        for (UserData user : users) {
            if (user.username().equals(username)) {
                return user;
            }
        }

        return null;
    }

    @Override
    public UserData authenticateUser(String username, String password) throws DataAccessException {
        return null;
    }

    @Override
    public void createUser(UserData user) throws DataAccessException {
        users.add(user);
    }


    @Override
    public void clear() {
        users.clear();
    }
}
