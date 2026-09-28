package com.gamezone.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Product return. Partial returns are allowed within 30 days.
 */
public class Return {
    private String id;
    private LocalDate returnDate;
    private Sale sale;
    private List<Product> returnedProducts;
    private String reason;
    private double refundAmount;

    /**
     * Creates a return.
     * @param id return identifier
     * @param returnDate return date
     * @param sale original sale
     * @param returnedProducts returned products (subset of the sale)
     * @param reason return reason
     */
    public Return(String id, LocalDate returnDate, Sale sale,
                  List<Product> returnedProducts, String reason) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Return id is required");
        }
        if (sale == null) {
            throw new IllegalArgumentException("Original sale is required");
        }
        if (returnedProducts == null || returnedProducts.isEmpty()) {
            throw new IllegalArgumentException("Return requires at least one product");
        }
        this.id = id;
        this.returnDate = returnDate;
        this.sale = sale;
        this.returnedProducts = returnedProducts != null
                ? new ArrayList<>(returnedProducts)
                : new ArrayList<>();
        this.reason = reason;
        this.refundAmount = 0;
    }

    /** @return return identifier */
    public String getId() { return id; }

    /** @return return date */
    public LocalDate getReturnDate() { return returnDate; }

    /** @return original sale */
    public Sale getSale() { return sale; }

    /** @return copy of returned products */
    public List<Product> getReturnedProducts() { return new ArrayList<>(returnedProducts); }

    /** @return reason */
    public String getReason() { return reason; }

    /** @return refund amount */
    public double getRefundAmount() { return refundAmount; }

    /**
     * Calculates the refund as the sum of returned list prices (R3 base).
     * @return refund amount
     */
    public double calculateRefundAmount() {
        double total = 0;
        for (Product p : returnedProducts) {
            total += p.getPrice();
        }
        this.refundAmount = total;
        return total;
    }

    /**
     * Generates a printable return receipt (Spanish, user-facing).
     * @return receipt text
     */
    public String generateReturnReceipt() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== GameZone Unicesar - Comprobante de devolucion ===\n");
        sb.append("Devolucion: ").append(id).append("\n");
        sb.append("Fecha: ").append(returnDate).append("\n");
        sb.append("Venta original: ").append(sale.getId()).append("\n");
        for (Product p : returnedProducts) {
            sb.append("- ").append(p.getId()).append(" ").append(p.getTitle())
              .append(" Precio: ").append(p.getPrice()).append("\n");
        }
        sb.append("Motivo: ").append(reason).append("\n");
        sb.append("Valor reembolsado: ").append(refundAmount).append("\n");
        return sb.toString();
    }
}
