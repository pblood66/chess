package client;

import models.requests.*;
import models.results.*;
import org.junit.jupiter.api.*;
import server.Server;
import ui.ServerFacade;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    private static final String username = "tester";
    private static final String password = "password";
    private static final String email = "test@test.com";

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);

        var serverUrl = String.format("http://localhost:%d", port);
        facade = new ServerFacade(serverUrl);

    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    void clearDatabase() throws Exception {
        facade.clear();
    }


    @Test
    public void registerPositiveTest() throws Exception {
        RegisterResult result = facade.register(username, password, email);

        System.out.println(result.toString());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(username, result.username());
        Assertions.assertNotNull(result.authToken());
    }

    @Test
    public void registerNegativeTest() throws Exception {
        facade.register(username, password, email);

        Assertions.assertThrows(Exception.class, () -> facade.register(username, password, email));

    }

    @Test
    public void loginPositiveTest() throws Exception {
        facade.register(username, password, email);

        LoginResult result = facade.login(username, password);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(username, result.username());
        Assertions.assertNotNull(result.authToken());
    }

    @Test
    public void loginNegativeTest() throws Exception {
        Assertions.assertThrows(Exception.class, () -> facade.login(username, password));
    }

    @Test
    public void logoutPositiveTest() throws Exception {
        RegisterResult registerResult = facade.register(username, password, email);

        String authToken = registerResult.authToken();

        LogoutRequest request = new LogoutRequest(authToken);

        Assertions.assertDoesNotThrow(() -> facade.logout(request));
    }

    @Test
    public void logoutNegativeTest() throws Exception {
        LogoutRequest request = new LogoutRequest("bad auth token");

        Assertions.assertThrows(Exception.class, () -> facade.logout(request));
    }

    @Test
    public void createGamePositiveTest() throws Exception {
        RegisterResult registerResult = facade.register(username, password, email);

        String authToken = registerResult.authToken();

        CreateGameRequest request = new CreateGameRequest(authToken, "testGame");
        CreateGameResult result = facade.createGame(request);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.gameID());
    }

    @Test
    public void createGameNegativeTest() throws Exception {
        CreateGameRequest request = new CreateGameRequest("Bad Auth Token", "testGame");

        Assertions.assertThrows(Exception.class, () -> facade.createGame(request));
    }

    @Test
    public void listGamesPositiveTest() throws Exception {
        RegisterResult registerResult = facade.register(username, password, email);

        String authToken = registerResult.authToken();

        CreateGameRequest createGame = new CreateGameRequest(authToken, "testGame");
        facade.createGame(createGame);

        ListGamesRequest request = new ListGamesRequest(authToken);
        ListGamesResult result = facade.listGames(request);
    }

    @Test
    public void listGamesNegativeTest() throws Exception {
        ListGamesRequest request = new ListGamesRequest("bad auth token");
        Assertions.assertThrows(Exception.class, () -> facade.listGames(request));
    }

    @Test
    public void joinGamePositiveTest() throws Exception {
            RegisterResult registerResult = facade.register(username, password, email);

        String authToken = registerResult.authToken();

        CreateGameRequest createGame = new CreateGameRequest(authToken, "testGame");
        CreateGameResult createGameResult = facade.createGame(createGame);
        int gameId = createGameResult.gameID();

        JoinGameRequest request = new JoinGameRequest(authToken, "BLACK", gameId);
        Assertions.assertDoesNotThrow(() -> facade.joinGame(request));
    }

    @Test
    public void joinGameNegativeTest() throws Exception {
        JoinGameRequest request = new JoinGameRequest("Bad Auth Token", "testGame", 1);
        Assertions.assertThrows(Exception.class, () -> facade.joinGame(request));
    }
}
