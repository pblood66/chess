package dataaccess;

import dataaccess.MySQL.MySqlAuthDAO;
import dataaccess.MySQL.MySqlUserDAO;
import dataaccess.exceptions.DataAccessException;
import dataaccess.memory.MemoryAuthDAO;
import dataaccess.memory.MemoryUserDAO;
import models.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class userDAOTests {
    private UserDAO getDataAccess(Class<? extends UserDAO> databaseClass) throws DataAccessException {
        UserDAO db;
        if (databaseClass.equals(MySqlUserDAO.class)) {
            db = new MySqlUserDAO();
        } else {
            db = new MemoryUserDAO();
        }
        db.clear();
        return db;
    }


    @ParameterizedTest
    @ValueSource(classes = {MySqlUserDAO.class, MemoryUserDAO.class})
    void createUserPositiveTest(Class<? extends UserDAO> databaseClass) throws DataAccessException {
        UserDAO db = getDataAccess(databaseClass);

        UserData user = new UserData("testUser", "testPassword", "testUser");

        Assertions.assertDoesNotThrow(() -> db.createUser(user));

        Assertions.assertEquals(1, db.size());
    }

    @ParameterizedTest
    @ValueSource(classes = {MySqlUserDAO.class, MemoryUserDAO.class})
    void createUserNegativeTest(Class<? extends UserDAO> databaseClass) throws DataAccessException {

    }



}
