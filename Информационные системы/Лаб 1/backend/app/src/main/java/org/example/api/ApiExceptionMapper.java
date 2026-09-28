package org.example.api;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.bind.JsonbException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.OptimisticLockException;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.stream.Collectors;
import org.example.utils.repository.StaleObjectException;

@ApplicationScoped
@Provider
public class ApiExceptionMapper implements ExceptionMapper<RuntimeException> {

    @Override
    public Response toResponse(RuntimeException exception) {
        if (exception instanceof WebApplicationException webException) {
            Response response = webException.getResponse();
            if (response.hasEntity()) {
                return response;
            }
            String message = response.getStatus() == 400
                    ? "Некорректный JSON или параметр запроса"
                    : response.getStatusInfo().getReasonPhrase();
            return Response.status(response.getStatus())
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(ApiError.of(message, "HTTP_" + response.getStatus()))
                    .build();
        }

        Response.Status status;
        String code;
        String message;
        if (isOptimisticConflict(exception)) {
            status = Response.Status.CONFLICT;
            code = "STALE_VERSION";
            message = "Работа уже изменена другим пользователем. Загрузите актуальные данные и повторите действие";
        } else if (exception instanceof EntityNotFoundException) {
            status = Response.Status.NOT_FOUND;
            code = "NOT_FOUND";
            message = exception.getMessage();
        } else if (exception instanceof ConstraintViolationException violations) {
            status = Response.Status.BAD_REQUEST;
            code = "INVALID_INPUT";
            message = violations.getConstraintViolations().stream()
                    .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                    .collect(Collectors.joining("; "));
        } else if (exception instanceof IllegalArgumentException || exception instanceof JsonbException) {
            status = Response.Status.BAD_REQUEST;
            code = "INVALID_INPUT";
            message = exception instanceof JsonbException
                    ? "Проверьте формат полей запроса: числа в JSON записываются через точку, дата должна быть корректной"
                    : exception.getMessage();
        } else {
            status = Response.Status.INTERNAL_SERVER_ERROR;
            code = "INTERNAL_ERROR";
            message = "Внутренняя ошибка сервера";
        }

        return Response.status(status)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(ApiError.of(message, code))
                .build();
    }

    private static boolean isOptimisticConflict(Throwable error) {
        while (error != null) {
            if (error instanceof StaleObjectException || error instanceof OptimisticLockException) {
                return true;
            }
            error = error.getCause();
        }
        return false;
    }
}
