package com.gamezone.model;

import java.time.LocalDate;

/**
 * Base class for promotions. Only one promotion applies per sale:
 * the one with the highest discount.
 */
public abstract class Promotion {
    private String id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;

    /**
     * Creates a promotion.
     * @param id promotion identifier
     * @param name promotion name
     * @param startDate start date (inclusive)
     * @param endDate end date (inclusive)
     */
    public Promotion(String id, String name, LocalDate startDate, LocalDate endDate) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Promotion id is required");
        }
        if (startDate != null && endDate != null && endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date must be on or after start date");
        }
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /** @return promotion identifier */
    public String getId() { return id; }
    /** @return promotion name */
    public String getName() { return name; }
    /** @param name promotion name */
    public void setName(String name) { this.name = name; }
    /** @return start date */
    public LocalDate getStartDate() { return startDate; }
    /** @return end date */
    public LocalDate getEndDate() { return endDate; }

    /**
     * Checks if the promotion is active on the given date.
     * @param date date to check
     * @return true if active
     */
    public boolean isActive(LocalDate date) {
        if (date == null || startDate == null || endDate == null) {
            return false;
        }
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }

    /**
     * Calculates the discount amount for the given sale.
     * @param sale sale to evaluate
     * @return discount amount, >= 0
     */
    public abstract double calculateDiscount(Sale sale);
}
