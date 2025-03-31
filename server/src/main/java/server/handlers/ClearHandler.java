package server.handlers;


import service.ClearService;
import spark.Request;
import spark.Response;

public class ClearHandler {
    private final ClearService clearService;

    public ClearHandler(ClearService clearService) {
        this.clearService = clearService;
    }

    public Object clear(Request req, Response res) {
        clearService.clear();
        System.out.println("Database Cleared");

        res.status(200);

        return "{}";
    }

}
