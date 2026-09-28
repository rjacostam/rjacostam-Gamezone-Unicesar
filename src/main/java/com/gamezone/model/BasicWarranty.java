package com.gamezone.model;

import java.time.LocalDate;

/**
 * Basic warranty: 6 months, no additional cost.
 */
public class BasicWarranty extends Warranty {

    /**
     * Creates a basic warranty.
     * @param id warranty identifier
     * @param productId associated product identifier
     * @param startDate start date
     */
    public BasicWarranty(String id, String productId, LocalDate startDate) {
        super(id, productId, startDate);
    }

    @Override
    public int getDurationInMonths() { return 6; }

    @Override
    public String getWarrantyType() { return "BASIC"; }

    @Override
    public double getAdditionalCost() { return 0.0; }
}
