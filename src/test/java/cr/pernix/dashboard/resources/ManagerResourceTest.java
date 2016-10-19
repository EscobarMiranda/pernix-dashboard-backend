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

import cr.pernix.dashboard.models.Company;
import cr.pernix.dashboard.models.Manager;
import cr.pernix.dashboard.services.CompanyService;
import cr.pernix.dashboard.services.ManagerService;

public class ManagerResourceTest extends JerseyTest {

    private final String NAME = "Pernix";
    private final String LASTNAME = "Solutions";
    private final String EMAIL = "kescobar@pernix-solutions.com";
    private final boolean ACTIVE = true;

    private ManagerService managerService = ManagerService.getInstance();
    private CompanyService companyService = CompanyService.getInstance();

    private Company insertTestCompany() {
        Company company = new Company();
        company.setName("Test metric");
        companyService.save(company);
        return company;
    }

    private List<Manager> insertTestManagers(int count) {
        List<Manager> testManagers = new ArrayList<>();
        for (; count > 0; count--) {
            Manager testManager = new Manager();
            testManager.setName(NAME);
            testManager.setLastname(LASTNAME);
            testManager.setEmail(EMAIL);
            testManager.setActive(ACTIVE);
            testManager.setCompany(insertTestCompany());
            managerService.save(testManager);
            testManagers.add(testManager);
        }
        return testManagers;
    }

    private void deleteAll(List<Manager> managerList) {
        for (Manager manager : managerList) {
            managerService.delete(manager.getId());
            companyService.delete(manager.getCompany().getId());
        }
    }

    @Override
    protected Application configure() {
        return new ResourceConfig(ManagerResource.class);
    }

    @Test
    public void testGetAll() {
        List<Manager> testManager = insertTestManagers(5);
        Assert.assertTrue(testManager.size() == 5);
        final Response response = target().path("manager").request().get();
        Assert.assertEquals(200, response.getStatus());
        List<Manager> managerList = response.readEntity(new GenericType<List<Manager>>() {
        });
        Assert.assertEquals(testManager.size(), managerList.size());
        deleteAll(managerList);
    }

    @Test
    public void testGet() {
        List<Manager> testManager = insertTestManagers(1);
        Assert.assertTrue(testManager.size() > 0);
        Manager toCompare = testManager.get(0);
        String path = "manager/%d";
        final Response response = target().path(String.format(path, toCompare.getId())).request().get();
        Assert.assertEquals(200, response.getStatus());
        Manager manager = response.readEntity(Manager.class);
        Assert.assertTrue("Object do not match", manager.equals(toCompare));
        deleteAll(testManager);
    }

    @Test
    public void testChangeState() {
        List<Manager> testManager = insertTestManagers(1);
        Assert.assertTrue(testManager.size() > 0);
        Manager toChangeState = testManager.get(0);
        Response response = target().path("manager/changeState").request().put(Entity.json(toChangeState), Response.class);
        Assert.assertEquals(200, response.getStatus());
        Manager manager = managerService.get(toChangeState.getId());
        Assert.assertFalse(manager.getActive());
        response = target().path("manager/changeState").request().put(Entity.json(manager), Response.class);
        Assert.assertEquals(200, response.getStatus());
        manager = managerService.get(manager.getId());
        Assert.assertTrue(manager.getActive());
        deleteAll(testManager);
    }

    @Test
    public void testEdit() {
        List<Manager> testManager = insertTestManagers(1);
        Assert.assertTrue(testManager.size() > 0);
        Manager toUpdate = testManager.get(0);
        toUpdate.setName("Modified Name");
        final Response response = target().path("manager").request().put(Entity.json(toUpdate), Response.class);
        Assert.assertEquals(200, response.getStatus());
        Manager modifiedManager = ManagerService.getInstance().get(toUpdate.getId());
        Assert.assertTrue("Not the same object", modifiedManager.equals(toUpdate));
        Assert.assertNotEquals("Name not modified", NAME, modifiedManager.getName());
        deleteAll(testManager);
    }
}
