package service.produs;

import database.DbConnection;
import entities.Produs;
import exception.InexistentItemException;
import exception.ProdusAlreadyExistsException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Optional;

public class HibernateInventoryService implements InventoryService{

    private SessionFactory sessionFactory;
    public HibernateInventoryService(){this.sessionFactory= DbConnection.INSTANCE.getSessionFactory();}

    @Override
    public void add(Produs produs) throws ProdusAlreadyExistsException {

    }

    @Override
    public void delete(Produs produs) throws InexistentItemException {

    }

    @Override
    public void update(Produs produs) {

    }

    @Override
    public Optional<Produs> searchByTitle(String title) throws InexistentItemException {
        return Optional.empty();
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

    }
}
