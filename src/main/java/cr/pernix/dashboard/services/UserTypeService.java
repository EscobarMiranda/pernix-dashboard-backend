package cr.pernix.dashboard.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import cr.pernix.dashboard.models.UserType;
import cr.pernix.dashboard.utils.HibernateUtil;

public class UserTypeService {
    private static volatile UserTypeService instance = null;
    private static final Log LOGGER = LogFactory.getLog(UserTypeService.class);

    private UserTypeService() {
    }
    
    public static synchronized UserTypeService getInstance() {
        if (instance == null) {
            instance = new UserTypeService();
        }
        return instance;
    }

    public List<UserType> get() {
        return get(0, 0);
    }
    
    public List<UserType> get(int firstResult, int maxResult) {
        List<UserType> userTypes = new ArrayList<>();
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.getTransaction();
        transaction.begin();
        if (transaction.getStatus().equals(TransactionStatus.NOT_ACTIVE))
            LOGGER.debug(" >>> Transaction close.");
        Query query = session.createQuery("from UserType");
        query.setFirstResult(firstResult);
        query.setMaxResults(maxResult);
        @SuppressWarnings("unchecked")
        List<UserType> allUserTypes = query.list();
        transaction.commit();
        for (Object userObject : allUserTypes) {
            UserType userType = (UserType) userObject;
            userTypes.add(userType);
        }
        return userTypes;
    }
    
    public UserType get(int id) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        transaction.begin();
        UserType userType = (UserType) session.get(UserType.class, id);
        transaction.commit();
        return userType;
    }
    
    public void save(UserType userType) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        session.saveOrUpdate(userType);
        transaction.commit();
    }
    
    public UserType delete(int id) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        UserType userType = get(id);
        if (userType != null) {
            if (!session.isOpen()) {
                LOGGER.debug(" >>> Session close.");
                LOGGER.debug(" >>> Reopening session.");
                session = HibernateUtil.getSessionFactory().openSession();
            }
            Transaction transaction = session.getTransaction();
            transaction.begin();
            if (transaction.getStatus().equals(TransactionStatus.NOT_ACTIVE))
                LOGGER.debug(" >>> Transaction close.");
            session.delete(userType);
            transaction.commit();
        }
        return userType;
    }
}
