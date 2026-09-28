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

    /**
     * Checks coverage on the given date.
     * @param date date to check
     * @return true if covered
     */
    public boolean isActive(LocalDate date) {
        if (date == null || startDate == null || endDate == null) {
            return false;
        }
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }

    /**
     * Generates a printable warranty certificate (Spanish, user-facing).
     * @return certificate text
     */
    public String generateWarrantyCertificate() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== GameZone Unicesar - Certificado de garantia ===\n");
        sb.append("Garantia: ").append(warrantyId).append(" (").append(getWarrantyType()).append(")\n");
        if (product != null) {
            sb.append("Producto: ").append(product.getId()).append(" - ").append(product.getTitle()).append("\n");
        }
        if (sale != null) {
            sb.append("Venta: ").append(sale.getId()).append(" Fecha: ").append(sale.getDate()).append("\n");
        }
        sb.append("Inicio: ").append(startDate).append(" Fin: ").append(endDate).append("\n");
        sb.append("Costo adicional: ").append(getAdditionalCost()).append("\n");
        return sb.toString();
    }
}
