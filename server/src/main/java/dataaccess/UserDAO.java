package dataaccess;

import dataaccess.exceptions.DataAccessException;
import models.UserData;
import org.mindrot.jbcrypt.BCrypt;

import javax.xml.crypto.Data;

public interface UserDAO {
    UserData getUser(String username);
    void createUser(UserData user) throws DataAccessException;
    int size();
    void clear();

    static String hashUserPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public boolean verifyUser(String username, String password) throws DataAccessException;

    public String getHashedPassword(String username) throws DataAccessException;
}
