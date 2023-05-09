package service.produs;

import database.DbConnection;
import entities.Produs;
import exception.InexistentItemException;
import exception.ProdusAlreadyExistsException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Optional;

public class HibernateInventoryService implements InventoryService{

    private SessionFactory sessionFactory;
    public HibernateInventoryService(){this.sessionFactory= DbConnection.INSTANCE.getSessionFactory();}

    @Override
    public void add(Produs produs) throws ProdusAlreadyExistsException {
        Session session = this.sessionFactory.openSession();
        try {
            session.getTransaction().begin();

            // Check if the product already exists
            Produs existingProdus = session.get(Produs.class, produs.getIsbn());
            if (existingProdus != null) {
                throw new ProdusAlreadyExistsException("Product already exists in inventory");
            }

            // Add the new product to the inventory
            session.persist(produs);

            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public void delete(Produs produs) throws InexistentItemException {
        Session session = this.sessionFactory.openSession();
        try {
            session.getTransaction().begin();

            // Check if the product exists
            Produs existingProdus = session.get(Produs.class, produs.getIsbn());
            if (existingProdus == null) {
                throw new InexistentItemException("Product does not exist in inventory");
            }

            // Delete the product from the inventory
            session.delete(existingProdus);

            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public void update(Produs produs) {
        //de rezolvat update
    }

    @Override
    public Optional<Produs> searchByTitle(String title) throws InexistentItemException {
        Session session = this.sessionFactory.openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Produs> query = builder.createQuery(Produs.class);
        Root<Produs> root = query.from(Produs.class);
        query.select(root).where(builder.equal(root.get("title"), title));
        TypedQuery<Produs> typedQuery = session.createQuery(query);
        Produs produs = typedQuery.getSingleResult();
        session.close();
        if (produs == null) {
            throw new InexistentItemException("Product with title " + title + " does not exist");
        }
        return Optional.of(produs);
    }

    @Override
    public Produs searchByIsbn(String isbn) throws InexistentItemException {
        Session session = this.sessionFactory.openSession();
        Produs produs = session.get(Produs.class, isbn);
       session.close();
       if( produs == null) {
           throw new InexistentItemException("produs doest not exist");
       } else {
        return produs;
    }}

    @Override
    public void displayAll() {
        Session session = this.sessionFactory.openSession();
        try {
            session.getTransaction().begin();

            // Retrieve all products from the inventory
            List<Produs> produse = session.createQuery("FROM Produs").getResultList();

            // Print the details of each product
            for (Produs produs : produse) {
                System.out.println(produs.toString());
            }

            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }
}
