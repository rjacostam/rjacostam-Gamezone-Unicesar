package com.gamezone.service;

import com.gamezone.model.Accessory;
import com.gamezone.model.Cable;
import com.gamezone.model.Controller;
import com.gamezone.model.Memory;
import com.gamezone.persistence.AccessoryRepository;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Accessory service R1 with register, search and stock control.
 * Includes restoreStock for returns (fix A4 covers accessories too).
 */
public class AccessoryService {
    private final AccessoryRepository repository;
    private final List<Accessory> accessories;

    /**
     * Creates a service loading persisted data.
     * @param repository repository
     */
    public AccessoryService(AccessoryRepository repository) {
        this.repository = repository;
        try {
            this.accessories = new ArrayList<>(repository.loadAll());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Registers a controller.
     * @param accessory controller
     */
    public void registerController(Controller accessory) {
        register(accessory);
    }

    /**
     * Registers a cable.
     * @param accessory cable
     */
    public void registerCable(Cable accessory) {
        register(accessory);
    }

    /**
     * Registers a memory accessory.
     * @param accessory memory
     */
    public void registerMemory(Memory accessory) {
        register(accessory);
    }

    private void register(Accessory accessory) {
        if (accessory == null) {
            throw new IllegalArgumentException("Accessory is required");
        }
        if (findById(accessory.getId()) != null) {
            throw new IllegalArgumentException("Duplicate id: " + accessory.getId());
        }
        accessories.add(accessory);
        persist();
    }

    /** @return all accessories copy */
    public List<Accessory> listAll() {
        return new ArrayList<>(accessories);
    }

    /**
     * Lists by type name.
     * @param type CONTROLLER, CABLE or MEMORY
     * @return filtered list
     */
    public List<Accessory> listByType(String type) {
        List<Accessory> result = new ArrayList<>();
        for (Accessory a : accessories) {
            if (a.getClass().getSimpleName().equalsIgnoreCase(type)) {
                result.add(a);
            }
        }
        return result;
    }

    /**
     * Finds accessories compatible with a console.
     * @param consoleId console id
     * @return compatible list
     */
    public List<Accessory> findCompatibleWith(String consoleId) {
        List<Accessory> result = new ArrayList<>();
        for (Accessory a : accessories) {
            if (a.getCompatibleConsoles().contains(consoleId)) {
                result.add(a);
            }
        }
        return result;
    }

    /**
     * Finds by id.
     * @param id id
     * @return accessory or null
     */
    public Accessory findById(String id) {
        for (Accessory a : accessories) {
            if (a.getId().equals(id)) {
                return a;
            }
        }
        return null;
    }

    /**
     * Updates stock by delta.
     * @param id accessory id
     * @param delta delta (negative on sale, positive on return)
     */
    public void updateStock(String id, int delta) {
        Accessory a = findById(id);
        if (a == null) {
            throw new IllegalArgumentException("Accessory not found: " + id);
        }
        int next = a.getStock() + delta;
        if (next < 0) {
            throw new IllegalArgumentException("Insufficient stock for: " + id);
        }
        a.setStock(next);
        persist();
    }

    /**
     * Restores stock on return (fix A4).
     * @param id accessory id
     * @param quantity quantity to restore
     */
    public void restoreStock(String id, int quantity) {
        if (quantity < 1) {
            throw new IllegalArgumentException("Quantity must be >= 1");
        }
        updateStock(id, quantity);
    }

    private void persist() {
        try {
            repository.saveAll(accessories);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
