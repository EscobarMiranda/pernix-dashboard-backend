package cr.pernix.dashboard.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import cr.pernix.dashboard.models.Manager;
import cr.pernix.dashboard.utils.HibernateUtil;

public class ManagerService {
    private static volatile ManagerService instance = null;
    private static final Log LOGGER = LogFactory.getLog(ManagerService.class);

    private ManagerService() {
    }

    public static synchronized ManagerService getInstance() {
        if (instance == null) {
            instance = new ManagerService();
        }
        return instance;
    }

    public List<Manager> get() {
        return get(0, 0);
    }

    public List<Manager> get(int firstResult, int maxResult) {
        List<Manager> managers = new ArrayList<>();
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.getTransaction();
        transaction.begin();
        if (transaction.getStatus().equals(TransactionStatus.NOT_ACTIVE))
            LOGGER.debug(" >>> Transaction close.");
        Query query = session.createQuery("from Manager");
        query.setFirstResult(firstResult);
        query.setMaxResults(maxResult);
        @SuppressWarnings("unchecked")
        List<Manager> allManagers = query.list();
        transaction.commit();
        for (Object userObject : allManagers) {
            Manager manager = (Manager) userObject;
            managers.add(manager);
        }
        return managers;
    }

    public Manager get(int id) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        transaction.begin();
        Manager manager = (Manager) session.get(Manager.class, id);
        transaction.commit();
        return manager;
    }

    public void save(Manager manager) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        session.saveOrUpdate(manager);
        transaction.commit();
    }

    public Manager delete(int id) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Manager manager = get(id);
        if (manager != null) {
            if (!session.isOpen()) {
                LOGGER.debug(" >>> Session close.");
                LOGGER.debug(" >>> Reopening session.");
                session = HibernateUtil.getSessionFactory().openSession();
            }
            Transaction transaction = session.getTransaction();
            transaction.begin();
            if (transaction.getStatus().equals(TransactionStatus.NOT_ACTIVE))
                LOGGER.debug(" >>> Transaction close.");
            session.delete(manager);
            transaction.commit();
        }
        return manager;
    }
}
