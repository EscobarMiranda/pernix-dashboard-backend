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

import cr.pernix.dashboard.models.Project;
import cr.pernix.dashboard.models.User;
import cr.pernix.dashboard.models.UserType;
import cr.pernix.dashboard.services.ProjectService;
import cr.pernix.dashboard.services.UserService;
import cr.pernix.dashboard.services.UserTypeService;

public class ProjectResourceTest extends JerseyTest {

private static Logger LOG = Logger.getLogger(ProjectResourceTest.class);
    
    private final String NAME = "Project Test";
    private final String DESCRIPTION = "Description test";
    private final Date START = new Date(2016, 9, 26); 
    private final Date END = new Date(2016, 9, 26);
    private final Date LASTDEMO = new Date(2016, 9, 26);
    private final String LASTUPDATE = "Update test";
    private final float PERCENTAGE = (float) 80.5;
    private final boolean ONTRACK = true;
    private final boolean ACTIVATE = true;
    
    private final String USER_NAME = "Kevin";
    private final String LASTNAME = "Escobar Miranda";
    private final String EMAIL = "kescobar@pernix.cr";
    private final boolean ACTIVE = true;
    private final String USERTYPENAME = "admintest";

    private UserService userService = UserService.getInstance();
    private UserTypeService userTypeService = UserTypeService.getInstance();
    private ProjectService projectService = ProjectService.getInstance();
    
    private List<Project> insertTestProjects(int count) {
        List<Project> testProjects = new ArrayList<Project>();
        for (; count > 0; count--) {
            Project projectObject = new Project();
            projectObject.setName(NAME);
            projectObject.setDescription(DESCRIPTION);
            projectObject.setStartDate(START);
            projectObject.setEndDate(END);
            projectObject.setLastDemo(LASTDEMO);
            projectObject.setLastUpdate(LASTUPDATE);
            projectObject.setPercentage(PERCENTAGE);
            projectObject.setOnTrack(ONTRACK);
            projectObject.setActivate(ACTIVATE);
            projectObject.setUser(insertTestUser());
            projectService.save(projectObject);
            testProjects.add(projectObject);
        }
        return testProjects;
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
            projectService.delete(project.getId());
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
        deleteAll(testProjects);
    }
    
    @Test
    public void testGetSingleProject() {
        List<Project> testProject = insertTestProjects(1);
        Assert.assertTrue(testProject.size() > 0);
        Project toCompare = testProject.get(0);
        String path = "project/%d";
        final Response response = target().path(String.format(path, toCompare.getId())).request().get();
        Assert.assertEquals(200, response.getStatus());
        Project project = response.readEntity(Project.class);
        Assert.assertTrue("Object do not match", project.equals(toCompare));
        deleteAll(testProject);
    }

    @Test
    public void testDeleteUser() {
        List<Project> testProject = insertTestProjects(1);
        Assert.assertTrue(testProject.size() > 0);
        Project toDelete = testProject.get(0);
        String path = "project/%d";
        final Response response = target().path(String.format(path, toDelete.getId())).request().delete();
        Assert.assertEquals(200, response.getStatus());
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
