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

import cr.pernix.dashboard.models.Answer;
import cr.pernix.dashboard.objects.AnswerObject;
import cr.pernix.dashboard.services.AnswerService;

@Path("answer")
public class AnswerResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get() throws Exception {
        List<Answer> result = AnswerService.getInstance().get();
        GenericEntity<List<Answer>> list = new GenericEntity<List<Answer>>(result) {
        };
        return Response.ok(list).header("Access-Control-Allow-Origin", "*").build();
    }

    @GET
    @Path("/general")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGeneral() throws Exception {
        List<AnswerObject> result = AnswerService.getInstance().getGeneral();
        GenericEntity<List<AnswerObject>> list = new GenericEntity<List<AnswerObject>>(
                result) {
        };
        return Response.ok(list).header("Access-Control-Allow-Origin", "*").build();
    }
    
    @GET
    @Path("/company/{companyId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCompany(@PathParam("companyId") int companyId) throws Exception {
        List<AnswerObject> result = AnswerService.getInstance().getCompany(companyId);
        GenericEntity<List<AnswerObject>> list = new GenericEntity<List<AnswerObject>>(
                result) {
        };
        return Response.ok(list).header("Access-Control-Allow-Origin", "*").build();
    }
    
    @GET
    @Path("/user/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("userId") int userId) throws Exception {
        List<AnswerObject> result = AnswerService.getInstance().getUser(userId);
        GenericEntity<List<AnswerObject>> list = new GenericEntity<List<AnswerObject>>(
                result) {
        };
        return Response.ok(list).header("Access-Control-Allow-Origin", "*").build();
    }

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") int id) throws Exception {
        Answer customerSatisfaction = AnswerService.getInstance().get(id);
        return Response.ok(customerSatisfaction).header("Access-Control-Allow-Origin", "*").build();
    }

    @POST
    public Response create(Answer customerSatisfaction) throws Exception {
        AnswerService.getInstance().save(customerSatisfaction);
        return Response.ok().header("Access-Control-Allow-Origin", "*").build();
    }

    @PUT
    public Response update(Answer customerSatisfaction) throws Exception {
        AnswerService.getInstance().save(customerSatisfaction);
        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") int id) throws Exception {
        AnswerService.getInstance().delete(id);
        return Response.ok().header("Access-Control-Allow-Origin", "*").build();
    }
}
