package server;

import com.google.gson.Gson;
import service.UserService;
import models.requests.LoginRequest;
import models.requests.LogoutRequest;
import models.requests.RegisterRequest;
import models.results.LoginResult;
import models.results.RegisterResult;
import spark.Request;
import spark.Response;

public class UserHandler {
    private final UserService userService;

    public UserHandler(UserService userService) {
        this.userService = userService;
    }

    public Object register(Request req, Response res) throws Exception {
        Gson gson = new Gson();
        RegisterRequest registerRequest = gson.fromJson(req.body(), RegisterRequest.class);

        RegisterResult result = userService.register(registerRequest);
        String jsonResult = gson.toJson(result);

        res.status(200);
        return jsonResult;
    }

    public Object login(Request req, Response res) throws Exception {
        LoginRequest request = new Gson().fromJson(req.body(), LoginRequest.class);
        LoginResult result = userService.login(request);

        res.status(200);
        return new Gson().toJson(result);
    }

    public Object logout(Request req, Response res) throws Exception {
        LogoutRequest request = new LogoutRequest(req.headers("Authorization"));
        System.out.println(request.authToken());

        userService.logout(request);

        res.status(200);

        return "{}";
    }
}
