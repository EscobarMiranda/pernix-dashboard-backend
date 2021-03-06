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

import cr.pernix.dashboard.models.Metric;
import cr.pernix.dashboard.models.Survey;
import cr.pernix.dashboard.services.MetricService;
import cr.pernix.dashboard.services.SurveyService;

public class MetricResourceTest extends JerseyTest {

    private final String NAME = "Resolution Rate";
    private final String DESCRIPTION = "Resolution Rate Description";
    private final boolean ACTIVE = true;

    private MetricService metricService = MetricService.getInstance();
    private SurveyService surveyService = SurveyService.getInstance();

    private List<Metric> insertTestMetrics(int count) {
        List<Metric> testMetrics = new ArrayList<>();
        for (; count > 0; count--) {
            Metric testMetric = new Metric();
            testMetric.setName(NAME);
            testMetric.setDescription(DESCRIPTION);
            testMetric.setActive(ACTIVE);
            testMetric.setSurvey(insertTestSurvey());
            metricService.save(testMetric);
            testMetrics.add(testMetric);
        }
        return testMetrics;
    }

    private Survey insertTestSurvey() {
        Survey testSurvey = new Survey();
        testSurvey.setName(NAME);
        testSurvey.setActive(ACTIVE);
        surveyService.save(testSurvey);
        return testSurvey;
    }

    private void deleteAll(List<Metric> metricList) {
        for (Metric metric : metricList) {
            metricService.delete(metric.getId());
            surveyService.delete(metric.getSurvey().getId());
        }
    }

    @Override
    protected Application configure() {
        return new ResourceConfig(MetricResource.class);
    }

    @Test
    public void testGetAll() {
        List<Metric> testMetric = insertTestMetrics(5);
        Assert.assertTrue(testMetric.size() == 5);
        final Response response = target().path("metric").request().get();
        Assert.assertEquals(200, response.getStatus());
        List<Metric> metricsList = response.readEntity(new GenericType<List<Metric>>() {
        });
        Assert.assertEquals(testMetric.size(), metricsList.size());
        deleteAll(testMetric);
    }

    @Test
    public void testGetBySurvey() {
        List<Metric> testMetric = insertTestMetrics(5);
        Assert.assertTrue(testMetric.size() == 5);
        Survey survey = testMetric.get(0).getSurvey();
        final String path = "metric/bySurvey/%d";
        final Response response = target().path(String.format(path, survey.getId())).request().get();
        Assert.assertEquals(200, response.getStatus());
        List<Metric> metricsList = response.readEntity(new GenericType<List<Metric>>() {
        });
        Assert.assertTrue(testMetric.size() > 0);
        deleteAll(testMetric);
    }

    @Test
    public void testGet() {
        List<Metric> testMetric = insertTestMetrics(1);
        Assert.assertTrue(testMetric.size() > 0);
        Metric toCompare = testMetric.get(0);
        final String path = "metric/%d";
        final Response response = target().path(String.format(path, toCompare.getId())).request().get();
        Assert.assertEquals(200, response.getStatus());
        Metric metric = response.readEntity(Metric.class);
        Assert.assertTrue("Object do not match", metric.equals(toCompare));
        deleteAll(testMetric);
    }

    @Test
    public void testChangeState() {
        List<Metric> testMetric = insertTestMetrics(1);
        Assert.assertTrue(testMetric.size() > 0);
        Metric toChangeState = testMetric.get(0);
        Response response = target().path("metric/changeState").request().put(Entity.json(toChangeState),
                Response.class);
        Assert.assertEquals(200, response.getStatus());
        Metric metric = metricService.get(toChangeState.getId());
        Assert.assertFalse(metric.getActive());
        response = target().path("metric/changeState").request().put(Entity.json(metric), Response.class);
        Assert.assertEquals(200, response.getStatus());
        metric = metricService.get(metric.getId());
        Assert.assertTrue(metric.getActive());
        deleteAll(testMetric);
    }

    @Test
    public void testEdit() {
        List<Metric> testMetric = insertTestMetrics(1);
        Assert.assertTrue(testMetric.size() > 0);
        Metric toUpdate = testMetric.get(0);
        toUpdate.setName("Modified Name");
        final Response response = target().path("metric").request().put(Entity.json(toUpdate), Response.class);
        Assert.assertEquals(200, response.getStatus());
        Metric modifiedMetric = MetricService.getInstance().get(toUpdate.getId());
        Assert.assertTrue("Not the same object", modifiedMetric.equals(toUpdate));
        Assert.assertNotEquals("Name not modified", NAME, modifiedMetric.getName());
        deleteAll(testMetric);
    }
}
