package com.gamezone.model;

import java.time.LocalDate;

/**
 * Percentage discount applied to the sale subtotal.
 */
public class PercentageDiscount extends Promotion {
    private double percent;

    /**
     * Creates a percentage discount.
     * @param id promotion identifier
     * @param name promotion name
     * @param startDate start date
     * @param endDate end date
     * @param percent percent (0-100)
     */
    public PercentageDiscount(String id, String name, LocalDate startDate, LocalDate endDate, double percent) {
        super(id, name, startDate, endDate);
        if (percent < 0 || percent > 100) {
            throw new IllegalArgumentException("Percent must be between 0 and 100");
        }
        this.percent = percent;
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
        return sale.getSubtotal() * percent / 100.0;
    }
}
