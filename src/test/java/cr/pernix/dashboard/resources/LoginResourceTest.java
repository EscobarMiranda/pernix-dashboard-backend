package cr.pernix.dashboard.resources;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Application;
import org.glassfish.jersey.server.ResourceConfig;
import org.jboss.logging.Logger;
import org.junit.Test;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.test.JerseyTest;
import org.junit.Assert;

import cr.pernix.dashboard.models.Company;
import cr.pernix.dashboard.models.Manager;
import cr.pernix.dashboard.models.User;
import cr.pernix.dashboard.models.UserType;
import cr.pernix.dashboard.objects.LoginObject;
import cr.pernix.dashboard.services.CompanyService;
import cr.pernix.dashboard.services.ManagerService;
import cr.pernix.dashboard.services.UserService;
import cr.pernix.dashboard.services.UserTypeService;

public class LoginResourceTest extends JerseyTest {

    private static Logger LOG = Logger.getLogger(UserResourceTest.class);

    private final String NAME = "Kevin";
    private final String LASTNAME = "Escobar Miranda";
    private final String EMAIL = "kescobar@pernix.cr";
    private final String PASSWORD = "password";
    private final boolean ACTIVE = true;
    private final String USERTYPENAME = "admintest";

    private UserService userService = UserService.getInstance();
    private UserTypeService userTypeService = UserTypeService.getInstance();
    private ManagerService managerService = ManagerService.getInstance();
    private CompanyService companyService = CompanyService.getInstance();

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
        testUser.setPassword(PASSWORD);
        testUser.setActive(ACTIVE);
        testUser.setUserType(insertTestUserType());
        testUser.setManager(insertTestManager());
        userService.save(testUser);
        return testUser;
    }

    private Manager insertTestManager() {
        Manager testManager = new Manager();
        testManager.setName(NAME);
        testManager.setLastname(LASTNAME);
        testManager.setEmail(EMAIL);
        testManager.setCompany(insertTestCompany());
        managerService.save(testManager);
        return testManager;
    }

    private Company insertTestCompany() {
        Company company = new Company();
        company.setName("Test metric");
        companyService.save(company);
        return company;
    }

    private void deleteAll(User user) {
        userService.delete(user.getId());
        userTypeService.delete(user.getUserType().getId());
        managerService.delete(user.getManager().getId());
        companyService.delete(user.getManager().getCompany().getId());
    }

    @Override
    protected Application configure() {
        return new ResourceConfig(LoginResourse.class);
    }

    @Test
    public void testLogin() {
        User testUser = insertTestUser();
        LoginObject login = new LoginObject();
        login.setEmail(EMAIL);
        login.setPassword(PASSWORD);
        String path = "login";
        final Response response = target().path(String.format(path)).request().post(Entity.json(login));
        Assert.assertEquals(200, response.getStatus());
        User user = response.readEntity(User.class);
        Assert.assertNotNull(user);
    }

}
