package com.gamezone.service;

import com.gamezone.model.Customer;
import com.gamezone.model.Person;
import com.gamezone.model.Seller;
import com.gamezone.persistence.PersonRepository;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Person service with register and list operations.
 */
public class PersonService {
    private final PersonRepository repository;
    private final List<Person> persons;

    /**
     * Creates a service loading persisted data.
     * @param repository repository
     */
    public PersonService(PersonRepository repository) {
        this.repository = repository;
        try {
            this.persons = new ArrayList<>(repository.loadAll());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Registers a customer.
     * @param customer customer
     */
    public void registerCustomer(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer is required");
        }
        if (findById(customer.getNationalId()) != null) {
            throw new IllegalArgumentException("Duplicate id: " + customer.getNationalId());
        }
        persons.add(customer);
        persist();
    }

    /**
     * Registers a seller.
     * @param seller seller
     */
    public void registerSeller(Seller seller) {
        if (seller == null) {
            throw new IllegalArgumentException("Seller is required");
        }
        if (findById(seller.getNationalId()) != null) {
            throw new IllegalArgumentException("Duplicate id: " + seller.getNationalId());
        }
        persons.add(seller);
        persist();
    }

    /** @return customers list copy */
    public List<Customer> listCustomers() {
        List<Customer> result = new ArrayList<>();
        for (Person p : persons) {
            if (p instanceof Customer) {
                result.add((Customer) p);
            }
        }
        return result;
    }

    /** @return sellers list copy */
    public List<Seller> listSellers() {
        List<Seller> result = new ArrayList<>();
        for (Person p : persons) {
            if (p instanceof Seller) {
                result.add((Seller) p);
            }
        }
        return result;
    }

    /**
     * Finds a person by national id.
     * @param nationalId id
     * @return person or null
     */
    public Person findById(String nationalId) {
        for (Person p : persons) {
            if (p.getNationalId().equals(nationalId)) {
                return p;
            }
        }
        return null;
    }

    private void persist() {
        try {
            repository.saveAll(persons);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
