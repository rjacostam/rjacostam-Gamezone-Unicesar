package com.gamezone.service;

import com.gamezone.model.BulkPurchaseDiscount;
import com.gamezone.model.CategoryDiscount;
import com.gamezone.model.PercentageDiscount;
import com.gamezone.model.Promotion;
import com.gamezone.model.Sale;
import com.gamezone.persistence.PromotionRepository;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Promotion service R2 with best promotion selection.
 * Only one promotion per sale, the one with highest discount.
 */
public class PromotionService {
    private final PromotionRepository repository;
    private final List<Promotion> promotions;

    /**
     * Creates a service loading persisted data.
     * @param repository repository
     */
    public PromotionService(PromotionRepository repository) {
        this.repository = repository;
        try {
            this.promotions = new ArrayList<>(repository.loadAll());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Registers a percentage discount.
     * @param promotion promotion
     */
    public void registerPercentage(PercentageDiscount promotion) {
        register(promotion);
    }

    /**
     * Registers a category discount.
     * @param promotion promotion
     */
    public void registerCategory(CategoryDiscount promotion) {
        register(promotion);
    }

    /**
     * Registers a bulk discount.
     * @param promotion promotion
     */
    public void registerBulk(BulkPurchaseDiscount promotion) {
        register(promotion);
    }

    private void register(Promotion promotion) {
        if (promotion == null) {
            throw new IllegalArgumentException("Promotion is required");
        }
        if (findById(promotion.getId()) != null) {
            throw new IllegalArgumentException("Duplicate id: " + promotion.getId());
        }
        promotions.add(promotion);
        persist();
    }

    /** @return all promotions copy */
    public List<Promotion> listAll() {
        return new ArrayList<>(promotions);
    }

    /**
     * Lists active promotions on date.
     * @param date date
     * @return active list
     */
    public List<Promotion> listActive(LocalDate date) {
        List<Promotion> result = new ArrayList<>();
        for (Promotion p : promotions) {
            if (p.isActive(date)) {
                result.add(p);
            }
        }
        return result;
    }

    /**
     * Finds a promotion by id.
     * @param id id
     * @return promotion or null
     */
    public Promotion findById(String id) {
        for (Promotion p : promotions) {
            if (p.getId().equals(id)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Finds the best promotion for a sale (highest discount among active).
     * @param sale sale
     * @return best promotion or null if none applies
     */
    public Promotion findBestPromotionFor(Sale sale) {
        Promotion best = null;
        double bestValue = 0;
        for (Promotion p : promotions) {
            if (!p.isActive(sale.getDate())) {
                continue;
            }
            double value = p.calculateDiscount(sale);
            if (value > bestValue) {
                bestValue = value;
                best = p;
            }
        }
        return best;
    }

    private void persist() {
        try {
            repository.saveAll(promotions);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
