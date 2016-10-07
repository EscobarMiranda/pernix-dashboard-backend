package cr.pernix.dashboard.resources;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Assert;
import org.junit.Test;

import cr.pernix.dashboard.models.UserType;
import cr.pernix.dashboard.services.UserTypeService;

public class UserTypeResourceTest extends JerseyTest {

    private final String TYPE = "Administrador";

    private UserTypeService userTypeService = UserTypeService.getInstance();

    private List<UserType> insertTestUserTypes(int count) {
        List<UserType> testUserTypes = new ArrayList<>();
        for (; count > 0; count--) {
            UserType testUserType = new UserType();
            testUserType.setName(TYPE);
            userTypeService.save(testUserType);
            testUserTypes.add(testUserType);
        }
        return testUserTypes;
    }

    @Override
    protected Application configure() {
        return new ResourceConfig(UserTypeResource.class);
    }

    @Test
    public void testGetAll() {
        List<UserType> testUserType = insertTestUserTypes(5);
        Assert.assertTrue(testUserType.size() == 5);
        final Response response = target().path("userType").request().get();
        Assert.assertEquals(200, response.getStatus());
        List<UserType> userTypeList = response.readEntity(new GenericType<List<UserType>>() {
        });
        Assert.assertEquals(testUserType.size()+1, userTypeList.size());
        for (UserType userType : userTypeList) {
            userTypeService.delete(userType.getId());
        }
    }

    @Test
    public void testGet() {
        List<UserType> testUserType = insertTestUserTypes(1);
        Assert.assertTrue(testUserType.size() > 0);
        UserType toCompare = testUserType.get(0);
        String path = "userType/%d";
        final Response response = target().path(String.format(path, toCompare.getId())).request().get();
        Assert.assertEquals(200, response.getStatus());
        UserType userType = response.readEntity(UserType.class);
        Assert.assertTrue("Object do not match", userType.equals(toCompare));
        userTypeService.delete(toCompare.getId());
    }

    @Test
    public void testGetByName() {
        List<UserType> testUserType = insertTestUserTypes(1);
        Assert.assertTrue(testUserType.size() > 0);
        UserType toCompare = testUserType.get(0);
        final Response response = target().path(String.format("userType/byName/" + toCompare.getName())).request().get();
        Assert.assertEquals(200, response.getStatus());
        UserType userType = response.readEntity(UserType.class);
        Assert.assertTrue("Object do not match", userType.equals(toCompare));
        userTypeService.delete(toCompare.getId());
    }
    
    @Test
    public void testDelete() {
        List<UserType> testUserType = insertTestUserTypes(1);
        Assert.assertTrue(testUserType.size() > 0);
        UserType toDelete = testUserType.get(0);
        String path = "userType/%d";
        final Response response = target().path(String.format(path, toDelete.getId())).request().delete();
        Assert.assertEquals(200, response.getStatus());
        Assert.assertNull("Object still persist", userTypeService.get(toDelete.getId()));
    }

    @Test
    public void testEdit() {
        List<UserType> testUserType = insertTestUserTypes(1);
        Assert.assertTrue(testUserType.size() > 0);
        UserType toUpdate = testUserType.get(0);
        toUpdate.setName("Modified Type");
        final Response response = target().path("userType").request().put(Entity.json(toUpdate), Response.class);
        Assert.assertEquals(200, response.getStatus());
        UserType modifiedUserType = UserTypeService.getInstance().get(toUpdate.getId());
        Assert.assertTrue("Not the same object", modifiedUserType.equals(toUpdate));
        Assert.assertNotEquals("Name not modified", TYPE, modifiedUserType.getName());
        userTypeService.delete(toUpdate.getId());
    }
}
