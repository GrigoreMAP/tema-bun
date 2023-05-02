package service.shopping_cart;

import database.DbConnection;
import entities.*;
import exception.InexistentItemException;
import exception.InvalidQuantityException;
import jakarta.persistence.Query;
import model.ShoppingCartStatus;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class HibernateShoppingCartService implements ShoppingCartService {
    private SessionFactory sessionFactory;

    public HibernateShoppingCartService() {
        this.sessionFactory = DbConnection.INSTANCE.getSessionFactory();
    }

    @Override
    public void addToCart(Customer customer, Produs produs) {
        Session session = this.sessionFactory.openSession();

        Transaction transaction = session.beginTransaction();

        ShoppingCart openShoppingCart = null;
        for (ShoppingCart shoppingCart : customer.getShoppingCarts()) {
            if (shoppingCart.getStatus().equals(ShoppingCartStatus.OPEN)) {
                openShoppingCart = shoppingCart;
                break;
            }
        }

        ShoppingCart customerShoppingCart = null;
        if (openShoppingCart == null) {
            //we don't have an open shopping cart

            customerShoppingCart = new ShoppingCart();

            customerShoppingCart.setId(System.currentTimeMillis());
            customerShoppingCart.setStatus(ShoppingCartStatus.OPEN);
            customerShoppingCart.setLastEdited(LocalDateTime.now());
            customerShoppingCart.setCustomer(customer);

            //customer is in NEW state
            customer.getShoppingCarts().add(customerShoppingCart);

            session.merge(customerShoppingCart);


        } else {
            //customer already has an open shopping cart
            //insert or update just the item
            customerShoppingCart = openShoppingCart;
        }

        //here we have a shopping cart for sure

        boolean itemExists = false;
        for (ShoppingCartItem item : customerShoppingCart.getItems()) {
            if (item.getProdus().getIsbn().equals(produs.getIsbn())) {
                itemExists = true;
                int oldQuantity = item.getQuantity();
                item.setQuantity(oldQuantity + 1);

                session.merge(item);
                break;
            }
        }

        if (!itemExists) {
            //we need to add it
            ShoppingCartItem newShoppingCartItem = new ShoppingCartItem();
            newShoppingCartItem.setQuantity(1);
            newShoppingCartItem.setProdus(produs);
            newShoppingCartItem.setShoppingCart(customerShoppingCart);
            newShoppingCartItem.setId(new ShoppingCartItemPK(produs.getIsbn(), customerShoppingCart.getId()));

            session.merge(newShoppingCartItem);

            customerShoppingCart.getItems().add(newShoppingCartItem);
        }


        transaction.commit();
        session.close();
    }


    @Override
    public void removeFromCart(Customer customer, Produs produs) throws InexistentItemException {

    }

    @Override
    public void updateQuantity(Customer customer, Produs produs, int quantity) throws InvalidQuantityException, InexistentItemException {

    }

    @Override
    public int getQuantity(Customer customer, Produs produs) {
        Session session = this.sessionFactory.openSession();

        Query query = session.createNamedQuery("getQuantity", Integer.class);
        query.setParameter("isbn", produs.getIsbn());
        query.setParameter("id", customer.getId());

        List<Integer> result = query.getResultList();
        if (result.isEmpty()) {
            return 0;
        } else {
            return result.get(0);
        }
    }

    @Override
    public Map<String, Integer> getShoppingCartItems(String clientId) {
        return null;
    }

    @Override
    public int getQuantity(String clientId, String isbn) {
        return 0;
    }

    @Override
    public void displayAll(Customer customer) {

    }
}
