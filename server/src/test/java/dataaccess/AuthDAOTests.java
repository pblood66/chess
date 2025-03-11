package dataaccess;

import dataaccess.mysql.MySqlAuthDAO;
import dataaccess.exceptions.DataAccessException;
import dataaccess.memory.MemoryAuthDAO;
import models.AuthData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class AuthDAOTests {
    private AuthDAO getDataAccess(Class<? extends AuthDAO> databaseClass) {
        AuthDAO db;
        if (databaseClass.equals(MySqlAuthDAO.class)) {
            db = new MySqlAuthDAO();
        } else {
            db = new MemoryAuthDAO();
        }
        db.clear();
        return db;
    }

    @ParameterizedTest
    @ValueSource(classes = {MySqlAuthDAO.class, MemoryAuthDAO.class})
    void addAuthPositiveTest(Class <? extends AuthDAO> dbClass) throws DataAccessException {
        AuthDAO db = getDataAccess(dbClass);

        AuthData auth = new AuthData("adminAuthToken", "adminTest");

        Assertions.assertDoesNotThrow(() -> db.createAuth(auth));
    }


    @ParameterizedTest
    @ValueSource(classes = {MySqlAuthDAO.class, MemoryAuthDAO.class})
    void getAuthPositiveTest(Class <? extends AuthDAO> dbClass) throws DataAccessException {
        AuthDAO db = getDataAccess(dbClass);

        AuthData auth = new AuthData("adminAuthToken", "adminTest");
        db.createAuth(auth);

        AuthData result = db.getAuth("adminAuthToken");
        Assertions.assertNotNull(result);
        Assertions.assertEquals(auth, result);


    }

    @ParameterizedTest
    @ValueSource(classes = {MySqlAuthDAO.class, MemoryAuthDAO.class})
    void getAuthNegativeTest(Class <? extends AuthDAO> dbClass) throws DataAccessException {
        AuthDAO db = getDataAccess(dbClass);

        AuthData auth = new AuthData("adminAuthToken", "adminTest");
        db.createAuth(auth);

        Assertions.assertThrows(DataAccessException.class, () -> db.getAuth("nothing"));
    }

    @ParameterizedTest
    @ValueSource(classes = {MySqlAuthDAO.class, MemoryAuthDAO.class})
    void deleteAuthPositiveTest(Class <? extends AuthDAO> dbClass) throws DataAccessException {
        AuthDAO db = getDataAccess(dbClass);

        AuthData auth = new AuthData("adminAuthToken", "adminTest");
        db.createAuth(auth);

        Assertions.assertEquals(1, db.size());
        db.deleteAuth("adminAuthToken");
        Assertions.assertEquals(0, db.size());
    }

    @ParameterizedTest
    @ValueSource(classes = {MySqlAuthDAO.class, MemoryAuthDAO.class})
    void deleteAuthNegativeTest(Class <? extends AuthDAO> dbClass) throws DataAccessException {
        AuthDAO db = getDataAccess(dbClass);

        AuthData auth = new AuthData("adminAuthToken", "adminTest");
        db.createAuth(auth);

        Assertions.assertDoesNotThrow(() -> db.deleteAuth("nothing"));
    }

    @ParameterizedTest
    @ValueSource(classes = {MySqlAuthDAO.class, MemoryAuthDAO.class})
    void clearAuthPositiveTest(Class <? extends AuthDAO> dbClass) throws DataAccessException {
        AuthDAO db = getDataAccess(dbClass);
        AuthData auth = new AuthData("adminAuthToken", "adminTest");
        db.createAuth(auth);
        auth = new AuthData("testToken", "testTest");
        db.createAuth(auth);

        Assertions.assertEquals(2, db.size());
        db.clear();
        Assertions.assertEquals(0, db.size());
    }

}
