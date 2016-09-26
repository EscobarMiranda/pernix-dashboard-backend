package cr.pernix.dashboard.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import cr.pernix.dashboard.models.CustomerSatisfaction;
import cr.pernix.dashboard.utils.HibernateUtil;

public class CustomerSatisfactionService {
    private static volatile CustomerSatisfactionService instance = null;
    private static final Log LOGGER = LogFactory.getLog(CustomerSatisfactionService.class);

    private CustomerSatisfactionService() {
    }

    public static synchronized CustomerSatisfactionService getInstance() {
        if (instance == null) {
            instance = new CustomerSatisfactionService();
        }
        return instance;
    }

    public List<CustomerSatisfaction> get() {
        return get(0, 0);
    }

    public List<CustomerSatisfaction> get(int firstResult, int maxResult) {
        List<CustomerSatisfaction> list = new ArrayList<>();
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.getTransaction();
        transaction.begin();
        if (transaction.getStatus().equals(TransactionStatus.NOT_ACTIVE))
            LOGGER.debug(" >>> Transaction close.");
        Query query = session.createQuery("from CustomerSatisfaction");
        query.setFirstResult(firstResult);
        query.setMaxResults(maxResult);
        @SuppressWarnings("unchecked")
        List<CustomerSatisfaction> allCostumerSatisfaction = query.list();
        transaction.commit();
        for (Object userObject : allCostumerSatisfaction) {
            CustomerSatisfaction customerSatisfaction = (CustomerSatisfaction) userObject;
            list.add(customerSatisfaction);
        }
        return list;
    }

    public CustomerSatisfaction get(int id) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        transaction.begin();
        CustomerSatisfaction customerSatisfaction = (CustomerSatisfaction) session.get(CustomerSatisfaction.class, id);
        transaction.commit();
        return customerSatisfaction;
    }

    public void save(CustomerSatisfaction customerSatisfaction) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        session.saveOrUpdate(customerSatisfaction);
        transaction.commit();
    }

    public void delete(int id) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.getTransaction();
        transaction.begin();
        if (transaction.getStatus().equals(TransactionStatus.NOT_ACTIVE))
            LOGGER.debug(" >>> Transaction close.");
        transaction.commit();
    }
}
