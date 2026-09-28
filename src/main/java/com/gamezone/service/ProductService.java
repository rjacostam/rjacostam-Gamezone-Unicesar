package com.gamezone.service;

import com.gamezone.model.Console;
import com.gamezone.model.Product;
import com.gamezone.model.VideoGame;
import com.gamezone.persistence.ProductRepository;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Product service with register and search operations.
 */
public class ProductService {
    private final ProductRepository repository;
    private final List<Product> products;

    /**
     * Creates a service loading persisted data.
     * @param repository repository
     */
    public ProductService(ProductRepository repository) {
        this.repository = repository;
        try {
            this.products = new ArrayList<>(repository.loadAll());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Registers a video game.
     * @param id identifier
     * @param title title
     * @param price unit price
     * @param stock initial stock
     * @param platform platform
     * @param genre genre
     * @param ageRating age rating
     */
    public void registerVideoGame(String id, String title, double price, int stock,
                                  String platform, String genre, String ageRating) {
        register(new VideoGame(id, title, price, stock, platform, genre, ageRating));
    }

    /**
     * Registers a console.
     * @param id identifier
     * @param title title
     * @param price unit price
     * @param stock initial stock
     * @param brand brand
     * @param modelName model name
     * @param generation generation
     */
    public void registerConsole(String id, String title, double price, int stock,
                                String brand, String modelName, String generation) {
        register(new Console(id, title, price, stock, brand, modelName, generation));
    }

    private void register(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product is required");
        }
        if (findById(product.getId()) != null) {
            throw new IllegalArgumentException("Duplicate id: " + product.getId());
        }
        products.add(product);
        persist();
    }

    /** @return all products copy */
    public List<Product> listAll() {
        return new ArrayList<>(products);
    }

    /**
     * Finds by id.
     * @param id id
     * @return product or null
     */
    public Product findById(String id) {
        for (Product p : products) {
            if (p.getId().equals(id)) {
                return p;
            }
        }
        return null;
    }

    private void persist() {
        try {
            repository.saveAll(products);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
