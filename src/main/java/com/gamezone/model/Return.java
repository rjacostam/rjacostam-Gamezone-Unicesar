package com.gamezone.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Product return. Partial returns are allowed within 30 days.
 * Stores only the sale identifier and returned product identifiers so
 * persistence never needs object references.
 * Refund is proportional to the original sale discount (adjustment A5):
 * price * (1 - discount / subtotal).
 */
public class Return {
    private String id;
    private LocalDate date;
    private String saleId;
    private List<String> productIds;
    private String reason;
    private double refundAmount;

    /**
     * Creates a return.
     * @param id return identifier
     * @param date return date
     * @param saleId original sale identifier
     * @param productIds returned product identifiers (subset of the sale)
     * @param reason return reason
     * @param refundAmount computed refund amount
     */
    public Return(String id, LocalDate date, String saleId,
                  List<String> productIds, String reason, double refundAmount) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Return id is required");
        }
        if (saleId == null || saleId.isBlank()) {
            throw new IllegalArgumentException("Original sale id is required");
        }
        if (productIds == null || productIds.isEmpty()) {
            throw new IllegalArgumentException("Return requires at least one product");
        }
        if (refundAmount < 0) {
            throw new IllegalArgumentException("Refund amount must be >= 0");
        }
        this.id = id;
        this.date = date;
        this.saleId = saleId;
        this.productIds = new ArrayList<>(productIds);
        this.reason = reason;
        this.refundAmount = refundAmount;
    }

    /** @return return identifier */
    public String getId() { return id; }

    /** @return return date */
    public LocalDate getDate() { return date; }

    /** @return original sale identifier */
    public String getSaleId() { return saleId; }

    /** @return copy of returned product identifiers */
    public List<String> getProductIds() { return new ArrayList<>(productIds); }

    /** @return reason */
    public String getReason() { return reason; }

    /** @return refund amount */
    public double getRefundAmount() { return refundAmount; }

    /**
     * Calculates a proportional item refund (A5):
     * price * (1 - discount / subtotal).
     * @param price item list price
     * @param subtotal original sale subtotal
     * @param discount original sale discount
     * @return refundable amount for the item
     */
    public static double calculateRefundAmount(double price, double subtotal, double discount) {
        return price * (1 - discount / subtotal);
    }

    /**
     * Generates a printable return receipt (Spanish, user-facing).
     * @return receipt text
     */
    public String generateReturnReceipt() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== GameZone Unicesar - Comprobante de devolucion ===\n");
        sb.append("Devolucion: ").append(id).append("\n");
        sb.append("Fecha: ").append(date).append("\n");
        sb.append("Venta original: ").append(saleId).append("\n");
        sb.append("Productos devueltos: ").append(productIds).append("\n");
        sb.append("Motivo: ").append(reason).append("\n");
        sb.append("Valor reembolsado: ").append(refundAmount).append("\n");
        return sb.toString();
    }
}
