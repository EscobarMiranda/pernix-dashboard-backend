package cr.pernix.dashboard.services;

import org.junit.Test;

import cr.pernix.dashboard.utils.ResourceManager;

import javax.ws.rs.core.Response.Status;

import org.junit.Assert;
import com.sparkpost.model.responses.Response;

public class MailServiceTest {
    
    @Test
    public void testSendMail() {
        Response response = MailService.getInstance().sendEmail("kescobar@pernix.cr", "Testing mail service", ResourceManager.getStringFromTemplate("MailTemplate.html"));
        Assert.assertEquals(Status.OK.getStatusCode(), response.getResponseCode());
    }
}
