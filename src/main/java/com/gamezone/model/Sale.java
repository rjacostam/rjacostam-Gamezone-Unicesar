package com.gamezone.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Completed sale of one or more products to a customer.
 */
public class Sale {
    private String id;
    private LocalDate date;
    private Customer customer;
    private Seller seller;
    private List<Product> items;
    private double subtotal;
    private double discountAmount;
    private String appliedPromotionName;
    private double warrantyCost;
    private double total;

    /**
     * Creates a sale.
     *
     * @param id sale identifier
     * @param date sale date
     * @param customer buying customer
     * @param seller selling employee
     * @param items sold products
     * @param subtotal sum of item prices before discounts
     * @param discountAmount applied discount amount
     * @param appliedPromotionName applied promotion name, may be null
     * @param warrantyCost extra warranty cost
     * @param total final charged amount
     */
    public Sale(String id, LocalDate date, Customer customer, Seller seller, List<Product> items,
                double subtotal, double discountAmount, String appliedPromotionName,
                double warrantyCost, double total) {
        this.id = id;
        this.date = date;
        this.customer = customer;
        this.seller = seller;
        this.items = items != null ? new ArrayList<>(items) : new ArrayList<>();
        this.subtotal = subtotal;
        this.discountAmount = discountAmount;
        this.appliedPromotionName = appliedPromotionName;
        this.warrantyCost = warrantyCost;
        this.total = total;
    }

    /** @return sale identifier */
    public String getId() { return id; }

    /** @return sale date */
    public LocalDate getDate() { return date; }

    /** @param date sale date */
    public void setDate(LocalDate date) { this.date = date; }

    /** @return buying customer */
    public Customer getCustomer() { return customer; }

    /** @return selling employee */
    public Seller getSeller() { return seller; }

    /** @return sold products */
    public List<Product> getItems() { return new ArrayList<>(items); }

    /** @return sum of item prices before discounts */
    public double getSubtotal() { return subtotal; }

    /** @return applied discount amount */
    public double getDiscountAmount() { return discountAmount; }

    /** @return applied discount amount, alias of getDiscountAmount */
    public double getDiscount() { return discountAmount; }

    /** @return applied promotion name, may be null */
    public String getAppliedPromotionName() { return appliedPromotionName; }

    /** @return extra warranty cost */
    public double getWarrantyCost() { return warrantyCost; }

    /** @return final charged amount */
    public double getTotal() { return total; }

    /**
     * Builds a receipt in Spanish with subtotal, discount and total.
     * @return receipt text
     */
    public String generateReceipt() {
        StringBuilder sb = new StringBuilder();
        sb.append("Venta ").append(id).append(" Fecha: ").append(date).append("\n");
        for (Product p : items) {
            sb.append("- ").append(p.getId()).append(" ").append(p.getTitle())
              .append(" ").append(p.getPrice()).append("\n");
        }
        sb.append("Subtotal: ").append(subtotal).append("\n");
        sb.append("Descuento");
        if (appliedPromotionName != null) {
            sb.append(" (").append(appliedPromotionName).append(")");
        }
        sb.append(": ").append(discountAmount).append("\n");
        sb.append("Costo garantias extendidas: ").append(warrantyCost).append("\n");
        sb.append("Total: ").append(total).append("\n");
        return sb.toString();
    }

    /**
     * Checks whether the sale is still within the return window.
     *
     * @return true if 30 days or fewer passed since the sale date
     */
    public boolean canBeReturned() {
        if (date == null) {
            return false;
        }
        return ChronoUnit.DAYS.between(date, LocalDate.now()) <= 30;
    }
}
