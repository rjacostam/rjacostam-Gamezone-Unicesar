package com.gamezone.persistence;

import com.gamezone.model.Return;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * CSV persistence for returns.
 * Format: id;date;saleId;productIds(comma);reason;refundAmount
 */
public class ReturnRepository {
    private final Path file;

    /**
     * Creates a repository.
     * @param file csv file path
     */
    public ReturnRepository(Path file) {
        this.file = file;
    }

    /**
     * Loads all returns.
     * @return returns list
     * @throws IOException on read error
     */
    public List<Return> loadAll() throws IOException {
        List<Return> result = new ArrayList<>();
        if (!Files.exists(file)) {
            return result;
        }
        for (String line : Files.readAllLines(file)) {
            if (line.isBlank() || line.startsWith("id;")) {
                continue;
            }
            String[] p = line.split(";", -1);
            if (p.length < 6) {
                continue;
            }
            List<String> ids = p[3].isBlank()
                    ? List.of()
                    : Arrays.asList(p[3].split(","));
            result.add(new Return(p[0], LocalDate.parse(p[1]), p[2], ids, p[4],
                    Double.parseDouble(p[5])));
        }
        return result;
    }

    /**
     * Saves all returns.
     * @param returns returns list
     * @throws IOException on write error
     */
    public void saveAll(List<Return> returns) throws IOException {
        StringBuilder sb = new StringBuilder("id;date;saleId;productIds;reason;refundAmount\n");
        for (Return r : returns) {
            sb.append(r.getId()).append(";").append(r.getDate().toString())
              .append(";").append(r.getSaleId()).append(";")
              .append(String.join(",", r.getProductIds())).append(";")
              .append(r.getReason()).append(";").append(r.getRefundAmount()).append("\n");
        }
        if (file.getParent() != null) {
            Files.createDirectories(file.getParent());
        }
        Files.writeString(file, sb.toString());
    }
}
