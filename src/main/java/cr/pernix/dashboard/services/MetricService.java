package cr.pernix.dashboard.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import cr.pernix.dashboard.models.Metric;
import cr.pernix.dashboard.objects.AnswerObject;
import cr.pernix.dashboard.utils.HibernateUtil;

public class MetricService {
    private static volatile MetricService instance = null;
    private static final Log LOGGER = LogFactory.getLog(MetricService.class);

    private MetricService() {
    }

    public static synchronized MetricService getInstance() {
        if (instance == null) {
            instance = new MetricService();
        }
        return instance;
    }

    public List<Metric> get() {
        return get(0, 0);
    }

    public List<Metric> get(int firstResult, int maxResult) {
        List<Metric> metrics = new ArrayList<>();
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.getTransaction();
        transaction.begin();
        if (transaction.getStatus().equals(TransactionStatus.NOT_ACTIVE))
            LOGGER.debug(" >>> Transaction close.");
        Query query = session.createQuery("from Metric");
        query.setFirstResult(firstResult);
        query.setMaxResults(maxResult);
        @SuppressWarnings("unchecked")
        List<Metric> allMetrics = query.list();
        transaction.commit();
        for (Object userObject : allMetrics) {
            Metric metric = (Metric) userObject;
            metrics.add(metric);
        }
        return metrics;
    }
    
    public List<Metric> getBySurvey(int surveyId) {
        List<Metric> list = new ArrayList<>();
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.getTransaction();
        transaction.begin();
        if (transaction.getStatus().equals(TransactionStatus.NOT_ACTIVE))
            LOGGER.debug(" >>> Transaction close.");
        Query query = session.createSQLQuery("select id, name, description, active from Metric where survey_id = :surveyId");
        query.setParameter("surveyId", surveyId);
        @SuppressWarnings("unchecked")
        List<Object[]> allMetrics = query.list();
        transaction.commit();
        for (Object metricObject[] : allMetrics) {
            Metric metric = new Metric();
            metric.setId((int)metricObject[0]);
            metric.setName((String)metricObject[1]);
            metric.setDescription((String)metricObject[2]);
            metric.setActive((boolean)metricObject[3]);
            list.add(metric);
        }
        return list;
    }

    public Metric get(int id) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        transaction.begin();
        Metric metric = (Metric) session.get(Metric.class, id);
        transaction.commit();
        return metric;
    }

    public void save(Metric metric) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        session.saveOrUpdate(metric);
        transaction.commit();
    }
    
    public void changeState(Metric metric) {
        metric.setActive(!metric.getActive());
        save(metric);
    }

    public void delete(int id) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Metric metric = get(id);
        if (metric != null) {
            if (!session.isOpen()) {
                LOGGER.debug(" >>> Session close.");
                LOGGER.debug(" >>> Reopening session.");
                session = HibernateUtil.getSessionFactory().openSession();
            }
            Transaction transaction = session.getTransaction();
            transaction.begin();
            if (transaction.getStatus().equals(TransactionStatus.NOT_ACTIVE))
                LOGGER.debug(" >>> Transaction close.");
            session.delete(metric);
            transaction.commit();
        }
    }
}
