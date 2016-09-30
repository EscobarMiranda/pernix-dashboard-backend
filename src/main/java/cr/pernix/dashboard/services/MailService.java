package cr.pernix.dashboard.services;

import javax.ws.rs.core.Response.Status;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sparkpost.Client;
import com.sparkpost.model.responses.Response;

import cr.pernix.dashboard.utils.ResourceManager;

public class MailService {
    private static volatile MailService instance = null;
    private static final Log LOGGER = LogFactory.getLog(MailService.class);
    private static final ResourceManager mailProperties = new ResourceManager("mail.properties");

    private MailService() {

    }

    public static synchronized MailService getInstance() {
        if (instance == null) {
            instance = new MailService();
        }
        return instance;
    }

    public Response sendEmail(String recipient, String subject, String body) {
        Response response = new Response();
        try {
            Client client = new Client(mailProperties.getValue("mail.api.key"));
            response = client.sendMessage(mailProperties.getValue("mail.api.email"), recipient, subject, body, body);
            return response;
        } catch (Exception mailerException) {
            LOGGER.error(mailerException);
            mailerException.printStackTrace();
            response.setResponseCode(Status.BAD_REQUEST.getStatusCode());
            return response;
        }
    }
}
