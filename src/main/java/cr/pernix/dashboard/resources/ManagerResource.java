package cr.pernix.dashboard.resources;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import cr.pernix.dashboard.models.Manager;
import cr.pernix.dashboard.services.ManagerService;

@Path("manager")
public class ManagerResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get() throws Exception {
        List<Manager> result = ManagerService.getInstance().get();
        GenericEntity<List<Manager>> list = new GenericEntity<List<Manager>>(result) {
        };
        return Response.ok(list).build();
    }

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") int id) throws Exception {
        Manager manager = ManagerService.getInstance().get(id);
        return Response.ok(manager).build();
    }

    @POST
    public Response create(Manager manager) throws Exception {
        ManagerService.getInstance().save(manager);
        return Response.ok().build();
    }

    @PUT
    public Response update(Manager manager) throws Exception {
        ManagerService.getInstance().save(manager);
        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") int id) throws Exception {
        ManagerService.getInstance().delete(id);
        return Response.ok().build();
    }
}
