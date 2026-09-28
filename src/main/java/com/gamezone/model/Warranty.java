package com.gamezone.model;

import java.time.LocalDate;

/**
 * Base class for warranties.
 * Holds direct references to the associated product and sale (R4).
 * Persistence stores only their identifiers (A2).
 */
public abstract class Warranty {
    private String warrantyId;
    private Product product;
    private Sale sale;
    private LocalDate startDate;
    private LocalDate endDate;

    /**
     * Creates a warranty. The end date is computed automatically
     * from the duration reported by the concrete subclass.
     * @param warrantyId warranty identifier
     * @param product associated product
     * @param sale associated sale
     * @param startDate warranty start date (sale date)
     */
    public Warranty(String warrantyId, Product product, Sale sale, LocalDate startDate) {
        if (warrantyId == null || warrantyId.isBlank()) {
            throw new IllegalArgumentException("Warranty id is required");
        }
        this.warrantyId = warrantyId;
        this.product = product;
        this.sale = sale;
        this.startDate = startDate;
        this.endDate = startDate != null ? startDate.plusMonths(getDurationInMonths()) : null;
    }

    /** @return warranty identifier */
    public String getWarrantyId() { return warrantyId; }

    /** @return associated product */
    public Product getProduct() { return product; }

    /** @return associated sale */
    public Sale getSale() { return sale; }

    /** @return start date */
    public LocalDate getStartDate() { return startDate; }

    /** @return end date */
    public LocalDate getEndDate() { return endDate; }

    /**
     * @return warranty duration in months
     */
    public abstract int getDurationInMonths();

    /**
     * @return warranty type name
     */
    public abstract String getWarrantyType();

    /**
     * @return additional cost added to the sale total
     */
    public abstract double getAdditionalCost();
}
