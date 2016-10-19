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

import cr.pernix.dashboard.models.User;
import cr.pernix.dashboard.services.UserService;

@Path("user")
public class UserResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsers() throws Exception {
        List<User> result = UserService.getInstance().get();
        GenericEntity<List<User>> list = new GenericEntity<List<User>>(result) {
        };
        return Response.ok(list).header("Access-Control-Allow-Origin", "*").build();
    }

    @GET
    @Path("/{id}")
    public Response getUser(@PathParam("id") int id) throws Exception {
        User user = UserService.getInstance().get(id);
        return Response.ok(user).header("Access-Control-Allow-Origin", "*").build();
    }

    @POST
    public Response createUser(User user) throws Exception {
        UserService.getInstance().save(user);
        return Response.ok().header("Access-Control-Allow-Origin", "*").build();
    }

    @PUT
    public Response update(User user) throws Exception {
        UserService.getInstance().save(user);
        return Response.ok().header("Access-Control-Allow-Origin", "*").build();
    }

    @PUT
    @Path("/changeState")
    public Response changeState(User user) throws Exception {
        UserService.getInstance().changeState(user);
        return Response.ok().header("Access-Control-Allow-Origin", "*").build();
    }
}
