package org.example.resource;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.InputStream;
import java.util.List;
import org.example.api.ApiError;
import org.example.dto.ImportView;
import org.example.utils.cdi.ImportHistoryService;
import org.example.utils.cdi.ImportService;

@RequestScoped
@Path("/imports")
@Produces(MediaType.APPLICATION_JSON)
public class ImportResource {

    @Inject private ImportService service;
    @Inject private ImportHistoryService history;
    @Context private HttpServletRequest request;

    @POST
    @Consumes("text/csv")
    public Response upload(InputStream file) {
        ImportView operation = service.importFile(requireUserId(), file);
        return Response.status(Response.Status.CREATED).entity(operation).build();
    }

    @GET
    public List<ImportView> list() {
        return history.list(requireUserId());
    }

    private long requireUserId() {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute(UserResource.SESSION_USER_ID) instanceof Long id) return id;
        throw new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(ApiError.of("Для выполнения операции войдите в систему", "UNAUTHORIZED"))
                .build());
    }
}
