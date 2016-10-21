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

import cr.pernix.dashboard.models.OnTrack;
import cr.pernix.dashboard.services.OnTrackService;

public class OnTrackResourceTest extends JerseyTest {

    private final String NAME = "Test";

    private OnTrackService onTrackService = OnTrackService.getInstance();

    private List<OnTrack> insertTestOnTrack(int count) {
        List<OnTrack> testOnTrackList = new ArrayList<>();
        for (; count > 0; count--) {
            OnTrack testOnTrack = new OnTrack();
            testOnTrack.setName(NAME);
            onTrackService.save(testOnTrack);
            testOnTrackList.add(testOnTrack);
        }
        return testOnTrackList;
    }
    
    private void deleteAll(List<OnTrack> onTrackList) {
        for(OnTrack onTrack: onTrackList) {
            onTrackService.delete(onTrack.getId());
        }
    }

    @Override
    protected Application configure() {
        return new ResourceConfig(OnTrackResource.class);
    }

    @Test
    public void testGetAll() {
        List<OnTrack> onTrack = insertTestOnTrack(5);
        Assert.assertTrue(onTrack.size() == 5);
        final Response response = target().path("onTrack").request().get();
        Assert.assertEquals(200, response.getStatus());
        List<OnTrack> onTrackList = response.readEntity(new GenericType<List<OnTrack>>() {
        });
        Assert.assertEquals(onTrack.size(), onTrackList.size());
        deleteAll(onTrackList);
    }

    @Test
    public void testGet() {
        List<OnTrack> testOnTrack = insertTestOnTrack(1);
        Assert.assertTrue(testOnTrack.size() > 0);
        OnTrack toCompare = testOnTrack.get(0);
        final String path = "onTrack/%d";
        final Response response = target().path(String.format(path, toCompare.getId())).request().get();
        Assert.assertEquals(200, response.getStatus());
        OnTrack onTrack = response.readEntity(OnTrack.class);
        Assert.assertTrue("Object do not match", onTrack.equals(toCompare));
        deleteAll(testOnTrack);
    }
    
    @Test
    public void testEdit() {
        List<OnTrack> testOnTrack = insertTestOnTrack(1);
        Assert.assertTrue(testOnTrack.size() > 0);
        OnTrack toUpdate = testOnTrack.get(0);
        toUpdate.setName("Modified Name");
        final Response response = target().path("onTrack").request().put(Entity.json(toUpdate), Response.class);
        Assert.assertEquals(200, response.getStatus());
        OnTrack modifiedOnTrack = OnTrackService.getInstance().get(toUpdate.getId());
        Assert.assertTrue("Not the same object", modifiedOnTrack.equals(toUpdate));
        Assert.assertNotEquals("Name not modified", NAME, modifiedOnTrack.getName());
        deleteAll(testOnTrack);
    }
}
