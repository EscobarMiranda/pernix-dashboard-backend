package cr.pernix.dashboard.services;

import java.io.IOException;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import com.google.common.io.BaseEncoding;

import cr.pernix.dashboard.models.User;
import cr.pernix.dashboard.objects.LoginObject;
import cr.pernix.dashboard.utils.HibernateUtil;

public class LoginService {
    private static volatile LoginService instance = null;
    private static final Log LOGGER = LogFactory.getLog(UserService.class);

    private LoginService() {
    }

    public static synchronized LoginService getInstance() {
        if (instance == null) {
            instance = new LoginService();
        }
        return instance;
    }
    
    public boolean authenticate(String authCredentials) {
        if (null == authCredentials)
            return false;

        final String encodedUserPassword = authCredentials.replaceFirst("Basic"
                + " ", "");
        String usernameAndPassword = null;
        try {

            byte[] decodedBytes = BaseEncoding.base64().decode(encodedUserPassword);
            usernameAndPassword = new String(decodedBytes, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        final StringTokenizer tokenizer = new StringTokenizer(
                usernameAndPassword, ":");
        final String email = tokenizer.nextToken();
        final String password = tokenizer.nextToken();
        
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.getTransaction();
        transaction.begin();
        if (transaction.getStatus().equals(TransactionStatus.NOT_ACTIVE))
            LOGGER.debug(" >>> Transaction close.");
        Query query = session.createQuery("from User u where u.email = :email");
        query.setParameter("email", email);
        @SuppressWarnings("unchecked")
        List<User> allUsers = query.list();
        transaction.commit();
        if(allUsers.size() > 0) {
            if(allUsers.get(0).getPassword().contentEquals(password)) {
                return true;
            }
        }
        return false;
    }

    public User login(LoginObject loginObject) {
        User user = new User();
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
            user = (User)allUsers.get(0);
        }
        else{  
            User userTmp = new User();
            userTmp.setActive(true);
            userTmp.setEmail(loginObject.getEmail());
            userTmp.setPassword(Hashing.sha1().hashString(loginObject.getPassword(), Charsets.UTF_8 ).toString());
            userTmp.setName(loginObject.getName());
            userTmp.setLastname(loginObject.getLastname());
            userTmp.setUserType(UserTypeService.getInstance().getByName("User"));
            UserService.getInstance().save(userTmp);
            user = userTmp;
        }
        return user;
    }
}
