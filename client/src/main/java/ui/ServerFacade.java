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

    public void register(RegisterRequest request) throws Exception {
        URI uri = new URI(serverUrl + "/user");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("POST");

        http.setDoOutput(true);

        http.addRequestProperty("Content-Type", "application/json");
        var body = Map.of("username", request.username(), "password", request.password(), "email", request.email());

        try (var outputStream = http.getOutputStream()) {
            var jsonBody = new Gson().toJson(body);
            outputStream.write(jsonBody.getBytes());
        }

        http.connect();

        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            System.out.println(new Gson().fromJson(inputStreamReader, Map.class));
        }
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) {
        try {
            URL url = (new URI(serverUrl + path).toURL());
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeBody(request, http);
            http.connect();

            if (isSuccessful(http.getResponseCode())) {
                return readBody(http, responseClass);
            }
            return null;
        } catch (Exception ex) {
            System.out.println("Server Error: " + ex.getMessage());
            return null;
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

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }


    private boolean isSuccessful(int status) { return status / 100 == 2; }

}
