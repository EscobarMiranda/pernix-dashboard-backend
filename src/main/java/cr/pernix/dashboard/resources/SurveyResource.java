package cr.pernix.dashboard.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import cr.pernix.dashboard.models.Survey;
import cr.pernix.dashboard.services.SurveyService;

@Path("survey")
public class SurveyResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get() throws Exception {
        List<Survey> result = SurveyService.getInstance().get();
        GenericEntity<List<Survey>> list = new GenericEntity<List<Survey>>(result) {
        };
        return Response.ok(list).header("Access-Control-Allow-Origin", "*").build();
    }

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") int id) throws Exception {
        Survey survey = SurveyService.getInstance().get(id);
        return Response.ok(survey).header("Access-Control-Allow-Origin", "*").build();
    }

    @POST
    public Response create(Survey survey) throws Exception {
        SurveyService.getInstance().save(survey);
        return Response.ok().header("Access-Control-Allow-Origin", "*").build();
    }

    @PUT
    public Response update(Survey survey) throws Exception {
        SurveyService.getInstance().save(survey);
        return Response.ok().header("Access-Control-Allow-Origin", "*").build();
    }

    @PUT
    @Path("changeState")
    public Response changeState(Survey survey) throws Exception {
        SurveyService.getInstance().changeState(survey);
        return Response.ok().header("Access-Control-Allow-Origin", "*").build();
    }
}
