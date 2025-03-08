package dataaccess.MySQL;

import dataaccess.DatabaseManager;
import dataaccess.UserDAO;
import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.DuplicatedException;
import models.UserData;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.SQLException;

public class MySQLUserDAO implements UserDAO {
    public MySQLUserDAO() {
        try {
            String[] tableStatements = {
                    """
            CREATE TABLE IF NOT EXISTS users (
            username VARCHAR(20) NOT NULL,
            password VARCHAR(255) NOT NULL,
            email VARCHAR(255),
            PRIMARY KEY (username)
            )
            """
            };
            DatabaseManager.configureDatabase(tableStatements);
        } catch (DataAccessException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public UserData getUser(String username) {
        var statement = "SELECT * FROM users WHERE username = ?";
        try (var result = DatabaseManager.executeQuery(statement, username)) {
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
        try {
            DatabaseManager.executeUpdate(statement, user.username(), user.password(), user.email());
        } catch (DataAccessException ex) {
            throw new DuplicatedException("Error: already taken");
        }
        System.out.println("Created user: " + user.username());
    }


    @Override
    public int size() {
        var statement = "SELECT COUNT(*) FROM users";

        try (var result = DatabaseManager.executeQuery(statement)) {
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
            DatabaseManager.executeUpdate(statement);
        } catch (DataAccessException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    private String hashUserPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public boolean verifyUser(String username, String password) throws DataAccessException {
        String hashedPassword = getHashedPassword(username);
        return BCrypt.checkpw(password, hashedPassword);
    }

    private String getHashedPassword(String username) throws DataAccessException {
        var statement = "SELECT password FROM users WHERE username = ?";

        try (var result = DatabaseManager.executeQuery(statement, username)) {
            result.next();
            return result.getString(1);
        } catch (SQLException ex) {
            return null;
        }
    }

}

