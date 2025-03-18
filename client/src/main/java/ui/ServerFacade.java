package ui;

import com.google.gson.Gson;
import models.requests.*;
import models.results.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;
import java.util.Map;


public class ServerFacade {
    private final String serverUrl;

    public ServerFacade(int port) {
        this.serverUrl = "http://localhost:" + port;
    }

    public void clear() throws Exception {
        var path = serverUrl + "/db";
        makeRequest("DELETE", path, null, null);
    }

    public RegisterResult register(RegisterRequest request) throws Exception {
        var path = serverUrl + "/user";
        var body = Map.of(
                "username", request.username(),
                "password", request.password(),
                "email", request.email()
        );

        return makeRequest("POST", path, body, RegisterResult.class);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws Exception {
        try {
            HttpURLConnection http = (HttpURLConnection) (new URI(path)).toURL().openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeBody(request, http);

            http.connect();

            if (http.getResponseCode() == 200) {
                return readBody(http, responseClass);
            }

            throw new Exception(http.getResponseMessage());
        } catch (Exception ex) {
            throw new Exception(ex);
        }
    }

    private static void writeBody(Object body, HttpURLConnection http) throws IOException {
        http.addRequestProperty("Content-Type", "application/json");
        try (var outputStream = http.getOutputStream()) {
            var jsonBody = new Gson().toJson(body);
            outputStream.write(jsonBody.getBytes());
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }



    private boolean isSuccessful(int status) { return status / 100 == 2; }

}
