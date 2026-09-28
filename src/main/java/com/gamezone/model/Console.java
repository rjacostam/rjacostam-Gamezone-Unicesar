package com.gamezone.model;

/**
 * Game console product.
 */
public class Console extends Product {
    private String brand;
    private String modelName;
    private String generation;

    /**
     * Creates a console.
     * @param id product identifier
     * @param title product title
     * @param price unit price
     * @param stock available stock
     * @param brand brand (Sony, Microsoft, Nintendo)
     * @param modelName model name
     * @param generation generation
     */
    public Console(String id, String title, double price, int stock,
                   String brand, String modelName, String generation) {
        super(id, title, price, stock);
        this.brand = brand;
        this.modelName = modelName;
        this.generation = generation;
    }

    /** @return brand */
    public String getBrand() { return brand; }
    /** @param brand brand */
    public void setBrand(String brand) { this.brand = brand; }
    /** @return model name */
    public String getModelName() { return modelName; }
    /** @param modelName model name */
    public void setModelName(String modelName) { this.modelName = modelName; }
    /** @return generation */
    public String getGeneration() { return generation; }
    /** @param generation generation */
    public void setGeneration(String generation) { this.generation = generation; }

    @Override
    public String getDescription() {
        return "Console [" + getId() + "] " + getTitle()
                + " | Brand: " + brand
                + " | Model: " + modelName
                + " | Gen: " + generation
                + " | Price: " + getPrice()
                + " | Stock: " + getStock();
    }
}
