package org.example.exceptions;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.example.api.ApiError;
import org.example.exceptions.ApiException;

@Provider
public class ApiExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable e) {

        if (e instanceof ApiException ae) {
            return Response.status(ae.getStatus())
                    .type(MediaType.APPLICATION_JSON)
                    .entity(ApiError.of(ae.getMessage(), ae.getCode()))
                    .build();
        }

        e.printStackTrace(); //на всякий, но пока хз, мб потом уберу, мб нет
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .type(MediaType.APPLICATION_JSON)
                .entity(ApiError.of("Внутренняя ошибка сервера", "INTERNAL_ERROR"))
                .build();
    }
}
