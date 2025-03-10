package dataaccess.memory;

import dataaccess.UserDAO;
import dataaccess.exceptions.DataAccessException;
import models.UserData;
import org.mindrot.jbcrypt.BCrypt;

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
        String hashedPassword = UserDAO.hashUserPassword(user.password());

        UserData updatedUser = new UserData(user.username(), hashedPassword, user.email());

        users.add(updatedUser);
    }

    @Override
    public boolean verifyUser(String username, String password) throws DataAccessException {
        String hashedPassword = getHashedPassword(username);
        return BCrypt.checkpw(password, hashedPassword);
    }

    @Override
    public int size() {
        return users.size();
    }

    @Override
    public void clear() {
        users.clear();
    }

    @Override
    public String getHashedPassword(String username) throws DataAccessException {
        for (UserData user : users) {
            if (user.username().equals(username)) {
                return user.password();
            }
        }

        throw new DataAccessException("Error Could Not Find user");
    }
}
