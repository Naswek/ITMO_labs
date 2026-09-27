package org.example.resource;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.sse.Sse;
import jakarta.ws.rs.sse.SseEventSink;
import org.example.api.ApiError;
import org.example.api.ChangeStream;
import org.example.entity.LabWork;
import org.example.utils.cdi.LabWorkService;

@RequestScoped
@Path("/labworks")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class LabWorkResource {

    @Inject
    private LabWorkService service;

    @Inject
    private ChangeStream changeStream;

    @Context
    private HttpServletRequest request;

    @GET
    public LabWorkService.Page list(@QueryParam("page") Integer page, @QueryParam("size") Integer size,
            @QueryParam("filterField") String filterField, @QueryParam("filterValue") String filterValue,
            @QueryParam("sortField") String sortField, @QueryParam("direction") String direction) {
        requireUser();
        int pageNumber = page == null ? 0 : page;
        int pageSize = size == null ? 20 : size;
        return service.list(pageNumber, pageSize, filterField, filterValue, sortField, direction);
    }

    @GET
    @Path("/events")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public void events(@Context SseEventSink sink, @Context Sse sse) {
        requireUser();
        changeStream.subscribe(sink, sse);
    }

    @GET
    @Path("/special/maximum-difficulty")
    public Response maximumDifficulty() {
        requireUser();
        return service.maximumDifficulty().map(value -> Response.ok(value).build())
                .orElseGet(() -> Response.noContent().build());
    }

    @GET
    @Path("/special/difficulty-counts")
    public Object difficultyCounts() {
        requireUser();
        return service.countByDifficulty();
    }

    @GET
    @Path("/special/name-contains")
    public Object nameContains(@QueryParam("substring") String substring) {
        requireUser();
        return service.nameContains(substring);
    }

    @POST
    @Path("/{id}/difficulty/increase")
    public LabWork increase(@PathParam("id") int id, @QueryParam("steps") int steps) {
        requireUser();
        return service.shiftDifficulty(id, steps, true);
    }

    @POST
    @Path("/{id}/difficulty/decrease")
    public LabWork decrease(@PathParam("id") int id, @QueryParam("steps") int steps) {
        requireUser();
        return service.shiftDifficulty(id, steps, false);
    }

    @GET
    @Path("/{id}")
    public LabWork info(@PathParam("id") int id) {
        requireUser();
        return service.info(id);
    }

    @POST
    public Response add(LabWork labWork, @Context UriInfo uriInfo) {
        requireUser();
        LabWork created = service.add(labWork);
        return Response.created(uriInfo.getAbsolutePathBuilder().path(Integer.toString(created.getId())).build())
                .entity(created)
                .build();
    }

    @PUT
    @Path("/{id}")
    public LabWork update(@PathParam("id") int id, LabWork changes) {
        requireUser();
        return service.update(id, changes);
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") int id) {
        requireUser();
        service.delete(id);
        return Response.noContent().build();
    }

    private void requireUser() {
        HttpSession session = request.getSession(false);
        if (session == null || !(session.getAttribute(UserResource.SESSION_USER_ID) instanceof Long)) {
            throw new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(ApiError.of("Для выполнения операции войдите в систему", "UNAUTHORIZED"))
                    .build());
        }
    }

}
