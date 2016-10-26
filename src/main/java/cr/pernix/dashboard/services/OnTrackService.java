package cr.pernix.dashboard.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import cr.pernix.dashboard.models.OnTrack;
import cr.pernix.dashboard.utils.HibernateUtil;

public class OnTrackService {
    private static volatile OnTrackService instance = null;
    private static final Log LOGGER = LogFactory.getLog(OnTrackService.class);

    private OnTrackService() {
    }

    public static synchronized OnTrackService getInstance() {
        if (instance == null) {
            instance = new OnTrackService();
        }
        return instance;
    }

    public List<OnTrack> get() {
        return get(0, 0);
    }

    public List<OnTrack> get(int firstResult, int maxResult) {
        List<OnTrack> onTrackList = new ArrayList<>();
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.getTransaction();
        transaction.begin();
        if (transaction.getStatus().equals(TransactionStatus.NOT_ACTIVE))
            LOGGER.debug(" >>> Transaction close.");
        Query query = session.createQuery("from OnTrack");
        query.setFirstResult(firstResult);
        query.setMaxResults(maxResult);
        @SuppressWarnings("unchecked")
        List<OnTrack> allOnTrack = query.list();
        transaction.commit();
        for (Object userObject : allOnTrack) {
            OnTrack onTrack = (OnTrack) userObject;
            onTrackList.add(onTrack);
        }
        return onTrackList;
    }

    public OnTrack get(int id) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        transaction.begin();
        OnTrack onTrack = (OnTrack) session.get(OnTrack.class, id);
        transaction.commit();
        return onTrack;
    }

    public void save(OnTrack onTrack) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        session.saveOrUpdate(onTrack);
        transaction.commit();
    }

    public void delete(int id) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        OnTrack onTrack = get(id);
        if (onTrack != null) {
            if (!session.isOpen()) {
                LOGGER.debug(" >>> Session close.");
                LOGGER.debug(" >>> Reopening session.");
                session = HibernateUtil.getSessionFactory().openSession();
            }
            Transaction transaction = session.getTransaction();
            transaction.begin();
            if (transaction.getStatus().equals(TransactionStatus.NOT_ACTIVE))
                LOGGER.debug(" >>> Transaction close.");
            session.delete(onTrack);
            transaction.commit();
        }
    }
}
