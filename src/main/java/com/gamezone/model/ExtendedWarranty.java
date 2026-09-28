package com.gamezone.model;

import java.time.LocalDate;

/**
 * Extended warranty: 12 months, precomputed additional cost
 * (10 percent of the product price, resolved by the service).
 */
public class ExtendedWarranty extends Warranty {
    private double additionalCost;

    /**
     * Creates an extended warranty.
     * @param id warranty identifier
     * @param productId associated product identifier
     * @param startDate start date
     * @param additionalCost additional cost added to the sale total
     */
    public ExtendedWarranty(String id, String productId, LocalDate startDate, double additionalCost) {
        super(id, productId, startDate);
        this.additionalCost = Math.max(additionalCost, 0);
    }

    @Override
    public int getDurationInMonths() { return 12; }

    @Override
    public String getWarrantyType() { return "EXTENDED"; }

    @Override
    public double getAdditionalCost() { return additionalCost; }
}
