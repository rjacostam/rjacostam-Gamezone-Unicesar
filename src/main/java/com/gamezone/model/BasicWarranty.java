package com.gamezone.model;

import java.time.LocalDate;

/**
 * Basic warranty: 6 months, no additional cost.
 */
public class BasicWarranty extends Warranty {

    /**
     * Creates a basic warranty.
     * @param warrantyId warranty identifier
     * @param product associated product
     * @param sale associated sale
     * @param startDate start date
     */
    public BasicWarranty(String warrantyId, Product product, Sale sale, LocalDate startDate) {
        super(warrantyId, product, sale, startDate);
    }

    @Override
    public int getDurationInMonths() { return 6; }

    @Override
    public String getWarrantyType() { return "Garantía Básica"; }

    @Override
    public double getAdditionalCost() { return 0.0; }
}
