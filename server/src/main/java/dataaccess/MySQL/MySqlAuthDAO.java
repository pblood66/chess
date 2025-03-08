package dataaccess.MySQL;

import dataaccess.AuthDAO;
import dataaccess.DatabaseManager;
import dataaccess.exceptions.BadRequestException;
import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.UnauthoriedException;
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
        var statement = "SELECT * FROM auth WHERE authToken = ?";

        try (var result = DatabaseManager.executeQuery(statement, authToken)) {
            result.next();
            String username = result.getString("username");

            return new AuthData(authToken, username);
        } catch (SQLException ex) {
            throw new UnauthoriedException("Error: unauthorized");
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
        } catch (DataAccessException ex) {
            throw new BadRequestException("Error deleting auth token");
        }
    }

    @Override
    public int size() {
        var statement = "SELECT COUNT(*) FROM auth";

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
            var statement = "TRUNCATE TABLE auth";
            DatabaseManager.executeUpdate(statement);
        } catch (DataAccessException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }


}
