package cr.pernix.dashboard.resources;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.jboss.logging.Logger;
import org.junit.Assert;
import org.junit.Test;

import cr.pernix.dashboard.models.Company;
import cr.pernix.dashboard.models.Manager;
import cr.pernix.dashboard.models.User;
import cr.pernix.dashboard.models.UserType;
import cr.pernix.dashboard.services.CompanyService;
import cr.pernix.dashboard.services.ManagerService;
import cr.pernix.dashboard.services.UserService;
import cr.pernix.dashboard.services.UserTypeService;

public class UserResourceTest extends JerseyTest {

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

    private List<User> insertTestUsers(int count) {
        List<User> testUsers = new ArrayList<User>();
        for (; count > 0; count--) {
            User testUser = new User();
            testUser.setName(NAME);
            testUser.setLastname(LASTNAME);
            testUser.setEmail(EMAIL);
            testUser.setPassword(PASSWORD);
            testUser.setActive(ACTIVE);
            testUser.setUserType(insertTestUserType());
            testUser.setManager(insertTestManager());
            userService.save(testUser);
            testUsers.add(testUser);
        }
        return testUsers;
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

    private void deleteAll(List<User> userList) {
        for (User user : userList) {
            userService.delete(user.getId());
            userTypeService.delete(user.getUserType().getId());
            managerService.delete(user.getManager().getId());
            companyService.delete(user.getManager().getCompany().getId());
        }
    }

    @Override
    protected Application configure() {
        return new ResourceConfig(UserResource.class);
    }

    @Test
    public void testGetAllUsers() {
        List<User> testUser = insertTestUsers(5);
        Assert.assertTrue(testUser.size() == 5);
        final Response response = target().path("user").request().get();
        Assert.assertEquals(200, response.getStatus());
        List<User> usersList = response.readEntity(new GenericType<List<User>>() {
        });
        Assert.assertEquals(testUser.size(), usersList.size());
        deleteAll(testUser);
    }

    @Test
    public void testGetSingleUser() {
        List<User> testUser = insertTestUsers(1);
        Assert.assertTrue(testUser.size() > 0);
        User toCompare = testUser.get(0);
        String path = "user/%d";
        final Response response = target().path(String.format(path, toCompare.getId())).request().get();
        Assert.assertEquals(200, response.getStatus());
        User user = response.readEntity(User.class);
        Assert.assertTrue("Object do not match", user.equals(toCompare));
        deleteAll(testUser);
    }

    @Test
    public void testDeleteUser() {
        List<User> testUser = insertTestUsers(1);
        Assert.assertTrue(testUser.size() > 0);
        User toDelete = testUser.get(0);
        String path = "user/%d";
        final Response response = target().path(String.format(path, toDelete.getId())).request().delete();
        Assert.assertEquals(200, response.getStatus());
        deleteAll(testUser);
    }

    @Test
    public void testEditUser() {
        List<User> testUser = insertTestUsers(1);
        Assert.assertTrue(testUser.size() > 0);
        User toUpdate = testUser.get(0);
        toUpdate.setName("Modified Name");
        final Response response = target().path("user").request().put(Entity.json(toUpdate), Response.class);
        Assert.assertEquals(200, response.getStatus());
        User modifiedUser = UserService.getInstance().get(toUpdate.getId());
        Assert.assertTrue("Not the same object", modifiedUser.equals(toUpdate));
        Assert.assertNotEquals("Name not modified", NAME, modifiedUser.getName());
        deleteAll(testUser);
    }
}
