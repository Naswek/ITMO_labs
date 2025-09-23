package org.example.connection;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.cords.CordRequest;
import org.example.cords.CordResponse;
import org.example.cords.CordValidation;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The type Object converter.
 */
public class ObjectConverter {

    private static final Logger log = LogManager.getLogger(ObjectConverter.class);
    private final CordValidation validator;
    private final Gson gson = new Gson();

    /**
     * Instantiates a new Object converter.
     */
    public ObjectConverter() {
        this.validator = new CordValidation();
    }

    /**
     * Serialize response datagram packet.
     *
     * @return the datagram packet
     * @throws ClassCastException the class cast exception
     */
    public String serializeResponse(String query) throws ClassCastException {

        log.info(query);

        if (query.isBlank()) {

            return """
                    Status: 400 Bad Request\r
                    Content-Type: application/json\r
                    \r
                    { "hit": false, "error": "Invalid or empty request body" }""";

        } else {

            final Map<String, String> params = Arrays.stream(query.split("&"))
                    .map(s -> s.split("=", 2))
                    .filter(pair -> pair.length == 2)
                    .collect(Collectors.toMap(
                            pair -> pair[0],
                            pair -> URLDecoder.decode(pair[1], StandardCharsets.UTF_8)
                    ));

            final String rawJson = params.get("data");

            if (rawJson == null) {
                return """
                        Content-type: application/json\r
                        \r
                        { "error": "missing data param" }""";
            }

            final CordRequest[] requests = gson.fromJson(rawJson, CordRequest[].class);

            final CordResponse[] responses = Arrays.stream(requests)
                    .map(request -> new CordResponse(
                            request.x(),
                            request.y(),
                            request.r(),
                            validator.validate(request.x(), request.y(), request.r())
                    ))
                    .toArray(CordResponse[]::new);


            return "Content-type: application/json\r\n\r\n" + gson.toJson(responses);

        }
    }
}
