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
}
