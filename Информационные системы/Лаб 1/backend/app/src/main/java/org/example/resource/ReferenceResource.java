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
import java.util.List;
import org.example.api.ApiError;
import org.example.entity.Discipline;
import org.example.entity.Person;
import org.example.utils.cdi.ReferenceService;

@RequestScoped
@Path("/references")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ReferenceResource {

    @Inject
    private ReferenceService service;

    @Context
    private HttpServletRequest request;

    @GET
    @Path("/disciplines")
    public List<Discipline> disciplines() {
        requireUser();
        return service.disciplines();
    }

    @GET
    @Path("/disciplines/{id}")
    public Discipline discipline(@PathParam("id") long id) {
        requireUser();
        return service.discipline(id);
    }

    @POST
    @Path("/disciplines")
    public Response addDiscipline(Discipline value, @Context UriInfo uriInfo) {
        requireUser();
        Discipline created = service.add(value);
        return Response.created(uriInfo.getAbsolutePathBuilder().path(created.getId().toString()).build())
                .entity(created).build();
    }

    @PUT
    @Path("/disciplines/{id}")
    public Discipline updateDiscipline(@PathParam("id") long id, Discipline value) {
        requireUser();
        return service.updateDiscipline(id, value);
    }

    @DELETE
    @Path("/disciplines/{id}")
    public Response deleteDiscipline(@PathParam("id") long id, @QueryParam("replacementId") Long replacementId) {
        requireUser();
        service.deleteDiscipline(id, replacementId);
        return Response.noContent().build();
    }

    @GET
    @Path("/people")
    public List<Person> people() {
        requireUser();
        return service.people();
    }

    @GET
    @Path("/people/{id}")
    public Person person(@PathParam("id") long id) {
        requireUser();
        return service.person(id);
    }

    @POST
    @Path("/people")
    public Response addPerson(Person value, @Context UriInfo uriInfo) {
        requireUser();
        Person created = service.add(value);
        return Response.created(uriInfo.getAbsolutePathBuilder().path(created.getId().toString()).build())
                .entity(created).build();
    }

    @PUT
    @Path("/people/{id}")
    public Person updatePerson(@PathParam("id") long id, Person value) {
        requireUser();
        return service.updatePerson(id, value);
    }

    @DELETE
    @Path("/people/{id}")
    public Response deletePerson(@PathParam("id") long id, @QueryParam("replacementId") Long replacementId) {
        requireUser();
        service.deletePerson(id, replacementId);
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
