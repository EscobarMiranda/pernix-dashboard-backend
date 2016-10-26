package cr.pernix.dashboard.resources;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.jboss.logging.Logger;
import org.junit.Assert;
import org.junit.Test;

import cr.pernix.dashboard.models.User;
import cr.pernix.dashboard.models.UserType;
import cr.pernix.dashboard.objects.LoginObject;
import cr.pernix.dashboard.services.UserService;
import cr.pernix.dashboard.services.UserTypeService;

public class LoginResourceTest extends JerseyTest {

    private static Logger LOG = Logger.getLogger(UserResourceTest.class);

    private final String NAME = "Luis";
    private final String LASTNAME = "Serrano";
    private final String EMAIL = "lserrano@pernix-solutions.com";
    private final boolean ACTIVE = true;
    private final String USERTYPENAME = "User";

    private UserService userService = UserService.getInstance();
    private UserTypeService userTypeService = UserTypeService.getInstance();


    private UserType insertTestUserType() {
        UserType testUserType = new UserType();
        testUserType.setName(USERTYPENAME);
        userTypeService.save(testUserType);

        return testUserType;
    }

    private User insertTestUser() {
        User testUser = new User();
        testUser.setName(NAME);
        testUser.setLastname(LASTNAME);
        testUser.setEmail(EMAIL);
        testUser.setActive(ACTIVE);
        testUser.setUserType(insertTestUserType());
        userService.save(testUser);
        return testUser;
    }

    private void deleteAll(User user) {
        userService.delete(user.getId());
        userTypeService.delete(user.getUserType().getId());
    }

    @Override
    protected Application configure() {
        return new ResourceConfig(LoginResource.class);
    }

    @Test
    public void testLogin() {
        User UserTmp = insertTestUser();
        LoginObject login = new LoginObject();
        login.setEmail(EMAIL);
        login.setName(NAME);
        login.setLastname(LASTNAME);
        login.setUser_type_id(UserTmp.getUserType().getId());
        final String path = "login";
        final Response response = target().path(String.format(path)).request().post(Entity.json(login));
        Assert.assertEquals(200, response.getStatus());
        User user = response.readEntity(User.class);
        Assert.assertNotNull(user);
        deleteAll(UserTmp);
    }

}
