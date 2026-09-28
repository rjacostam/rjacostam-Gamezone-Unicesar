package com.gamezone.model;

import java.time.LocalDate;

/**
 * Discount applied only to products of a target category.
 * Base version (R2): target category is VIDEOGAME or CONSOLE.
 * Adjustment A1 adds ACCESSORY support on branch feature/accessory-category-discount.
 */
public class CategoryDiscount extends Promotion {
    private double percent;
    private String targetCategory;

    /**
     * Creates a category discount.
     * @param id promotion identifier
     * @param name promotion name
     * @param startDate start date
     * @param endDate end date
     * @param percent percent (0-100)
     * @param targetCategory target category (VIDEOGAME, CONSOLE)
     */
    public CategoryDiscount(String id, String name, LocalDate startDate, LocalDate endDate,
                            double percent, String targetCategory) {
        super(id, name, startDate, endDate);
        if (percent < 0 || percent > 100) {
            throw new IllegalArgumentException("Percent must be between 0 and 100");
        }
        if (!"VIDEOGAME".equals(targetCategory) && !"CONSOLE".equals(targetCategory)) {
            throw new IllegalArgumentException("Target category must be VIDEOGAME or CONSOLE");
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
    public String getTargetCategory() { return targetCategory; }

    /** @param targetCategory target category (VIDEOGAME, CONSOLE) */
    public void setTargetCategory(String targetCategory) {
        if (!"VIDEOGAME".equals(targetCategory) && !"CONSOLE".equals(targetCategory)) {
            throw new IllegalArgumentException("Target category must be VIDEOGAME or CONSOLE");
        }
        this.targetCategory = targetCategory;
    }

    @Override
    public double calculateDiscount(Sale sale) {
        if (sale == null || !isActive(sale.getDate())) {
            return 0;
        }
        double base = 0;
        for (Product p : sale.getItems()) {
            if ("VIDEOGAME".equals(targetCategory) && p instanceof VideoGame) {
                base += p.getPrice();
            } else if ("CONSOLE".equals(targetCategory) && p instanceof Console) {
                base += p.getPrice();
            }
        }
        return base * percent / 100.0;
    }
}
