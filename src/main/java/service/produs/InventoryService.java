package service.produs;

import entities.Produs;
import exception.InexistentItemException;
import exception.ProdusAlreadyExistsException;

import java.util.Optional;

public interface InventoryService {
    void add(Produs produs) throws ProdusAlreadyExistsException;

    void delete(Produs produs) throws  InexistentItemException;

    void update(Produs produs);

    Optional<Produs> searchByTitle(String title) throws InexistentItemException;

    Produs searchByIsbn(String isbn) throws InexistentItemException;

    void displayAll();
}


