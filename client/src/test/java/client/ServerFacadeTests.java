package client;

import clients.ClientData;
import models.requests.*;
import models.results.*;
import org.junit.jupiter.api.*;
import server.Server;
import clients.ServerFacade;
import ui.Repl;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    private static final String USERNAME = "tester";
    private static final String PASSWORD = "password";
    private static final String EMAIL = "test@test.com";

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);

        var serverUrl = String.format("http://localhost:%d", port);
        facade = new ServerFacade(serverUrl, new ClientData(serverUrl));
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
        RegisterResult result = facade.register(USERNAME, PASSWORD, EMAIL);

        System.out.println(result.toString());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(USERNAME, result.username());
        Assertions.assertNotNull(result.authToken());
    }

    @Test
    public void registerNegativeTest() throws Exception {
        facade.register(USERNAME, PASSWORD, EMAIL);

        Assertions.assertThrows(Exception.class, () -> facade.register(USERNAME, PASSWORD, EMAIL));

    }

    @Test
    public void loginPositiveTest() throws Exception {
        facade.register(USERNAME, PASSWORD, EMAIL);

        LoginResult result = facade.login(USERNAME, PASSWORD);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(USERNAME, result.username());
        Assertions.assertNotNull(result.authToken());
    }

    @Test
    public void loginNegativeTest() throws Exception {
        Assertions.assertThrows(Exception.class, () -> facade.login(USERNAME, PASSWORD));
    }

    @Test
    public void logoutPositiveTest() throws Exception {
        RegisterResult registerResult = facade.register(USERNAME, PASSWORD, EMAIL);

        String authToken = registerResult.authToken();

        Assertions.assertDoesNotThrow(() -> facade.logout(authToken));
    }

    @Test
    public void logoutNegativeTest() throws Exception {
        Assertions.assertThrows(Exception.class, () -> facade.logout("bad auth token"));
    }

    @Test
    public void createGamePositiveTest() throws Exception {
        facade.clear();
        RegisterResult registerResult = facade.register(USERNAME, PASSWORD, EMAIL);

        String authToken = registerResult.authToken();

        CreateGameResult result = facade.createGame("testGame", authToken);
        Assertions.assertNotNull(result);
       Assertions.assertEquals(1, result.gameID());
    }

    @Test
    public void createGameNegativeTest() throws Exception {
        Assertions.assertThrows(Exception.class, () -> facade.createGame("testGame", "bad"));
    }

    @Test
    public void listGamesPositiveTest() throws Exception {
        RegisterResult registerResult = facade.register(USERNAME, PASSWORD, EMAIL);

        String authToken = registerResult.authToken();

        facade.createGame("testGame", authToken);

        ListGamesResult result = facade.listGames(authToken);
    }

    @Test
    public void listGamesNegativeTest() throws Exception {
        Assertions.assertThrows(Exception.class, () -> facade.listGames("bad auth token"));
    }

    @Test
    public void joinGamePositiveTest() throws Exception {
        RegisterResult registerResult = facade.register(USERNAME, PASSWORD, EMAIL);

        String authToken = registerResult.authToken();

        CreateGameResult createGameResult = facade.createGame("testGame", authToken);
        int gameId = createGameResult.gameID();

        Assertions.assertDoesNotThrow(() -> facade.joinGame("BLACK", gameId, authToken));
    }

    @Test
    public void joinGameNegativeTest() throws Exception {
        JoinGameRequest request = new JoinGameRequest("Bad Auth Token", "testGame", 1);
        Assertions.assertThrows(Exception.class, () -> facade.joinGame("request", 1, ""));
    }

    @Test
    public void resignGameTest() throws Exception {
        RegisterResult registerResult = facade.register(USERNAME, PASSWORD, EMAIL);
        String authToken = registerResult.authToken();
        CreateGameResult createGameResult = facade.createGame("testGame", authToken);
        int gameId = createGameResult.gameID();

        facade.joinGame("WHITE", gameId, authToken);
        
    }
}
