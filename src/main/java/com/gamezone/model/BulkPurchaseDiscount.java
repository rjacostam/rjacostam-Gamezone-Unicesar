package com.gamezone.model;

import java.time.LocalDate;

/**
 * Discount applied when the sale reaches a minimum quantity of items.
 */
public class BulkPurchaseDiscount extends Promotion {
    private int minQuantity;
    private double percent;

    /**
     * Creates a bulk purchase discount.
     * @param id promotion identifier
     * @param name promotion name
     * @param startDate start date
     * @param endDate end date
     * @param minQuantity minimum quantity of items
     * @param percent percent (0-100)
     */
    public BulkPurchaseDiscount(String id, String name, LocalDate startDate, LocalDate endDate,
                                int minQuantity, double percent) {
        super(id, name, startDate, endDate);
        if (minQuantity <= 0) {
            throw new IllegalArgumentException("Min quantity must be > 0");
        }
        if (percent < 0 || percent > 100) {
            throw new IllegalArgumentException("Percent must be between 0 and 100");
        }
        this.minQuantity = minQuantity;
        this.percent = percent;
    }

    /** @return minimum quantity */
    public int getMinQuantity() { return minQuantity; }

    /** @param minQuantity minimum quantity */
    public void setMinQuantity(int minQuantity) {
        if (minQuantity <= 0) {
            throw new IllegalArgumentException("Min quantity must be > 0");
        }
        this.minQuantity = minQuantity;
    }

    /** @return percent */
    public double getPercent() { return percent; }

    /** @param percent percent 0-100 */
    public void setPercent(double percent) {
        if (percent < 0 || percent > 100) {
            throw new IllegalArgumentException("Percent must be between 0 and 100");
        }
        this.percent = percent;
    }

    @Override
    public double calculateDiscount(Sale sale) {
        if (sale == null || !isActive(sale.getDate())) {
            return 0;
        }
        if (sale.getItems().size() < minQuantity) {
            return 0;
        }
        return sale.getSubtotal() * percent / 100.0;
    }
}
