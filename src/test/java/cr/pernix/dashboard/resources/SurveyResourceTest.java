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

import cr.pernix.dashboard.models.Survey;
import cr.pernix.dashboard.services.SurveyService;

public class SurveyResourceTest extends JerseyTest {

    private final String NAME = "Survey test";
    private final boolean ACTIVE = true;

    private SurveyService surveyService = SurveyService.getInstance();

    private List<Survey> insertTestSurveys(int count) {
        List<Survey> testSurveys = new ArrayList<>();
        for (; count > 0; count--) {
            Survey testSurvey = new Survey();
            testSurvey.setName(NAME);
            testSurvey.setActive(ACTIVE);
            surveyService.save(testSurvey);
            testSurveys.add(testSurvey);
        }
        return testSurveys;
    }

    private void deleteAll(List<Survey> surveyList) {
        for (Survey survey : surveyList) {
            surveyService.delete(survey.getId());
        }
    }

    @Override
    protected Application configure() {
        return new ResourceConfig(SurveyResource.class);
    }

    @Test
    public void testGetAll() {
        List<Survey> testSurvey = insertTestSurveys(5);
        Assert.assertTrue(testSurvey.size() == 5);
        final Response response = target().path("survey").request().get();
        Assert.assertEquals(200, response.getStatus());
        List<Survey> metricsList = response.readEntity(new GenericType<List<Survey>>() {
        });
        Assert.assertEquals(testSurvey.size(), metricsList.size());
        deleteAll(metricsList);
    }

    @Test
    public void testGet() {
        List<Survey> testSurvey = insertTestSurveys(1);
        Assert.assertTrue(testSurvey.size() > 0);
        Survey toCompare = testSurvey.get(0);
        String path = "survey/%d";
        final Response response = target().path(String.format(path, toCompare.getId())).request().get();
        Assert.assertEquals(200, response.getStatus());
        Survey survey = response.readEntity(Survey.class);
        Assert.assertTrue("Object do not match", survey.equals(toCompare));
        deleteAll(testSurvey);
    }

    @Test
    public void testChangeState() {
        List<Survey> testSurvey = insertTestSurveys(1);
        Assert.assertTrue(testSurvey.size() > 0);
        Survey toChangeState = testSurvey.get(0);
        Response response = target().path("survey/changeState").request().put(Entity.json(toChangeState),
                Response.class);
        Assert.assertEquals(200, response.getStatus());
        Survey survey = surveyService.get(toChangeState.getId());
        Assert.assertFalse(survey.getActive());
        response = target().path("survey/changeState").request().put(Entity.json(survey), Response.class);
        Assert.assertEquals(200, response.getStatus());
        survey = surveyService.get(survey.getId());
        Assert.assertTrue(survey.getActive());
        deleteAll(testSurvey);
    }

    @Test
    public void testEdit() {
        List<Survey> testSurvey = insertTestSurveys(1);
        Assert.assertTrue(testSurvey.size() > 0);
        Survey toUpdate = testSurvey.get(0);
        toUpdate.setName("Modified Name");
        final Response response = target().path("survey").request().put(Entity.json(toUpdate), Response.class);
        Assert.assertEquals(200, response.getStatus());
        Survey modifiedSurvey = surveyService.get(toUpdate.getId());
        Assert.assertTrue("Not the same object", modifiedSurvey.equals(toUpdate));
        Assert.assertNotEquals("Name not modified", NAME, modifiedSurvey.getName());
        deleteAll(testSurvey);
    }
}
