package cr.pernix.dashboard.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import cr.pernix.dashboard.models.Answer;
import cr.pernix.dashboard.objects.AnswerObject;
import cr.pernix.dashboard.utils.HibernateUtil;

public class AnswerService {
    private static volatile AnswerService instance = null;
    private static final Log LOGGER = LogFactory.getLog(AnswerService.class);

    private AnswerService() {
    }

    public static synchronized AnswerService getInstance() {
        if (instance == null) {
            instance = new AnswerService();
        }
        return instance;
    }

    public List<Answer> get() {
        return get(0, 0);
    }

    public List<Answer> get(int firstResult, int maxResult) {
        List<Answer> list = new ArrayList<>();
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.getTransaction();
        transaction.begin();
        if (transaction.getStatus().equals(TransactionStatus.NOT_ACTIVE))
            LOGGER.debug(" >>> Transaction close.");
        Query query = session.createQuery("from Answer");
        query.setFirstResult(firstResult);
        query.setMaxResults(maxResult);
        @SuppressWarnings("unchecked")
        List<Answer> allCostumerSatisfaction = query.list();
        transaction.commit();
        for (Object userObject : allCostumerSatisfaction) {
            Answer customerSatisfaction = (Answer) userObject;
            list.add(customerSatisfaction);
        }
        return list;
    }
    
    public List<AnswerObject> getGeneral() {
        List<AnswerObject> list = new ArrayList<>();
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.getTransaction();
        transaction.begin();
        if (transaction.getStatus().equals(TransactionStatus.NOT_ACTIVE))
            LOGGER.debug(" >>> Transaction close.");
        Query query = session.createSQLQuery("select csat.metric_id as id, m.name as name, avg(csat.value) as value from Answer csat join Metric m on m.id = csat.metric_id group by metric_id, m.name");
        @SuppressWarnings("unchecked")
        List<Object[]> allCostumerSatisfaction = query.list();
        transaction.commit();
        createObjectList(list, allCostumerSatisfaction);
        return list;
    }
    
    public List<AnswerObject> getCompany(int companyId) {
        List<AnswerObject> list = new ArrayList<>();
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.getTransaction();
        transaction.begin();
        if (transaction.getStatus().equals(TransactionStatus.NOT_ACTIVE))
            LOGGER.debug(" >>> Transaction close.");
        Query query = session.createSQLQuery("select csat.metric_id as id, me.name as name, avg(csat.value) as value from Answer csat join Metric me on me.id = csat.metric_id join public.User u on u.id = csat.user_id join Manager m on m.id = u.manager_id join Company c on c.id = m.company_id where c.id = :companyId group by metric_id, me.name, c.name;");
        query.setParameter("companyId", companyId);
        @SuppressWarnings("unchecked")
        List<Object[]> allCostumerSatisfaction = query.list();
        transaction.commit();
        createObjectList(list, allCostumerSatisfaction);
        return list;
    }
    
    public List<AnswerObject> getUser(int userId) {
        List<AnswerObject> list = new ArrayList<>();
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.getTransaction();
        transaction.begin();
        if (transaction.getStatus().equals(TransactionStatus.NOT_ACTIVE))
            LOGGER.debug(" >>> Transaction close.");
        Query query = session.createSQLQuery("select csat.metric_id as id, m.name as name, avg(csat.value) as value from Answer csat join Metric m on m.id = csat.metric_id where csat.user_id = :userId group by metric_id, m.name, csat.user_id;");
        query.setParameter("userId", userId);
        @SuppressWarnings("unchecked")
        List<Object[]> allCostumerSatisfaction = query.list();
        transaction.commit();
        createObjectList(list, allCostumerSatisfaction);
        return list;
    }

    private void createObjectList(List<AnswerObject> list, List<Object[]> allCostumerSatisfaction) {
        for (Object userObject[] : allCostumerSatisfaction) {
            AnswerObject customerSatisfaction = new AnswerObject();
            customerSatisfaction.setId((int)userObject[0]);
            customerSatisfaction.setName((String)userObject[1]);
            customerSatisfaction.setValue(Float.parseFloat(userObject[2].toString()));
            list.add(customerSatisfaction);
        }
    }

    public Answer get(int id) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        transaction.begin();
        Answer customerSatisfaction = (Answer) session.get(Answer.class, id);
        transaction.commit();
        return customerSatisfaction;
    }

    public void save(Answer answer) {
        System.out.println(new Date());
        answer.setTimestamp(new Date());
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        session.saveOrUpdate(answer);
        transaction.commit();
    }

    public void delete(int id) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Answer customerSatisfaction = get(id);
        if (customerSatisfaction != null) {
            if (!session.isOpen()) {
                LOGGER.debug(" >>> Session close.");
                LOGGER.debug(" >>> Reopening session.");
                session = HibernateUtil.getSessionFactory().openSession();
            }
            Transaction transaction = session.getTransaction();
            transaction.begin();
            if (transaction.getStatus().equals(TransactionStatus.NOT_ACTIVE))
                LOGGER.debug(" >>> Transaction close.");
            session.delete(customerSatisfaction);
            transaction.commit();
        }
    }
}
