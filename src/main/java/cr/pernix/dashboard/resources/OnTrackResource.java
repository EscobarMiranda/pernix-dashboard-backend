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

import cr.pernix.dashboard.models.OnTrack;
import cr.pernix.dashboard.services.OnTrackService;

@Path("onTrack")
public class OnTrackResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get() throws Exception {
        List<OnTrack> result = OnTrackService.getInstance().get();
        GenericEntity<List<OnTrack>> list = new GenericEntity<List<OnTrack>>(result) {
        };
        return Response.ok(list).header("Access-Control-Allow-Origin", "*").build();
    }

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") int id) throws Exception {
        OnTrack onTrack = OnTrackService.getInstance().get(id);
        return Response.ok(onTrack).header("Access-Control-Allow-Origin", "*").build();
    }

    @POST
    public Response create(OnTrack onTrack) throws Exception {
        OnTrackService.getInstance().save(onTrack);
        return Response.ok().header("Access-Control-Allow-Origin", "*").build();
    }

    @PUT
    public Response update(OnTrack onTrack) throws Exception {
        OnTrackService.getInstance().save(onTrack);
        return Response.ok().header("Access-Control-Allow-Origin", "*").build();
    }
}
