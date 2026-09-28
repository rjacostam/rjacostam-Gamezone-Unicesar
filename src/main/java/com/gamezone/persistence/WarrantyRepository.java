package com.gamezone.persistence;

import com.gamezone.model.BasicWarranty;
import com.gamezone.model.ExtendedWarranty;
import com.gamezone.model.Warranty;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * CSV persistence for warranties.
 * Stores only productId (String) without Product reference (fix A2).
 * Format: type;id;productId;startDate;additionalCost
 */
public class WarrantyRepository {
    private final Path file;

    /**
     * Creates a repository.
     * @param file csv file path
     */
    public WarrantyRepository(Path file) {
        this.file = file;
    }

    /**
     * Loads all warranties.
     * @return warranties list
     * @throws IOException on read error
     */
    public List<Warranty> loadAll() throws IOException {
        List<Warranty> result = new ArrayList<>();
        if (!Files.exists(file)) {
            return result;
        }
        for (String line : Files.readAllLines(file)) {
            if (line.isBlank() || line.startsWith("type;")) {
                continue;
            }
            String[] p = line.split(";", -1);
            if (p.length < 4) {
                continue;
            }
            LocalDate start = LocalDate.parse(p[3]);
            if ("BASIC".equalsIgnoreCase(p[0].trim())) {
                result.add(new BasicWarranty(p[1], p[2], start));
            } else if ("EXTENDED".equalsIgnoreCase(p[0].trim())) {
                double cost = p.length > 4 && !p[4].isBlank() ? Double.parseDouble(p[4]) : 0;
                result.add(new ExtendedWarranty(p[1], p[2], start, cost));
            }
        }
        return result;
    }

    /**
     * Saves all warranties.
     * @param warranties warranties list
     * @throws IOException on write error
     */
    public void saveAll(List<Warranty> warranties) throws IOException {
        StringBuilder sb = new StringBuilder("type;id;productId;startDate;additionalCost\n");
        for (Warranty w : warranties) {
            sb.append(w.getWarrantyType()).append(";").append(w.getId()).append(";")
              .append(w.getProductId()).append(";").append(w.getStartDate().toString())
              .append(";").append(w.getAdditionalCost()).append("\n");
        }
        if (file.getParent() != null) {
            Files.createDirectories(file.getParent());
        }
        Files.writeString(file, sb.toString());
    }
}
