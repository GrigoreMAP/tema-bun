package service.customers;

import entities.Customer;

import java.util.Optional;

public interface CustomersService {
    Optional<Customer> get(String email);

    void add(Customer customer);

    void delete(Customer customer);

    void update(Customer customer);
}

