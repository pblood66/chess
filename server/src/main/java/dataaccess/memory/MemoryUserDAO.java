package dataaccess.memory;

import dataaccess.UserDAO;
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
    public void createUser(UserData user) {
        users.add(user);
    }

    @Override
    public int size() {
        return users.size();
    }

    @Override
    public void clear() {
        users.clear();
    }
}
