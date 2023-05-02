package service.producators;

import entities.Producator;

import java.util.Optional;

public interface ProducatorsService {
    void add(Producator producator);

    void delete(Producator producator);

    void update(Producator producator);

    Optional<Producator> get(int id);

    Optional<Producator> getByName(String name);

    Optional<Producator> getByEmail(String email);
}

