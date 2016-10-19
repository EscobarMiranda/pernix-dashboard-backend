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
import cr.pernix.dashboard.models.Answer;
import cr.pernix.dashboard.models.Manager;
import cr.pernix.dashboard.models.Metric;
import cr.pernix.dashboard.models.Survey;
import cr.pernix.dashboard.models.User;
import cr.pernix.dashboard.models.UserType;
import cr.pernix.dashboard.services.CompanyService;
import cr.pernix.dashboard.services.AnswerService;
import cr.pernix.dashboard.services.ManagerService;
import cr.pernix.dashboard.services.MetricService;
import cr.pernix.dashboard.services.SurveyService;
import cr.pernix.dashboard.services.UserService;
import cr.pernix.dashboard.services.UserTypeService;

public class AnswerResourceTest extends JerseyTest {

    private final int VALUE = 5;
    private final Date TIMESTAMP = new Date(2016, 9, 26);
    private final String NAME = "Pernix";
    private final String LASTNAME = "Solutions";
    private final String EMAIL = "kescobar@pernix-solutions.com";
    private final boolean ACTIVE = true;

    private UserTypeService userTypeService = UserTypeService.getInstance();
    private UserService userService = UserService.getInstance();
    private ManagerService managerService = ManagerService.getInstance();
    private CompanyService companyService = CompanyService.getInstance();
    private AnswerService customerSatisfactionService = AnswerService.getInstance();
    private MetricService metricService = MetricService.getInstance();
    private SurveyService surveyService = SurveyService.getInstance();

    private Metric insertTestMetric() {
        Metric metric = new Metric();
        metric.setName("Test metric");
        metric.setDescription("Description");
        metric.setSurvey(insertTestSurvey());
        metric.setActive(true);
        metricService.save(metric);
        return metric;
    }

    private Survey insertTestSurvey() {
        Survey testSurvey = new Survey();
        testSurvey.setName(NAME);
        testSurvey.setActive(ACTIVE);
        surveyService.save(testSurvey);
        return testSurvey;
    }

    private List<Answer> insertTestCostumerSatisfaction(int count) {
        List<Answer> testCostumerSatisfactionList = new ArrayList<>();
        for (; count > 0; count--) {
            Answer testCostumerSatisfaction = new Answer();
            testCostumerSatisfaction.setValue(VALUE);
            testCostumerSatisfaction.setTimestamp(TIMESTAMP);
            testCostumerSatisfaction.setMetric(insertTestMetric());
            testCostumerSatisfaction.setUser(insertTestUser());
            customerSatisfactionService.save(testCostumerSatisfaction);
            testCostumerSatisfactionList.add(testCostumerSatisfaction);
        }
        return testCostumerSatisfactionList;
    }

    private User insertTestUser() {
        User testUser = new User();
        testUser.setActive(true);
        testUser.setName(NAME);
        testUser.setLastname(LASTNAME);
        testUser.setEmail(EMAIL);
        testUser.setUserType(insertTestUserType());
        testUser.setManager(insertTestManager());
        userService.save(testUser);
        return testUser;
    }

    public UserType insertTestUserType() {
        UserType testUserType = new UserType();
        testUserType.setName(NAME);
        userTypeService.save(testUserType);
        return testUserType;
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
        company.setName(NAME);
        companyService.save(company);
        return company;
    }

    private void deleteAll(List<Answer> costumerSatisfactionList) {
        for (Answer customerSatisfaction : costumerSatisfactionList) {
            customerSatisfactionService.delete(customerSatisfaction.getId());
            metricService.delete(customerSatisfaction.getMetric().getId());
            userService.delete(customerSatisfaction.getUser().getId());
            userTypeService.delete(customerSatisfaction.getUser().getUserType().getId());
            managerService.delete(customerSatisfaction.getUser().getManager().getId());
            companyService.delete(customerSatisfaction.getUser().getManager().getCompany().getId());

        }
    }

    @Override
    protected Application configure() {
        return new ResourceConfig(AnswerResource.class);
    }

    @Test
    public void testGetAll() {
        List<Answer> testCostumerSatisfaction = insertTestCostumerSatisfaction(2);
        Assert.assertTrue(testCostumerSatisfaction.size() == 2);
        final Response response = target().path("answer").request().get();
        Assert.assertEquals(200, response.getStatus());
        List<Answer> costumerSatisfactionList = response
                .readEntity(new GenericType<List<Answer>>() {
                });
        Assert.assertEquals(testCostumerSatisfaction.size(), costumerSatisfactionList.size());
        deleteAll(costumerSatisfactionList);
    }

    @Test
    public void testGetGeneral() {
        List<Answer> testCostumerSatisfaction = insertTestCostumerSatisfaction(2);
        Assert.assertTrue(testCostumerSatisfaction.size() == 2);
        final Response response = target().path("answer/general").request().get();
        Assert.assertEquals(200, response.getStatus());
        List<Answer> costumerSatisfactionList = response
                .readEntity(new GenericType<List<Answer>>() {
                });
        Assert.assertTrue(costumerSatisfactionList.size() > 0);
        deleteAll(testCostumerSatisfaction);
    }

    @Test
    public void testGetCompany() {
        List<Answer> testCostumerSatisfaction = insertTestCostumerSatisfaction(2);
        Assert.assertTrue(testCostumerSatisfaction.size() == 2);
        int companyId = testCostumerSatisfaction.get(0).getUser().getManager().getCompany().getId();
        final Response response = target().path("answer/company/" + companyId).request().get();
        Assert.assertEquals(200, response.getStatus());
        List<Answer> costumerSatisfactionList = response
                .readEntity(new GenericType<List<Answer>>() {
                });
        Assert.assertTrue(costumerSatisfactionList.size() > 0);
        deleteAll(testCostumerSatisfaction);
    }

    @Test
    public void testGetUser() {
        List<Answer> testCostumerSatisfaction = insertTestCostumerSatisfaction(2);
        Assert.assertTrue(testCostumerSatisfaction.size() == 2);
        int userId = testCostumerSatisfaction.get(0).getUser().getId();
        final Response response = target().path("answer/user/" + userId).request().get();
        Assert.assertEquals(200, response.getStatus());
        List<Answer> costumerSatisfactionList = response
                .readEntity(new GenericType<List<Answer>>() {
                });
        Assert.assertTrue(costumerSatisfactionList.size() > 0);
        deleteAll(testCostumerSatisfaction);
    }

    @Test
    public void testGet() {
        List<Answer> testCostumerSatisfaction = insertTestCostumerSatisfaction(1);
        Assert.assertTrue(testCostumerSatisfaction.size() > 0);
        Answer toCompare = testCostumerSatisfaction.get(0);
        String path = "answer/%d";
        final Response response = target().path(String.format(path, toCompare.getId())).request().get();
        Assert.assertEquals(200, response.getStatus());
        Answer customerSatisfaction = response.readEntity(Answer.class);
        Assert.assertTrue("Object do not match", customerSatisfaction.equals(toCompare));
        customerSatisfactionService.delete(toCompare.getId());
        deleteAll(testCostumerSatisfaction);
    }

    @Test
    public void testDelete() {
        List<Answer> testCostumerSatisfaction = insertTestCostumerSatisfaction(1);
        Assert.assertTrue(testCostumerSatisfaction.size() > 0);
        Answer toDelete = testCostumerSatisfaction.get(0);
        String path = "answer/%d";
        final Response response = target().path(String.format(path, toDelete.getId())).request().delete();
        Assert.assertEquals(200, response.getStatus());
        deleteAll(testCostumerSatisfaction);
    }

    @Test
    public void testEdit() {
        List<Answer> testCostumerSatisfaction = insertTestCostumerSatisfaction(1);
        Assert.assertTrue(testCostumerSatisfaction.size() > 0);
        Answer toUpdate = testCostumerSatisfaction.get(0);
        toUpdate.setValue(1);
        final Response response = target().path("answer").request().put(Entity.json(toUpdate),
                Response.class);
        Assert.assertEquals(200, response.getStatus());
        Answer modifiedCostumerSatisfaction = AnswerService.getInstance()
                .get(toUpdate.getId());
        Assert.assertTrue("Not the same object", modifiedCostumerSatisfaction.equals(toUpdate));
        Assert.assertNotEquals("Name not modified", VALUE, modifiedCostumerSatisfaction.getValue());
        customerSatisfactionService.delete(toUpdate.getId());
        deleteAll(testCostumerSatisfaction);
    }
}
