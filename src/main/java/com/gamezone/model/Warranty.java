package com.gamezone.model;

import java.time.LocalDate;

/**
 * Base class for warranties.
 * Stores only product and sale identifiers so persistence never needs
 * object references (adjustment A2: no SaleService dependency).
 */
public abstract class Warranty {
    private String id;
    private String productId;
    private LocalDate startDate;
    private LocalDate endDate;

    /**
     * Creates a warranty. The end date is computed automatically
     * from the duration reported by the concrete subclass.
     * @param id warranty identifier
     * @param productId associated product identifier
     * @param startDate warranty start date (sale date)
     */
    public Warranty(String id, String productId, LocalDate startDate) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Warranty id is required");
        }
        this.id = id;
        this.productId = productId;
        this.startDate = startDate;
        this.endDate = startDate != null ? startDate.plusMonths(getDurationInMonths()) : null;
    }

    /** @return warranty identifier */
    public String getId() { return id; }

    /** @return associated product identifier */
    public String getProductId() { return productId; }

    /** @return start date */
    public LocalDate getStartDate() { return startDate; }

    /** @return end date */
    public LocalDate getEndDate() { return endDate; }

    /**
     * @return warranty duration in months
     */
    public abstract int getDurationInMonths();

    /**
     * @return warranty type discriminator (BASIC, EXTENDED)
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
        sb.append("Garantia: ").append(id).append(" (").append(getWarrantyType()).append(")\n");
        sb.append("Producto: ").append(productId).append("\n");
        sb.append("Inicio: ").append(startDate).append(" Fin: ").append(endDate).append("\n");
        sb.append("Costo adicional: ").append(getAdditionalCost()).append("\n");
        return sb.toString();
    }
}
