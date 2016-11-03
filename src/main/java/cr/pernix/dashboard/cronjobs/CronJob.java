package cr.pernix.dashboard.cronjobs;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import cr.pernix.dashboard.models.Survey;
import cr.pernix.dashboard.models.User;
import cr.pernix.dashboard.services.MailService;
import cr.pernix.dashboard.services.SurveyService;
import cr.pernix.dashboard.services.UserService;
import cr.pernix.dashboard.utils.ResourceManager;

public class CronJob implements Job {
    private static final Log LOGGER = LogFactory.getLog(UserService.class);
    private SurveyService surveyService = SurveyService.getInstance();
    private UserService userService = UserService.getInstance();
    private MailService mailService = MailService.getInstance();
    private static final ResourceManager mailProperties = new ResourceManager("mail.properties");

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        LOGGER.info("Cron job started");
        List<Survey> surveyList = surveyService.getSurveysSendToManagers();
        List<User> userList = userService.get();
        for (Survey survey : surveyList) {
            for (User user : userList) {
                mailService.sendEmail(user.getManager().getEmail(),
                        mailProperties.getValue("mail.new.manager.subject").replace("{%surveyName%}", survey.getName()),
                        ResourceManager.getStringFromTemplate("MailTemplate.html").replace("{%body%}",
                                mailProperties.getValue("mail.new.manager.body")
                                        .replace("{%surveyId%}", Integer.toString(survey.getId()))
                                        .replace("{%userId%}", Integer.toString(user.getId()))));
                LOGGER.info("Sending email to:" + user.getManager().getEmail());
            }
        }
    }
}
