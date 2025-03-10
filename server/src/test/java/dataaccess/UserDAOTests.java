package dataaccess;

import dataaccess.MySQL.MySqlUserDAO;
import dataaccess.exceptions.DataAccessException;
import dataaccess.memory.MemoryUserDAO;
import models.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class UserDAOTests {
    private UserDAO getDataAccess(Class<? extends UserDAO> databaseClass) {
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
    void createUserPositiveTest(Class<? extends UserDAO> databaseClass) {
        UserDAO db = getDataAccess(databaseClass);

        UserData user = new UserData("testUser", "testPassword", "testUser");

        Assertions.assertDoesNotThrow(() -> db.createUser(user));

        Assertions.assertEquals(1, db.size());
    }

    @ParameterizedTest
    @ValueSource(classes = {MySqlUserDAO.class})
    void createUserNegativeTest(Class<? extends UserDAO> databaseClass) throws DataAccessException {
        UserDAO db = getDataAccess(databaseClass);

        UserData user = new UserData("testUser", "testPassword", "testUser");
        db.createUser(user);

        Assertions.assertThrows(DataAccessException.class, () -> db.createUser(user));
        Assertions.assertEquals(1, db.size());
    }

    @ParameterizedTest
    @ValueSource(classes = {MySqlUserDAO.class, MemoryUserDAO.class})
    void getUserPositiveTest(Class<? extends UserDAO> databaseClass) throws DataAccessException {
        UserDAO db = getDataAccess(databaseClass);
        db.clear();

        UserData user = new UserData("testUser", "testPassword", "testUser");
        db.createUser(user);

        UserData result = db.getUser("testUser");

        Assertions.assertEquals(user.username(), result.username());
        Assertions.assertTrue(db.verifyUser("testUser", "testPassword"));
        Assertions.assertEquals(user.email(), result.email());
    }

    @ParameterizedTest
    @ValueSource(classes = {MySqlUserDAO.class, MemoryUserDAO.class})
    void getUserNegativeTest(Class<? extends UserDAO> databaseClass) throws DataAccessException {
        UserDAO db = getDataAccess(databaseClass);

        UserData user = new UserData("testUser", "testPassword", "testUser");
        db.createUser(user);

        UserData result = db.getUser("badUser");

        Assertions.assertNull(result);
    }

    @ParameterizedTest
    @ValueSource(classes = {MySqlUserDAO.class, MemoryUserDAO.class})
    void clearDatabaseTest(Class<? extends UserDAO> databaseClass) throws DataAccessException {
        UserDAO db = getDataAccess(databaseClass);

        UserData user = new UserData("testUser", "testPassword", "testUser");
        db.createUser(user);

        user = new UserData("testUser2", "testPassword2", "testUser2");
        db.createUser(user);
        Assertions.assertEquals(2, db.size());

        db.clear();
        Assertions.assertEquals(0, db.size());
    }
}
