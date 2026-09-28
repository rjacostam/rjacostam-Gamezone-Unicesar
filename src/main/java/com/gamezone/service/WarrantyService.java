package com.gamezone.service;

import com.gamezone.model.BasicWarranty;
import com.gamezone.model.ExtendedWarranty;
import com.gamezone.model.Warranty;
import com.gamezone.persistence.WarrantyRepository;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Warranty service R4 with assign, expiry and cancellation (A7).
 */
public class WarrantyService {
    private final WarrantyRepository repository;
    private final List<Warranty> warranties;

    /**
     * Creates a service loading persisted data.
     * @param repository repository
     */
    public WarrantyService(WarrantyRepository repository) {
        this.repository = repository;
        try {
            this.warranties = new ArrayList<>(repository.loadAll());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Assigns a basic warranty (6 months, cost 0).
     * @param id warranty id
     * @param productId product id
     * @param start start date
     * @return created warranty
     */
    public BasicWarranty assignBasic(String id, String productId, LocalDate start) {
        BasicWarranty w = new BasicWarranty(id, productId, start);
        add(w);
        return w;
    }

    /**
     * Assigns an extended warranty (12 months, 10 percent of price).
     * @param id warranty id
     * @param productId product id
     * @param start start date
     * @param productPrice product price for 10 percent cost
     * @return created warranty
     */
    public ExtendedWarranty assignExtended(String id, String productId, LocalDate start, double productPrice) {
        if (productPrice < 0) {
            throw new IllegalArgumentException("Product price must be >= 0");
        }
        ExtendedWarranty w = new ExtendedWarranty(id, productId, start, productPrice * 0.10);
        add(w);
        return w;
    }

    private void add(Warranty warranty) {
        if (findById(warranty.getId()) != null) {
            throw new IllegalArgumentException("Duplicate id: " + warranty.getId());
        }
        warranties.add(warranty);
        persist();
    }

    /**
     * Finds a warranty by id.
     * @param id id
     * @return warranty or null
     */
    public Warranty findById(String id) {
        for (Warranty w : warranties) {
            if (w.getId().equals(id)) {
                return w;
            }
        }
        return null;
    }

    /**
     * Finds warranties by product.
     * @param productId product id
     * @return matching list
     */
    public List<Warranty> findByProduct(String productId) {
        List<Warranty> result = new ArrayList<>();
        for (Warranty w : warranties) {
            if (w.getProductId().equals(productId)) {
                result.add(w);
            }
        }
        return result;
    }

    /**
     * Lists active warranties on date.
     * @param date date
     * @return active list
     */
    public List<Warranty> listActive(LocalDate date) {
        List<Warranty> result = new ArrayList<>();
        for (Warranty w : warranties) {
            if (w.isActive(date)) {
                result.add(w);
            }
        }
        return result;
    }

    /**
     * Lists warranties expiring within days.
     * @param today today
     * @param days days window
     * @return expiring soon list
     */
    public List<Warranty> listExpiringSoon(LocalDate today, int days) {
        List<Warranty> result = new ArrayList<>();
        for (Warranty w : warranties) {
            LocalDate end = w.getEndDate();
            if (!end.isBefore(today) && !end.isAfter(today.plusDays(days))) {
                result.add(w);
            }
        }
        return result;
    }

    /**
     * Cancels warranties of a product on return (fix A7).
     * @param productId product id
     * @return cancelled count
     */
    public int cancelWarranties(String productId) {
        int count = 0;
        Iterator<Warranty> it = warranties.iterator();
        while (it.hasNext()) {
            if (it.next().getProductId().equals(productId)) {
                it.remove();
                count++;
            }
        }
        if (count > 0) {
            persist();
        }
        return count;
    }

    private void persist() {
        try {
            repository.saveAll(warranties);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
