package com.gamezone.model;

/**
 * Base class for store products with inventory control.
 */
public abstract class Product {
    private String id;
    private String title;
    private double price;
    private int stock;

    /**
     * Creates a product.
     * @param id product identifier
     * @param title product title
     * @param price unit price, must be >= 0
     * @param stock available stock, must be >= 0
     */
    public Product(String id, String title, double price, int stock) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Product id is required");
        }
        if (price < 0) {
            throw new IllegalArgumentException("Price must be >= 0");
        }
        if (stock < 0) {
            throw new IllegalArgumentException("Stock must be >= 0");
        }
        this.id = id;
        this.title = title;
        this.price = price;
        this.stock = stock;
    }

    /** @return product identifier */
    public String getId() { return id; }

    /** @return product title */
    public String getTitle() { return title; }

    /** @param title product title */
    public void setTitle(String title) { this.title = title; }

    /** @return unit price */
    public double getPrice() { return price; }

    /** @param price unit price, must be >= 0 */
    public void setPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Price must be >= 0");
        }
        this.price = price;
    }

    /** @return available stock */
    public int getStock() { return stock; }

    /** @param stock available stock, must be >= 0 */
    public void setStock(int stock) {
        if (stock < 0) {
            throw new IllegalArgumentException("Stock must be >= 0");
        }
        this.stock = stock;
    }

    /**
     * Full description including subclass details.
     * @return description
     */
    public abstract String getDescription();
}
