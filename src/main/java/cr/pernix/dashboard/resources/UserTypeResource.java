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

import cr.pernix.dashboard.models.UserType;
import cr.pernix.dashboard.services.UserTypeService;

@Path("userType")
public class UserTypeResource {
	
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get() throws Exception {
        List<UserType> result = UserTypeService.getInstance().get();
        GenericEntity<List<UserType>> list = new GenericEntity<List<UserType>>(result) {
        };
        return Response.ok(list).header("Access-Control-Allow-Origin", "*").build();
    }

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") int id) throws Exception {
        UserType userType = UserTypeService.getInstance().get(id);
        return Response.ok(userType).header("Access-Control-Allow-Origin", "*").build();
    }

    @GET
    @Path("byName/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("name") String name) throws Exception {
        UserType userType = UserTypeService.getInstance().getByName(name);
        return Response.ok(userType).header("Access-Control-Allow-Origin", "*").build();
    }
    
    @POST
    public Response create(UserType userType) throws Exception {
        UserTypeService.getInstance().save(userType);
        return Response.ok().header("Access-Control-Allow-Origin", "*").build();
    }

    @PUT
    public Response update(UserType userType) throws Exception {
        UserTypeService.getInstance().save(userType);
        return Response.ok().header("Access-Control-Allow-Origin", "*").build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") int id) throws Exception {
        UserTypeService.getInstance().delete(id);
        return Response.ok().header("Access-Control-Allow-Origin", "*").build();
    }
}
