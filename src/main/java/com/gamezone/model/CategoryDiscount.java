package com.gamezone.model;

import java.time.LocalDate;

/**
 * Discount applied only to products of a target category.
 * Base version (R2): VIDEOGAME or CONSOLE.
 * Adjustment A1 also admits ACCESSORY.
 */
public class CategoryDiscount extends Promotion {
    private double percent;
    private ProductCategory targetCategory;

    /**
     * Creates a category discount.
     * @param id promotion identifier
     * @param name promotion name
     * @param startDate start date
     * @param endDate end date
     * @param percent percent (0-100)
     * @param targetCategory target category
     */
    public CategoryDiscount(String id, String name, LocalDate startDate, LocalDate endDate,
                            double percent, ProductCategory targetCategory) {
        super(id, name, startDate, endDate);
        if (percent < 0 || percent > 100) {
            throw new IllegalArgumentException("Percent must be between 0 and 100");
        }
        if (targetCategory == null) {
            throw new IllegalArgumentException("Target category is required");
        }
        this.percent = percent;
        this.targetCategory = targetCategory;
    }
    /** @return percent */
    public double getPercent() { return percent; }

    /** @param percent percent 0-100 */
    public void setPercent(double percent) {
        if (percent < 0 || percent > 100) {
            throw new IllegalArgumentException("Percent must be between 0 and 100");
        }
        this.percent = percent;
    }

    /** @return target category */
    public ProductCategory getTargetCategory() { return targetCategory; }

    /** @param targetCategory target category */
    public void setTargetCategory(ProductCategory targetCategory) {
        if (targetCategory == null) {
            throw new IllegalArgumentException("Target category is required");
        }
        this.targetCategory = targetCategory;
    }

    /**
     * Resolves the category of a product.
     * @param product product
     * @return category, null when it has no base category
     */
    public static ProductCategory categoryOf(Product product) {
        if (product instanceof VideoGame) {
            return ProductCategory.VIDEOGAME;
        }
        if (product instanceof Console) {
            return ProductCategory.CONSOLE;
        }
        return null;
    }

    @Override
    public double calculateDiscount(Sale sale) {
        if (sale == null || !isActive(sale.getDate())) {
            return 0;
        }
        double base = 0;
        for (Product p : sale.getItems()) {
            if (categoryOf(p) == targetCategory) {
                base += p.getPrice();
            }
        }
        return base * percent / 100.0;
    }
}
