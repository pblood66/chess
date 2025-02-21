package service;

import dataaccess.*;
import dataaccess.exceptions.DataAccessException;
import dataaccess.memory.MemoryAuthDAO;
import dataaccess.memory.MemoryUserDAO;
import org.junit.jupiter.api.*;
import service.requests.LoginRequest;
import service.requests.LogoutRequest;
import service.requests.RegisterRequest;
import service.results.LoginResult;
import service.results.RegisterResult;

public class UserServiceTests {
    private static UserService userService;
    private static UserDAO userDAO;
    private static AuthDAO authDAO;
    private static String token;

    @BeforeAll
    static void setUp() {
        userDAO = new MemoryUserDAO();
        authDAO = new MemoryAuthDAO();
        userService = new UserService(userDAO, authDAO);
    }

    @Test
    @Order(1)
    public void RegisterPositiveTest() throws DataAccessException {
        RegisterRequest request = new RegisterRequest("pblood66", "test", "test");
        RegisterResult result = userService.register(request);

        token = result.authToken();

        Assertions.assertNotNull(result);
        Assertions.assertEquals("pblood66", result.username());
    }

    @Test
    @Order(2)
    public void RegisterNegativeTest()  {
        RegisterRequest request = new RegisterRequest("pblood66", "", "");
        Assertions.assertThrows(DataAccessException.class, () -> userService.register(request));
    }

    @Test
    @Order(3)
    public void LoginPositiveTest() throws DataAccessException {
        LoginRequest login = new LoginRequest("pblood66", "test");
        LoginResult result = userService.login(login);

        userService.logout(new LogoutRequest(result.authToken()));

        Assertions.assertEquals("pblood66", result.username());
        Assertions.assertNotNull(result.authToken());
    }

    @Test
    @Order(4)
    public void LoginNegativeTest() {
        LoginRequest login = new LoginRequest("pblood66", "error");
        Assertions.assertThrows(DataAccessException.class, () -> userService.login(login));
    }

    @Test
    @Order(5)
    void LogoutNegativeTest() {
        LogoutRequest request = new LogoutRequest("error");
        Assertions.assertThrows(DataAccessException.class, () -> userService.logout(request));
    }

    @Test
    @Order(6)
    void LogoutPositiveTest() throws DataAccessException {
        LogoutRequest request = new LogoutRequest(token);

        Assertions.assertEquals(1, authDAO.size());

        userService.logout(request);

        Assertions.assertEquals(0, authDAO.size());
    }
}
