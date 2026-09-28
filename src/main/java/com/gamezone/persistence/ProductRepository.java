package com.gamezone.persistence;

import com.gamezone.model.Console;
import com.gamezone.model.Product;
import com.gamezone.model.VideoGame;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * CSV persistence for catalog products (video games and consoles).
 * Accessories live in data/accessories.csv.
 * Format: type;id;title;price;stock;extra1;extra2;extra3
 * VIDEOGAME extra1=platform extra2=genre extra3=ageRating,
 * CONSOLE extra1=brand extra2=modelName extra3=generation.
 */
public class ProductRepository {
    private final Path file;

    /**
     * Creates a repository using data/products.csv.
     */
    public ProductRepository() {
        this(Path.of("data/products.csv"));
    }

    /**
     * Creates a repository.
     * @param file csv file path
     */
    public ProductRepository(Path file) {
        this.file = file;
    }

    /**
     * Loads all products.
     * @return products list, empty when the file is missing
     * @throws IOException on read error
     */
    public List<Product> loadAll() throws IOException {
        List<Product> result = new ArrayList<>();
        if (!Files.exists(file)) {
            return result;
        }
        for (String line : Files.readAllLines(file)) {
            if (line.isBlank() || line.startsWith("type;")) {
                continue;
            }
            String[] p = line.split(";", -1);
            if (p.length < 8) {
                continue;
            }
            double price = Double.parseDouble(p[3]);
            int stock = Integer.parseInt(p[4]);
            if ("VIDEOGAME".equalsIgnoreCase(p[0].trim())) {
                result.add(new VideoGame(p[1], p[2], price, stock, p[5], p[6], p[7]));
            } else if ("CONSOLE".equalsIgnoreCase(p[0].trim())) {
                result.add(new Console(p[1], p[2], price, stock, p[5], p[6], p[7]));
            }
        }
        return result;
    }

    /**
     * Saves all products.
     * @param products products list
     * @throws IOException on write error
     */
    public void saveAll(List<Product> products) throws IOException {
        StringBuilder sb = new StringBuilder("type;id;title;price;stock;extra1;extra2;extra3\n");
        for (Product p : products) {
            if (p instanceof VideoGame) {
                VideoGame g = (VideoGame) p;
                sb.append("VIDEOGAME;").append(p.getId()).append(";").append(p.getTitle())
                  .append(";").append(p.getPrice()).append(";").append(p.getStock())
                  .append(";").append(g.getPlatform()).append(";").append(g.getGenre())
                  .append(";").append(g.getAgeRating()).append("\n");
            } else if (p instanceof Console) {
                Console c = (Console) p;
                sb.append("CONSOLE;").append(p.getId()).append(";").append(p.getTitle())
                  .append(";").append(p.getPrice()).append(";").append(p.getStock())
                  .append(";").append(c.getBrand()).append(";").append(c.getModelName())
                  .append(";").append(c.getGeneration()).append("\n");
            }
        }
        if (file.getParent() != null) {
            Files.createDirectories(file.getParent());
        }
        Files.writeString(file, sb.toString());
    }
}
