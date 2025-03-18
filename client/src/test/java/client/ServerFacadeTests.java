package client;

import models.requests.RegisterRequest;
import models.results.RegisterResult;
import org.junit.jupiter.api.*;
import server.Server;
import ui.ServerFacade;

import java.sql.SQLException;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);

        facade = new ServerFacade(port);

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
    public void loginPositiveTest() throws Exception {
        RegisterResult result = facade.register(new RegisterRequest("pblood", "pblood", "pblood"));

        System.out.println(result.toString());

        Assertions.assertTrue(true);
    }

}
