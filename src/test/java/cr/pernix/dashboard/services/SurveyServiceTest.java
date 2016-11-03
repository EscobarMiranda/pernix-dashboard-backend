package cr.pernix.dashboard.services;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import cr.pernix.dashboard.models.Survey;

public class SurveyServiceTest {
    
    private final String NAME = "Survey test";
    private final String DESCRIPTION = "Description survey test";
    private final boolean ACTIVE = true;
    private final boolean SENDMANAGERS = true;

    private SurveyService surveyService = SurveyService.getInstance();

    private List<Survey> insertTestSurveys(int count) {
        List<Survey> testSurveys = new ArrayList<>();
        for (; count > 0; count--) {
            Survey testSurvey = new Survey();
            testSurvey.setName(NAME);
            testSurvey.setDescription(DESCRIPTION);
            testSurvey.setActive(ACTIVE);
            testSurvey.setSendManagers(SENDMANAGERS);
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
    
    @Test
    public void testGetSurveySendToManagers() {
        List<Survey> testList = insertTestSurveys(5);
        List<Survey> listGet = surveyService.getSurveysSendToManagers();
        Assert.assertEquals(testList, listGet);
        deleteAll(testList);
    }
}
