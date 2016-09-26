package cr.pernix.dashboard.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import cr.pernix.dashboard.models.Company;
import cr.pernix.dashboard.utils.HibernateUtil;

public class CompanyService {
    private static volatile CompanyService instance = null;
    private static final Log LOGGER = LogFactory.getLog(CompanyService.class);

    private CompanyService() {
    }

    public static synchronized CompanyService getInstance() {
        if (instance == null) {
            instance = new CompanyService();
        }
        return instance;
    }

    public List<Company> get() {
        return get(0, 0);
    }

    public List<Company> get(int firstResult, int maxResult) {
        List<Company> companies = new ArrayList<>();
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.getTransaction();
        transaction.begin();
        if (transaction.getStatus().equals(TransactionStatus.NOT_ACTIVE))
            LOGGER.debug(" >>> Transaction close.");
        Query query = session.createQuery("from Company");
        query.setFirstResult(firstResult);
        query.setMaxResults(maxResult);
        @SuppressWarnings("unchecked")
        List<Company> allCompanies = query.list();
        transaction.commit();
        for (Object userObject : allCompanies) {
            Company company = (Company) userObject;
            companies.add(company);
        }
        return companies;
    }

    public Company get(int id) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        transaction.begin();
        Company company = (Company) session.get(Company.class, id);
        transaction.commit();
        return company;
    }

    public void save(Company company) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        session.saveOrUpdate(company);
        transaction.commit();
    }

    public void delete(int id) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Company company = get(id);
        if (company != null) {
            if (!session.isOpen()) {
                LOGGER.debug(" >>> Session close.");
                LOGGER.debug(" >>> Reopening session.");
                session = HibernateUtil.getSessionFactory().openSession();
            }
            Transaction transaction = session.getTransaction();
            transaction.begin();
            if (transaction.getStatus().equals(TransactionStatus.NOT_ACTIVE))
                LOGGER.debug(" >>> Transaction close.");
            session.delete(company);
            transaction.commit();
        }
    }
}
