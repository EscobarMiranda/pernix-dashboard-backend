package cr.pernix.dashboard.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import cr.pernix.dashboard.models.Survey;
import cr.pernix.dashboard.utils.HibernateUtil;

public class SurveyService {
    private static volatile SurveyService instance = null;
    private static final Log LOGGER = LogFactory.getLog(SurveyService.class);

    private SurveyService() {
    }

    public static synchronized SurveyService getInstance() {
        if (instance == null) {
            instance = new SurveyService();
        }
        return instance;
    }

    public List<Survey> get() {
        return get(0, 0);
    }

    public List<Survey> get(int firstResult, int maxResult) {
        List<Survey> surveys = new ArrayList<>();
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.getTransaction();
        transaction.begin();
        if (transaction.getStatus().equals(TransactionStatus.NOT_ACTIVE))
            LOGGER.debug(" >>> Transaction close.");
        Query query = session.createQuery("from Survey");
        query.setFirstResult(firstResult);
        query.setMaxResults(maxResult);
        @SuppressWarnings("unchecked")
        List<Survey> allSurveys = query.list();
        transaction.commit();
        for (Object object : allSurveys) {
            Survey survey = (Survey) object;
            surveys.add(survey);
        }
        return surveys;
    }

    public Survey get(int id) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        transaction.begin();
        Survey survey = (Survey) session.get(Survey.class, id);
        transaction.commit();
        return survey;
    }

    public void save(Survey survey) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        session.saveOrUpdate(survey);
        transaction.commit();
    }
    
    public void changeState(Survey survey) {
        survey.setActive(!survey.getActive());
        save(survey);
    }

    public void delete(int id) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Survey survey = get(id);
        if (survey != null) {
            if (!session.isOpen()) {
                LOGGER.debug(" >>> Session close.");
                LOGGER.debug(" >>> Reopening session.");
                session = HibernateUtil.getSessionFactory().openSession();
            }
            Transaction transaction = session.getTransaction();
            transaction.begin();
            if (transaction.getStatus().equals(TransactionStatus.NOT_ACTIVE))
                LOGGER.debug(" >>> Transaction close.");
            session.delete(survey);
            transaction.commit();
        }
    }
}
