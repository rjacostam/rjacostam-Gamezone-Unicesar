package com.gamezone.persistence;

import com.gamezone.model.BulkPurchaseDiscount;
import com.gamezone.model.CategoryDiscount;
import com.gamezone.model.PercentageDiscount;
import com.gamezone.model.ProductCategory;
import com.gamezone.model.Promotion;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * CSV persistence for promotions with type discriminator.
 * Format: type;id;name;start;end;percent;targetOrMinQty
 */
public class PromotionRepository {
    private final Path file;

    /**
     * Creates a repository.
     * @param file csv file path
     */
    public PromotionRepository(Path file) {
        this.file = file;
    }

    /**
     * Loads all promotions.
     * @return promotions list
     * @throws IOException on read error
     */
    public List<Promotion> loadAll() throws IOException {
        List<Promotion> result = new ArrayList<>();
        if (!Files.exists(file)) {
            return result;
        }
        for (String line : Files.readAllLines(file)) {
            if (line.isBlank() || line.startsWith("type;")) {
                continue;
            }
            String[] p = line.split(";", -1);
            if (p.length < 6) {
                continue;
            }
            LocalDate start = p[3].isBlank() ? null : LocalDate.parse(p[3]);
            LocalDate end = p[4].isBlank() ? null : LocalDate.parse(p[4]);
            double percent = Double.parseDouble(p[5]);
            String type = p[0].trim();
            if ("PERCENTAGE".equalsIgnoreCase(type)) {
                result.add(new PercentageDiscount(p[1], p[2], start, end, percent));
            } else if ("CATEGORY".equalsIgnoreCase(type)) {
                ProductCategory cat = ProductCategory.valueOf(p[6].trim().toUpperCase());
                result.add(new CategoryDiscount(p[1], p[2], start, end, percent, cat));
            } else if ("BULK".equalsIgnoreCase(type)) {
                result.add(new BulkPurchaseDiscount(p[1], p[2], start, end,
                        Integer.parseInt(p[6].trim()), percent));
            }
        }
        return result;
    }

    /**
     * Saves all promotions.
     * @param promotions promotions list
     * @throws IOException on write error
     */
    public void saveAll(List<Promotion> promotions) throws IOException {
        StringBuilder sb = new StringBuilder("type;id;name;start;end;percent;targetOrMinQty\n");
        for (Promotion promo : promotions) {
            String start = promo.getStartDate() == null ? "" : promo.getStartDate().toString();
            String end = promo.getEndDate() == null ? "" : promo.getEndDate().toString();
            if (promo instanceof PercentageDiscount) {
                PercentageDiscount d = (PercentageDiscount) promo;
                sb.append("PERCENTAGE;").append(d.getId()).append(";").append(d.getName())
                  .append(";").append(start).append(";").append(end)
                  .append(";").append(d.getPercent()).append(";\n");
            } else if (promo instanceof CategoryDiscount) {
                CategoryDiscount d = (CategoryDiscount) promo;
                sb.append("CATEGORY;").append(d.getId()).append(";").append(d.getName())
                  .append(";").append(start).append(";").append(end)
                  .append(";").append(d.getPercent()).append(";")
                  .append(d.getTargetCategory().name()).append("\n");
            } else if (promo instanceof BulkPurchaseDiscount) {
                BulkPurchaseDiscount d = (BulkPurchaseDiscount) promo;
                sb.append("BULK;").append(d.getId()).append(";").append(d.getName())
                  .append(";").append(start).append(";").append(end)
                  .append(";").append(d.getPercent()).append(";")
                  .append(d.getMinQuantity()).append("\n");
            }
        }
        if (file.getParent() != null) {
            Files.createDirectories(file.getParent());
        }
        Files.writeString(file, sb.toString());
    }
}
