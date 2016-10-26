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

import cr.pernix.dashboard.models.Project;
import cr.pernix.dashboard.services.ProjectService;

@Path("project")
public class ProjectResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get() throws Exception {
        List<Project> result = ProjectService.getInstance().get();
        GenericEntity<List<Project>> list = new GenericEntity<List<Project>>(result) {
        };
        return Response.ok(list).header("Access-Control-Allow-Origin", "*").build();
    }
    
    @GET
    @Path("/byUser/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByUser(@PathParam("id") int id) throws Exception {
        List<Project> result = ProjectService.getInstance().getByUser(id);
        GenericEntity<List<Project>> list = new GenericEntity<List<Project>>(result) {
        };
        return Response.ok(list).header("Access-Control-Allow-Origin", "*").build();
    }

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") int id) throws Exception {
        Project project = ProjectService.getInstance().get(id);
        return Response.ok(project).header("Access-Control-Allow-Origin", "*").build();
    }

    @POST
    public Response create(Project project) throws Exception {
        ProjectService.getInstance().save(project);
        return Response.ok().header("Access-Control-Allow-Origin", "*").build();
    }

    @PUT
    public Response update(Project project) throws Exception {
        ProjectService.getInstance().save(project);
        return Response.ok().header("Access-Control-Allow-Origin", "*").build();
    }
    
    @PUT
    @Path("/changeState")
    public Response changeState(Project project) throws Exception {
        ProjectService.getInstance().changeState(project);
        return Response.ok().header("Access-Control-Allow-Origin", "*").build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") int id) throws Exception {
        ProjectService.getInstance().delete(id);
        return Response.ok().header("Access-Control-Allow-Origin", "*").build();
    }
}
