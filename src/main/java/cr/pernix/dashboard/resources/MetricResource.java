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

import cr.pernix.dashboard.models.Metric;
import cr.pernix.dashboard.services.MetricService;

@Path("metric")
public class MetricResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get() throws Exception {
        List<Metric> result = MetricService.getInstance().get();
        GenericEntity<List<Metric>> list = new GenericEntity<List<Metric>>(result) {
        };
        return Response.ok(list).header("Access-Control-Allow-Origin", "*").build();
    }
    
    @GET
    @Path("/bySurvey/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBySurvey(@PathParam("id") int id) throws Exception {
        List<Metric> result = MetricService.getInstance().getBySurvey(id);
        GenericEntity<List<Metric>> list = new GenericEntity<List<Metric>>(result) {
        };
        return Response.ok(list).header("Access-Control-Allow-Origin", "*").build();
    }

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") int id) throws Exception {
        Metric metric = MetricService.getInstance().get(id);
        return Response.ok(metric).header("Access-Control-Allow-Origin", "*").build();
    }

    @POST
    public Response create(Metric metric) throws Exception {
        MetricService.getInstance().save(metric);
        return Response.ok().header("Access-Control-Allow-Origin", "*").build();
    }

    @PUT
    public Response update(Metric metric) throws Exception {
        MetricService.getInstance().save(metric);
        return Response.ok().header("Access-Control-Allow-Origin", "*").build();
    }

    @PUT
    @Path("changeState")
    public Response changeState(Metric metric) throws Exception {
        MetricService.getInstance().changeState(metric);
        return Response.ok().header("Access-Control-Allow-Origin", "*").build();
    }
}
