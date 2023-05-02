package service.producators;

import entities.Producator;

import java.util.Optional;

public class HibernateProducatorsService implements ProducatorsService{
    @Override
    public void add(Producator producator) {

    }

    @Override
    public void delete(Producator producator) {

    }

    @Override
    public void update(Producator producator) {

    }

    @Override
    public Optional<Producator> get(int id) {
        return Optional.empty();
    }

    @Override
    public Optional<Producator> getByName(String name) {
        return Optional.empty();
    }

    @Override
    public Optional<Producator> getByEmail(String email) {
        return Optional.empty();
    }

}
