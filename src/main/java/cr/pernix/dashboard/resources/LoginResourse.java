package cr.pernix.dashboard.resources;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import cr.pernix.dashboard.models.User;
import cr.pernix.dashboard.objects.LoginObject;
import cr.pernix.dashboard.services.LoginService;

@Path("login")
public class LoginResourse {

    @POST
    public Response getUser(LoginObject loginObject) throws Exception {
        User user = LoginService.getInstance().login(loginObject);
        return Response.ok(user).header("Access-Control-Allow-Origin", "*").build();
    }
}
