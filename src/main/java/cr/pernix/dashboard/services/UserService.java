package cr.pernix.dashboard.services;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import cr.pernix.dashboard.models.User;
import cr.pernix.dashboard.utils.HibernateUtil;
import cr.pernix.dashboard.utils.ResourceManager;

public class UserService {
    private static volatile UserService instance = null;
    private static final Log LOGGER = LogFactory.getLog(UserService.class);
    private static final ResourceManager mailProperties = new ResourceManager("mail.properties");

    private UserService() {

    }

    public static synchronized UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    public List<User> get() {
        return get(0, 0);
    }

    public List<User> get(int firstResult, int maxResult) {
        List<User> users = new ArrayList<User>();
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.getTransaction();
        transaction.begin();
        if (transaction.getStatus().equals(TransactionStatus.NOT_ACTIVE))
            LOGGER.debug(" >>> Transaction close.");
        Query query = session.createQuery("from User");
        query.setFirstResult(firstResult);
        query.setMaxResults(maxResult);
        @SuppressWarnings("unchecked")
        List<User> allUsers = query.list();
        transaction.commit();
        for (Object object : allUsers) {
            User user = (User) object;
            User userObject = new User();
            userObject.setId(user.getId());
            userObject.setName(user.getName());
            userObject.setLastname(user.getLastname());
            userObject.setEmail(user.getEmail());
            userObject.setPassword(user.getPassword());
            userObject.setUserType(user.getUserType());
            users.add(userObject);
        }
        return users;
    }

    public User get(int id) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        transaction.begin();
        User user = (User) session.get(User.class, id);
        transaction.commit();
        return user;
    }

    public void save(User user) {
        user.setPassword(generatePassword());
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        session.saveOrUpdate(user);
        transaction.commit();
        MailService.getInstance().sendEmail(user.getEmail(), mailProperties.getValue("mail.new.user.subject"),
                ResourceManager.getStringFromTemplate("MailTemplate.html").replace("{%body%}",
                        mailProperties.getValue("mail.new.user.body").replace("{%email%}", user.getEmail())
                                .replace("{%password%}", user.getPassword())));
    }

    public void delete(int id) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        User user = get(id);
        if (user != null) {
            if (!session.isOpen()) {
                LOGGER.debug(" >>> Session close.");
                LOGGER.debug(" >>> Reopening session.");
                session = HibernateUtil.getSessionFactory().openSession();
            }
            Transaction transaction = session.getTransaction();
            transaction.begin();
            if (transaction.getStatus().equals(TransactionStatus.NOT_ACTIVE))
                LOGGER.debug(" >>> Transaction close.");
            session.delete(user);
            transaction.commit();
            MailService.getInstance().sendEmail(user.getEmail(), mailProperties.getValue("mail.delete.user.subject"),
                    ResourceManager.getStringFromTemplate("MailTemplate.html").replace("{%body%}",
                            mailProperties.getValue("mail.delete.user.body")));
        }
    }

    public String generatePassword() {
        return new BigInteger(40, new SecureRandom()).toString(32);
    }

}
