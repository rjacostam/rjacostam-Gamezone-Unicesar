package com.gamezone.model;

import java.time.LocalDate;

/**
 * Extended warranty: 12 months, 10% of the associated product price.
 */
public class ExtendedWarranty extends Warranty {

    /**
     * Creates an extended warranty.
     * @param warrantyId warranty identifier
     * @param product associated product
     * @param sale associated sale
     * @param startDate start date
     */
    public ExtendedWarranty(String warrantyId, Product product, Sale sale, LocalDate startDate) {
        super(warrantyId, product, sale, startDate);
    }

    @Override
    public int getDurationInMonths() { return 12; }

    @Override
    public String getWarrantyType() { return "Garantía Extendida"; }

    @Override
    public double getAdditionalCost() {
        if (getProduct() == null) {
            return 0;
        }
        return getProduct().getPrice() * 0.10;
    }
}
