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
import org.jboss.logging.Logger;
import org.junit.Assert;
import org.junit.Test;

import cr.pernix.dashboard.models.OnTrack;
import cr.pernix.dashboard.models.Project;
import cr.pernix.dashboard.models.User;
import cr.pernix.dashboard.models.UserType;
import cr.pernix.dashboard.services.OnTrackService;
import cr.pernix.dashboard.services.ProjectService;
import cr.pernix.dashboard.services.UserService;
import cr.pernix.dashboard.services.UserTypeService;

public class ProjectResourceTest extends JerseyTest {

    private static Logger LOG = Logger.getLogger(ProjectResourceTest.class);

    private final String NAME = "Project Test";
    private final String DESCRIPTION = "Description test";
    private final Date START = new Date("2016/9/26");
    private final Date END = new Date("2016/9/26");
    private final Date LASTDEMO = new Date("2016/9/26");
    private final String LASTUPDATE = "Update test";
    private final float PERCENTAGE = (float) 80.5;
    private final boolean ACTIVATE = true;

    private final String USER_NAME = "Kevin";
    private final String LASTNAME = "Escobar Miranda";
    private final String EMAIL = "kescobar@pernix.cr";
    private final boolean ACTIVE = true;
    private final String USERTYPENAME = "admintest";

    private UserService userService = UserService.getInstance();
    private UserTypeService userTypeService = UserTypeService.getInstance();
    private ProjectService projectService = ProjectService.getInstance();
    private OnTrackService onTrackService = OnTrackService.getInstance();

    private List<Project> insertTestProjects(int count) {
        List<Project> testProjects = new ArrayList<Project>();
        for (; count > 0; count--) {
            Project project = new Project();
            project.setName(NAME);
            project.setDescription(DESCRIPTION);
            project.setStartDate(START);
            project.setEndDate(END);
            project.setLastDemo(LASTDEMO);
            project.setLastUpdate(LASTUPDATE);
            project.setPercentage(PERCENTAGE);
            project.setOnTrack(insertTestOnTrack());
            project.setActive(ACTIVATE);
            project.setUser(insertTestUser());
            projectService.save(project);
            testProjects.add(project);
        }
        return testProjects;
    }
    
    private OnTrack insertTestOnTrack() {
        OnTrack testOnTrack = new OnTrack();
        testOnTrack.setName(NAME);
        onTrackService.save(testOnTrack);
        return testOnTrack;
    }

    private UserType insertTestUserType() {
        UserType testUserType = new UserType();
        testUserType.setName(USERTYPENAME);
        userTypeService.save(testUserType);
        return testUserType;
    }

    private User insertTestUser() {
        User testUser = new User();
        testUser.setName(USER_NAME);
        testUser.setLastname(LASTNAME);
        testUser.setEmail(EMAIL);
        testUser.setActive(ACTIVE);
        testUser.setUserType(insertTestUserType());
        userService.save(testUser);
        return testUser;
    }

    private void deleteAll(List<Project> projectList) {
        for (Project project : projectList) {
            System.out.println(project.getId());
            projectService.delete(project.getId());
            onTrackService.delete(project.getOnTrack().getId());
            userService.delete(project.getUser().getId());
            userTypeService.delete(project.getUser().getUserType().getId());
        }
    }

    @Override
    protected Application configure() {
        return new ResourceConfig(ProjectResource.class);
    }

    @Test
    public void testGetAll() {
        List<Project> testProjects = insertTestProjects(5);
        Assert.assertTrue(testProjects.size() == 5);
        final Response response = target().path("project").request().get();
        Assert.assertEquals(200, response.getStatus());
        List<Project> projectsList = response.readEntity(new GenericType<List<Project>>() {
        });
        Assert.assertEquals(testProjects.size(), projectsList.size());
        deleteAll(projectsList);
    }

    @Test
    public void testGetByUser() {
        List<Project> testProjects = insertTestProjects(5);
        Assert.assertTrue(testProjects.size() == 5);
        int userId = testProjects.get(0).getUser().getId();
        final String path = "project/byUser/%d";
        final Response response = target().path(String.format(path, userId)).request().get();
        Assert.assertEquals(200, response.getStatus());
        List<Project> projectsList = response.readEntity(new GenericType<List<Project>>() {
        });
        Assert.assertTrue(projectsList.size() > 0);
        deleteAll(projectsList);
    }

    @Test
    public void testGetSingleProject() {
        List<Project> testProject = insertTestProjects(1);
        Assert.assertTrue(testProject.size() > 0);
        Project toCompare = testProject.get(0);
        final String path = "project/%d";
        final Response response = target().path(String.format(path, toCompare.getId())).request().get();
        Assert.assertEquals(200, response.getStatus());
        Project project = response.readEntity(Project.class);
        Assert.assertNotNull(project);
        deleteAll(testProject);
    }

    @Test
    public void testDeleteUser() {
        List<Project> testProject = insertTestProjects(1);
        Assert.assertTrue(testProject.size() > 0);
        Project toDelete = testProject.get(0);
        final String path = "project/%d";
        final Response response = target().path(String.format(path, toDelete.getId())).request().delete();
        Assert.assertEquals(200, response.getStatus());
        deleteAll(testProject);
    }

    @Test
    public void testChangeState() {
        List<Project> testProject = insertTestProjects(1);
        Assert.assertTrue(testProject.size() > 0);
        Project toChangeState = testProject.get(0);
        Response response = target().path("project/changeState").request().put(Entity.json(toChangeState), Response.class);
        Assert.assertEquals(200, response.getStatus());
        Project project = projectService.get(toChangeState.getId());
        Assert.assertFalse(project.getActive());
        response = target().path("project/changeState").request().put(Entity.json(project), Response.class);
        Assert.assertEquals(200, response.getStatus());
        project = projectService.get(project.getId());
        Assert.assertTrue(project.getActive());
        deleteAll(testProject);
    }

    @Test
    public void testEditProject() {
        List<Project> testProject = insertTestProjects(1);
        Assert.assertTrue(testProject.size() > 0);
        Project toUpdate = testProject.get(0);
        toUpdate.setName("Modified Name");
        final Response response = target().path("project").request().put(Entity.json(toUpdate), Response.class);
        Assert.assertEquals(200, response.getStatus());
        Project modifiedUser = ProjectService.getInstance().get(toUpdate.getId());
        Assert.assertTrue("Not the same object", modifiedUser.equals(toUpdate));
        Assert.assertNotEquals("Name not modified", NAME, modifiedUser.getName());
        deleteAll(testProject);
    }
}
