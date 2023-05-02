package service.shopping_cart;

import entities.Customer;
import entities.Produs;
import exception.InexistentItemException;
import exception.InvalidQuantityException;

import java.util.Map;

public interface ShoppingCartService {
    void addToCart(Customer customer, Produs produs);

    void removeFromCart(Customer customer, Produs produs) throws InexistentItemException;

    void updateQuantity(Customer customer,Produs produs, int quantity) throws InvalidQuantityException, InexistentItemException;

    int getQuantity(Customer customer, Produs produs);
    Map<String, Integer> getShoppingCartItems(String clientId);
    int getQuantity(String clientId, String isbn);

    void displayAll(Customer customer);
}

