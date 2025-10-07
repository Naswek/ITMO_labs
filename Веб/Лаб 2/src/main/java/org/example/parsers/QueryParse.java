package org.example.parsers;

import com.google.gson.Gson;
import org.example.cords.CordRequest;
import org.example.exceptions.EmptyRequestException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class QueryParse {

    private final Gson gson;

    public QueryParse() {
        this.gson = new Gson();
    }

    public CordRequest[] parseRequest(String query) throws ClassCastException {

        if (query == null || query.isEmpty()) {
            return new CordRequest[0];
        }

        final String json = getJson(query);
        if (json == null || json.isEmpty()) {
            return new CordRequest[0];
        }

        return gson.fromJson(json, CordRequest[].class);
    }

    private String getJson(String query) {
        final Map<String, String> params = Arrays.stream(query.split("&"))
                .map(s -> s.split("=", 2))
                .filter(pair -> pair.length == 2)
                .collect(Collectors.toMap(
                        pair -> pair[0],
                        pair -> URLDecoder.decode(pair[1], StandardCharsets.UTF_8)
                ));

        return params.get("data");
    }
}
