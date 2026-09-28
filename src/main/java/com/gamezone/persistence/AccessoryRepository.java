package com.gamezone.persistence;

import com.gamezone.model.Accessory;
import com.gamezone.model.Cable;
import com.gamezone.model.Controller;
import com.gamezone.model.Memory;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * CSV persistence for accessories with type discriminator.
 * Format: type;id;title;price;stock;compatibleConsoles(comma);extra1;extra2
 * CONTROLLER extra1=connectionType, CABLE extra1=length extra2=connectorType,
 * MEMORY extra1=capacityGB extra2=memoryType.
 */
public class AccessoryRepository {
    private final Path file;

    /**
     * Creates a repository.
     * @param file csv file path
     */
    public AccessoryRepository(Path file) {
        this.file = file;
    }

    /**
     * Loads all accessories.
     * @return accessories list
     * @throws IOException on read error
     */
    public List<Accessory> loadAll() throws IOException {
        List<Accessory> result = new ArrayList<>();
        if (!Files.exists(file)) {
            return result;
        }
        for (String line : Files.readAllLines(file)) {
            if (line.isBlank() || line.startsWith("type;")) {
                continue;
            }
            String[] p = line.split(";", -1);
            if (p.length < 7) {
                continue;
            }
            String type = p[0].trim();
            List<String> consoles = p[5].isBlank()
                    ? List.of()
                    : Arrays.asList(p[5].split(","));
            double price = Double.parseDouble(p[3]);
            int stock = Integer.parseInt(p[4]);
            if ("CONTROLLER".equalsIgnoreCase(type)) {
                result.add(new Controller(p[1], p[2], price, stock, consoles, p[6]));
            } else if ("CABLE".equalsIgnoreCase(type)) {
                result.add(new Cable(p[1], p[2], price, stock, consoles,
                        Double.parseDouble(p[6]), p.length > 7 ? p[7] : ""));
            } else if ("MEMORY".equalsIgnoreCase(type)) {
                result.add(new Memory(p[1], p[2], price, stock, consoles,
                        Integer.parseInt(p[6]), p.length > 7 ? p[7] : ""));
            }
        }
        return result;
    }

    /**
     * Saves all accessories.
     * @param accessories accessories list
     * @throws IOException on write error
     */
    public void saveAll(List<Accessory> accessories) throws IOException {
        StringBuilder sb = new StringBuilder("type;id;title;price;stock;consoles;extra1;extra2\n");
        for (Accessory a : accessories) {
            String consoles = String.join(",", a.getCompatibleConsoles());
            if (a instanceof Controller) {
                Controller c = (Controller) a;
                sb.append("CONTROLLER;").append(a.getId()).append(";").append(a.getTitle())
                  .append(";").append(a.getPrice()).append(";").append(a.getStock())
                  .append(";").append(consoles).append(";").append(c.getConnectionType()).append(";\n");
            } else if (a instanceof Cable) {
                Cable c = (Cable) a;
                sb.append("CABLE;").append(a.getId()).append(";").append(a.getTitle())
                  .append(";").append(a.getPrice()).append(";").append(a.getStock())
                  .append(";").append(consoles).append(";").append(c.getLength())
                  .append(";").append(c.getConnectorType()).append("\n");
            } else if (a instanceof Memory) {
                Memory m = (Memory) a;
                sb.append("MEMORY;").append(a.getId()).append(";").append(a.getTitle())
                  .append(";").append(a.getPrice()).append(";").append(a.getStock())
                  .append(";").append(consoles).append(";").append(m.getCapacityGB())
                  .append(";").append(m.getMemoryType()).append("\n");
            }
        }
        if (file.getParent() != null) {
            Files.createDirectories(file.getParent());
        }
        Files.writeString(file, sb.toString());
    }
}
