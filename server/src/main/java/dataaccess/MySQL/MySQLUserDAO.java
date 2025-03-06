package dataaccess.MySQL;

import com.google.gson.Gson;
import dataaccess.DatabaseManager;
import dataaccess.UserDAO;
import dataaccess.exceptions.DataAccessException;
import models.UserData;

import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class MySQLUserDAO implements UserDAO {
    public MySQLUserDAO() throws DataAccessException, SQLException {
        configureDatabase();
    }

    @Override
    public UserData getUser(String username) {
        return null;
    }

    @Override
    public void createUser(UserData user) throws DataAccessException{

    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public void clear() {

    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS users (
            'id' int NOT NULL AUTO_INCREMENT,
            'username' varchar(255) NOT NULL,
            'password' varchar(255) NOT NULL,
            'email' varchar(255) NOT NULL,
            PRIMARY KEY ('id'),
            )
            """
    };

    private void configureDatabase() throws SQLException, DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        }
    }
}

