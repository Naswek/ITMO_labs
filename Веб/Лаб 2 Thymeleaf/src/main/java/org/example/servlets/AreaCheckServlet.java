package org.example.servlets;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.cords.Result;
import org.example.parsers.QueryParse;
import org.example.cords.CordRequest;
import org.example.cords.CordResponse;
import org.example.exceptions.EmptyRequestException;

import jakarta.servlet.http.HttpServlet;
import org.example.storage.AppStorage;

import java.io.Serial;
import java.util.Arrays;

//@WebServlet("/checker")
public class AreaCheckServlet extends HttpServlet {

    private transient QueryParse queryParse;
    private transient Gson gson;
    private transient AppStorage appStorage;
    @Serial
    private static final long serialVersionUID = 1L;
    private static final Logger log = LogManager.getLogger(AreaCheckServlet.class);

    public String parseParams(String query, String parameter) throws EmptyRequestException{

        if (parameter.equalsIgnoreCase("true")) {
            appStorage.clearResults();
            log.info("Storage cleared");
        }

        final CordRequest[] responses = queryParse.parseRequest(query);
        return checkParams(responses);
    }

    private String checkParams(CordRequest[] requests) {


        if (requests.length == 0) {
            return gson.toJson(appStorage.getResults());
        }

        final CordResponse[] responses = Arrays.stream(requests)
                .map(request -> {
                    boolean isValid = validate(request.x(), request.y(), request.r());
                    final CordResponse response = new CordResponse(
                            request.x(),
                            request.y(),
                            request.r(),
                            isValid,
                            request.realTime(),
                            request.timeSend()
                    );

                    final Result result = new Result(
                            request.x(),
                            request.y(),
                            request.r(),
                            isValid,
                            request.realTime(),
                            request.timeSend()
                    );
                    appStorage.addResult(result);

                    log.info("Added point to storage: {}", result);

                    return response;
                })
                .toArray(CordResponse[]::new);

        return gson.toJson(appStorage.getResults());
    }

    @Override
    public void init() {
        this.queryParse = new QueryParse();
        this.gson = new Gson();

        AppStorage storage = (AppStorage) getServletContext().getAttribute("appStorage");
        if (storage == null) {
            storage = new AppStorage();
            getServletContext().setAttribute("appStorage", storage);
        }
        this.appStorage = storage;
    }

    private boolean validate(double x, double y, double r) {
        return isHitSquare(x, y, r) || isHitCircle(x, y, r) || isHitTriangle(x, y, r);
    }

    private boolean isHitSquare(double x, double y, double r) {
        return  (x >= -r && x <= 0) && (y >= 0 && y <= r/2);
    }

    private boolean isHitTriangle(double x, double y, double r) {
        return (0 <= x && x <= r/2) && (-r/2 <= y && y <= 0) && (y >= 2*x - r);
    }

    private boolean isHitCircle(double x, double y, double r) {
        return (x >= 0 && y >= 0 && (x * x + y * y <= r * r));
    }
}

