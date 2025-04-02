package dataaccess.mysql;

import dataaccess.AuthDAO;
import dataaccess.DatabaseManager;
import dataaccess.exceptions.BadRequestException;
import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.UnauthorizedException;
import models.AuthData;

import java.sql.SQLException;

public class MySqlAuthDAO implements AuthDAO {
    public MySqlAuthDAO() {
        try {
            String[] tableStatement = {
                    """
            CREATE TABLE IF NOT EXISTS auth (
            authToken varchar(250) NOT NULL,
            username varchar(20) NOT NULL,
            PRIMARY KEY (authToken)
            );
            """
            };

            DatabaseManager.configureDatabase(tableStatement);
        } catch (DataAccessException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        String statement = "SELECT username FROM auth WHERE authToken = ?";

        try (var connection = DatabaseManager.getConnection();
             var result = DatabaseManager.executeQuery(connection, statement, authToken)) {

            if (result.next()) { // Ensure a row exists
                String username = result.getString("username");
                return new AuthData(authToken, username);
            } else {
                throw new UnauthorizedException("Error: Unauthorized");
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Error: " + ex.getMessage());
        }
    }

    @Override
    public void createAuth(AuthData authData) throws DataAccessException {
        String authToken = authData.authToken();
        String username = authData.username();

        var statement = "INSERT INTO auth (authToken, username) VALUES (?, ?)";

        try {
            DatabaseManager.executeUpdate(statement, authToken, username);
            System.out.println("Created auth token: " + authToken + ", username: " + username);
        } catch (DataAccessException ex) {
            throw new BadRequestException("Error creating auth data");
        }
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        var statement = "DELETE FROM auth WHERE authToken = ?";

        try {
            DatabaseManager.executeUpdate(statement, authToken);
            System.out.println("Deleted auth token: " + authToken);
        } catch (DataAccessException ex) {
            throw new BadRequestException("Error deleting auth token");
        }
    }

    @Override
    public int size() {
        var statement = "SELECT COUNT(*) FROM auth";

        try (var connection = DatabaseManager.getConnection()) {
            try (var result = DatabaseManager.executeQuery(connection, statement)) {
                result.next();
                return result.getInt(1);
            }
        } catch (DataAccessException | SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public void clear() {
        try {
            var statement = "TRUNCATE TABLE auth";
            DatabaseManager.executeUpdate(statement);
        } catch (DataAccessException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }
}
