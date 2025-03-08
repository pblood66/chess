package dataaccess.MySQL;

import com.google.gson.Gson;
import dataaccess.DatabaseManager;
import dataaccess.UserDAO;
import dataaccess.exceptions.BadRequestException;
import dataaccess.exceptions.DataAccessException;
import models.UserData;
import org.mindrot.jbcrypt.BCrypt;

import javax.xml.crypto.Data;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class MySQLUserDAO implements UserDAO {
    public MySQLUserDAO() {
        try {
            configureDatabase();
        } catch (DataAccessException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public UserData getUser(String username) {
        var statement = "SELECT * FROM users WHERE username = ?";
        try (var result = executeQuery(statement, username)) {
            result.next();
            String password = result.getString("password");
            String email = result.getString("email");

            return new UserData(username, password, email);
        } catch (DataAccessException | SQLException ex) {
            return null;
        }
    }

    @Override
    public void createUser(UserData user) throws DataAccessException {
        var statement = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        executeUpdate(statement, user.username(), user.password(), user.email());
        System.out.println("Created user: " + user.username());
    }

    private ResultSet executeQuery(String statement, Object... params) throws DataAccessException {
        try (var connection = DatabaseManager.getConnection()) {
            try (var ps = connection.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (int i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String) {
                        ps.setString(i + 1, (String) param);
                    }
                }

                return ps.executeQuery();
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    private void executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var connection = DatabaseManager.getConnection()) {
            try (var ps = connection.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (int i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String) ps.setString(i + 1, (String) param);
                }

                ps.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new BadRequestException("Could not execute SQL statement: " + statement);
        }
    }

    @Override
    public int size() {
        var statement = "SELECT COUNT(*) FROM users";

        try (var result = executeQuery(statement)) {
            result.next();
            return result.getInt(1);
        } catch (DataAccessException | SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public void clear() {
        try {
            var statement = "TRUNCATE TABLE users";
            executeUpdate(statement);
        } catch (DataAccessException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    private String hashUserPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    private boolean verifyUser(String username, String password) throws DataAccessException {
        String hashedPassword = getHashedPassword(username);
        return BCrypt.checkpw(password, hashedPassword);
    }

    private String getHashedPassword(String username) throws DataAccessException {
        return null;
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS users (
            username VARCHAR(255) NOT NULL,
            password VARCHAR(255) NOT NULL,
            email VARCHAR(255),
            PRIMARY KEY (username)
            )
            """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Could not configure database: " + ex.getMessage());
        }
    }
}

