package cr.pernix.dashboard.resources;

import java.util.ArrayList;
import java.util.Date;
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
import cr.pernix.dashboard.models.CustomerSatisfaction;
import cr.pernix.dashboard.models.Manager;
import cr.pernix.dashboard.models.Metric;
import cr.pernix.dashboard.services.CompanyService;
import cr.pernix.dashboard.services.CustomerSatisfactionService;
import cr.pernix.dashboard.services.ManagerService;
import cr.pernix.dashboard.services.MetricService;

public class CustomerSatisfactionResourceTest extends JerseyTest {

    private final int VALUE = 5;
    private final Date TIMESTAMP = new Date(2016, 9, 26);
    private final String NAME = "Pernix";
    private final String LASTNAME = "Solutions";
    private final String EMAIL = "kescobar@pernix-solutions.com";

    private ManagerService managerService = ManagerService.getInstance();
    private CompanyService companyService = CompanyService.getInstance();
    private CustomerSatisfactionService customerSatisfactionService = CustomerSatisfactionService.getInstance();
    private MetricService metricService = MetricService.getInstance();

    private Metric insertTestMetric() {
        Metric metric = new Metric();
        metric.setName("Test metric");
        metric.setDescription("Description");
        metric.setActive(true);
        metricService.save(metric);
        return metric;
    }

    private List<CustomerSatisfaction> insertTestCostumerSatisfaction(int count) {
        List<CustomerSatisfaction> testCostumerSatisfactionList = new ArrayList<>();
        for (; count > 0; count--) {
            CustomerSatisfaction testCostumerSatisfaction = new CustomerSatisfaction();
            testCostumerSatisfaction.setValue(VALUE);
            testCostumerSatisfaction.setTimestamp(TIMESTAMP);
            testCostumerSatisfaction.setMetric(insertTestMetric());
            testCostumerSatisfaction.setManager(insertTestManager());
            customerSatisfactionService.save(testCostumerSatisfaction);
            testCostumerSatisfactionList.add(testCostumerSatisfaction);
        }
        return testCostumerSatisfactionList;
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

    private void deleteAll(List<CustomerSatisfaction> costumerSatisfactionList) {
        for (CustomerSatisfaction customerSatisfaction : costumerSatisfactionList) {
            customerSatisfactionService.delete(customerSatisfaction.getId());
            metricService.delete(customerSatisfaction.getMetric().getId());
            managerService.delete(customerSatisfaction.getManager().getId());
            companyService.delete(customerSatisfaction.getManager().getCompany().getId());
        }
    }

    @Override
    protected Application configure() {
        return new ResourceConfig(CustomerSatisfactionResource.class);
    }

    @Test
    public void testGetAll() {
        List<CustomerSatisfaction> testCostumerSatisfaction = insertTestCostumerSatisfaction(5);
        Assert.assertTrue(testCostumerSatisfaction.size() == 5);
        final Response response = target().path("customerSatisfaction").request().get();
        Assert.assertEquals(200, response.getStatus());
        List<CustomerSatisfaction> costumerSatisfactionList = response
                .readEntity(new GenericType<List<CustomerSatisfaction>>() {
                });
        Assert.assertEquals(testCostumerSatisfaction.size(), costumerSatisfactionList.size());
        deleteAll(costumerSatisfactionList);
    }

    @Test
    public void testGet() {
        List<CustomerSatisfaction> testCostumerSatisfaction = insertTestCostumerSatisfaction(1);
        Assert.assertTrue(testCostumerSatisfaction.size() > 0);
        CustomerSatisfaction toCompare = testCostumerSatisfaction.get(0);
        String path = "customerSatisfaction/%d";
        final Response response = target().path(String.format(path, toCompare.getId())).request().get();
        Assert.assertEquals(200, response.getStatus());
        CustomerSatisfaction customerSatisfaction = response.readEntity(CustomerSatisfaction.class);
        Assert.assertTrue("Object do not match", customerSatisfaction.equals(toCompare));
        customerSatisfactionService.delete(toCompare.getId());
        deleteAll(testCostumerSatisfaction);
    }

    @Test
    public void testDelete() {
        List<CustomerSatisfaction> testCostumerSatisfaction = insertTestCostumerSatisfaction(1);
        Assert.assertTrue(testCostumerSatisfaction.size() > 0);
        CustomerSatisfaction toDelete = testCostumerSatisfaction.get(0);
        String path = "customerSatisfaction/%d";
        final Response response = target().path(String.format(path, toDelete.getId())).request().delete();
        Assert.assertEquals(200, response.getStatus());
        deleteAll(testCostumerSatisfaction);
    }

    @Test
    public void testEdit() {
        List<CustomerSatisfaction> testCostumerSatisfaction = insertTestCostumerSatisfaction(1);
        Assert.assertTrue(testCostumerSatisfaction.size() > 0);
        CustomerSatisfaction toUpdate = testCostumerSatisfaction.get(0);
        toUpdate.setValue(1);
        final Response response = target().path("customerSatisfaction").request().put(Entity.json(toUpdate),
                Response.class);
        Assert.assertEquals(200, response.getStatus());
        CustomerSatisfaction modifiedCostumerSatisfaction = CustomerSatisfactionService.getInstance()
                .get(toUpdate.getId());
        Assert.assertTrue("Not the same object", modifiedCostumerSatisfaction.equals(toUpdate));
        Assert.assertNotEquals("Name not modified", VALUE, modifiedCostumerSatisfaction.getValue());
        customerSatisfactionService.delete(toUpdate.getId());
        deleteAll(testCostumerSatisfaction);
    }
}
