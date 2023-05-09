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
import java.util.Optional;
import java.util.stream.Collectors;

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

            // leave product open for a total of 12 or multiple of 12 hours
            LocalDateTime now = LocalDateTime.now();
            int hour = now.getHour();
            int remaining = hour % 12;
            int remainingMinutes = 60 - now.getMinute();
            int remainingSeconds = 60 - now.getSecond();
            int remainingMillis = 1000 - now.getNano() / 1000000;
            LocalDateTime endOfOpenness = now.plusHours(remaining == 0 ? 12 : (12 - remaining))
                    .minusMinutes(now.getMinute())
                    .minusSeconds(now.getSecond())
                    .minusNanos(now.getNano())
                    .plusMinutes(remainingMinutes)
                    .plusSeconds(remainingSeconds).plusSeconds(remainingMillis);

            newShoppingCartItem.setEndOfOpenness(endOfOpenness);

            session.merge(newShoppingCartItem);

            customerShoppingCart.getItems().add(newShoppingCartItem);
        }


        transaction.commit();
        session.close();
    }


    @Override
    public void removeFromCart(Customer customer, Produs produs) throws InexistentItemException {
        Session session = this.sessionFactory.openSession();

        Transaction transaction = session.beginTransaction();

        ShoppingCartItem itemToRemove = null;
        for (ShoppingCart cart : customer.getShoppingCarts()) {
            for (ShoppingCartItem item : cart.getItems()) {
                if (item.getProdus().getIsbn().equals(produs.getIsbn())) {
                    itemToRemove = item;
                    break;
                }
            }
            if (itemToRemove != null) {
                break;
            }
        }

        if (itemToRemove == null) {
            throw new InexistentItemException("Product is not in the shopping cart.");
        }

        ShoppingCart cart = itemToRemove.getShoppingCart();
        cart.getItems().remove(itemToRemove);

        session.delete(itemToRemove);

        transaction.commit();
        session.close();
    }

    @Override
    public void updateQuantity(Customer customer, Produs produs, int quantity) throws InvalidQuantityException, InexistentItemException {
        if (quantity < 0) {
            throw new InvalidQuantityException("Quantity cannot be negative.");
        }

        Session session = this.sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        Optional<ShoppingCartItem> shoppingCartItemOptional = getShoppingCartItem(session, customer.getId(), produs.getIsbn());

        if (!shoppingCartItemOptional.isPresent()) {
            throw new InexistentItemException("Item does not exist in the shopping cart.");
        }

        ShoppingCartItem shoppingCartItem = shoppingCartItemOptional.get();
        shoppingCartItem.setQuantity(quantity);
        session.merge(shoppingCartItem);

        transaction.commit();
        session.close();
    }
    private Optional<ShoppingCartItem> getShoppingCartItem(Session session, Integer customerId, String isbn) {
        Query query = session.createQuery("FROM ShoppingCartItem WHERE shoppingCart.customer.id = :customerId AND produs.isbn = :isbn", ShoppingCartItem.class);
        query.setParameter("customerId", customerId);
        query.setParameter("isbn", isbn);
        List<ShoppingCartItem> resultList = query.getResultList();
        if (resultList.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(resultList.get(0));
        }
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

        Session session = this.sessionFactory.openSession();

        Query query = session.createNamedQuery("getShoppingCartItems", Object[].class);
        query.setParameter("id", clientId);

        List<Object[]> results = query.getResultList();
        Map<String, Integer> items = results.stream()
                .collect(Collectors.toMap(
                        row -> (String)row[0], //title
                        row -> (Integer)row[1] //quantity
                ));

        session.close();
        return items;
    }

    @Override
    public int getQuantity(String clientId, String isbn) {
        return 0;
    }

    @Override
    public void displayAll(Customer customer) {
        for (ShoppingCart cart : customer.getShoppingCarts()) {
            System.out.println("Shopping Cart " + cart.getId());
            System.out.println("Last Edited: " + cart.getLastEdited());
            System.out.println("Status: " + cart.getStatus());
            for (ShoppingCartItem item : cart.getItems()) {
                System.out.println("  " + item.getProdus().getTitle() + " x " + item.getQuantity());
            }
        }
    }
}
