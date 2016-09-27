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

import cr.pernix.dashboard.models.Company;
import cr.pernix.dashboard.services.CompanyService;

@Path("company")
public class CompanyResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get() throws Exception {
        List<Company> result = CompanyService.getInstance().get();
        GenericEntity<List<Company>> list = new GenericEntity<List<Company>>(result) {
        };
        return Response.ok(list).header("Access-Control-Allow-Origin", "*").build();
    }

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") int id) throws Exception {
        Company company = CompanyService.getInstance().get(id);
        return Response.ok(company).header("Access-Control-Allow-Origin", "*").build();
    }

    @POST
    public Response create(Company company) throws Exception {
        CompanyService.getInstance().save(company);
        return Response.ok().header("Access-Control-Allow-Origin", "*").build();
    }

    @PUT
    public Response update(Company company) throws Exception {
        CompanyService.getInstance().save(company);
        return Response.ok().header("Access-Control-Allow-Origin", "*").build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") int id) throws Exception {
        CompanyService.getInstance().delete(id);
        return Response.ok().header("Access-Control-Allow-Origin", "*").build();
    }
}
