package service;

import dataaccess.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserServiceTests {
    private UserService userService;
    private UserDAO userDAO;
    private AuthDAO authDAO;

    @BeforeEach
    void setUp() {
        userDAO = new MemoryUserDAO();
        authDAO = new MemoryAuthDAO();
        userService = new UserService(userDAO, authDAO);
    }

    @Test
    public void testRegisterPositiveTest() throws DataAccessException {
        RegisterRequest request = new RegisterRequest("pblood66", "test", "test");
        RegisterResult result = userService.register(request);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("pblood66", result.username());
    }

    @Test
    public void testRegisterNegativeTest() throws DataAccessException {
        RegisterRequest request = new RegisterRequest("pblood66", "", "");
        Assertions.assertThrows(DataAccessException.class, () -> userService.register(request));
    }

    @Test
    public void testLoginPositiveTest() throws DataAccessException {
        RegisterRequest register = new RegisterRequest("pblood66", "test", "test");
        userService.register(register);

        LoginRequest login = new LoginRequest("pblood66", "test");
        LoginResult result = userService.login(login);

        Assertions.assertEquals("pblood66", result.username());
        Assertions.assertNotNull(result.authToken());
    }

    @Test
    public void testLoginNegativeTest() throws DataAccessException {
        RegisterRequest register = new RegisterRequest("pblood66", "password", "test");
        userService.register(register);

        LoginRequest login = new LoginRequest("pblood66", "error");
        Assertions.assertThrows(DataAccessException.class, () -> userService.login(login));
    }
}
