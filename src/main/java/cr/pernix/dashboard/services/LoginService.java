package cr.pernix.dashboard.services;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import cr.pernix.dashboard.models.User;
import cr.pernix.dashboard.objects.LoginObject;
import cr.pernix.dashboard.utils.HibernateUtil;

public class LoginService {
    private static volatile LoginService instance = null;
    private static final Log LOGGER = LogFactory.getLog(UserService.class);
    private UserService userService = UserService.getInstance();

    private LoginService() {
    }

    public static synchronized LoginService getInstance() {
        if (instance == null) {
            instance = new LoginService();
        }
        return instance;
    }

    public User login(LoginObject loginObject) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.getTransaction();
        transaction.begin();
        if (transaction.getStatus().equals(TransactionStatus.NOT_ACTIVE))
            LOGGER.debug(" >>> Transaction close.");
        Query query = session.createQuery("from User u where u.email = :email");
        query.setParameter("email", loginObject.getEmail());
        @SuppressWarnings("unchecked")
        List<User> allUsers = query.list();
        transaction.commit();
        if (allUsers.size() > 0) {
            User user = (User)allUsers.get(0);
            if (user.getPassword().equals(loginObject.getPassword())) {
                return user;
            }
        }
        return null;
    }
}
