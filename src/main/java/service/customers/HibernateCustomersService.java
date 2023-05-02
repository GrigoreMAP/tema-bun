package service.customers;

import database.DbConnection;
import entities.Customer;
import jakarta.persistence.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Optional;

public class HibernateCustomersService implements CustomersService {
    private SessionFactory sessionFactory;

    public HibernateCustomersService() {
        this.sessionFactory = DbConnection.INSTANCE.getSessionFactory();
    }

    @Override
    public Optional<Customer> get(String email) {
        Session session = this.sessionFactory.openSession();

        Query query = session.createNamedQuery("findByEmail", Customer.class);
        query.setParameter("email", email);

        List<Customer> customers = query.getResultList();
        // PERSISTENT

        session.close();

        //DETACHED

        if (customers.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(customers.get(0));
        }

    }

    @Override
    public void add(Customer customer) {

    }

    @Override
    public void delete(Customer customer) {

    }

    @Override
    public void update(Customer customer) {

    }
}


