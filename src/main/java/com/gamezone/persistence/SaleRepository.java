package com.gamezone.persistence;

import com.gamezone.model.Accessory;
import com.gamezone.model.Customer;
import com.gamezone.model.Person;
import com.gamezone.model.Product;
import com.gamezone.model.Sale;
import com.gamezone.model.Seller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Persists sales to a semicolon-separated CSV file storing only identifiers.
 * Format: saleId;date;customerId;sellerId;itemIds;subtotal;discount;promoName;warrantyCost;total
 * Item ids are comma-separated. References are resolved on load through the
 * injected person, product and accessory repositories.
 */
public class SaleRepository {
    private final String filePath;
    private final PersonRepository personRepository;
    private final ProductRepository productRepository;
    private final AccessoryRepository accessoryRepository;

    /**
     * Creates a repository using the default data file.
     */
    public SaleRepository() {
        this(new PersonRepository(), new ProductRepository(), new AccessoryRepository());
    }

    /**
     * Creates a repository with constructor injection of its collaborators.
     *
     * @param personRepository repository used to resolve customers and sellers
     * @param productRepository repository used to resolve video games and consoles
     * @param accessoryRepository repository used to resolve accessories
     */
    public SaleRepository(PersonRepository personRepository, ProductRepository productRepository,
                          AccessoryRepository accessoryRepository) {
        this("data/sales.csv", personRepository, productRepository, accessoryRepository);
    }

    /**
     * Creates a repository with an explicit storage path and collaborators.
     *
     * @param filePath CSV file path
     * @param personRepository repository used to resolve customers and sellers
     * @param productRepository repository used to resolve video games and consoles
     * @param accessoryRepository repository used to resolve accessories
     */
    public SaleRepository(String filePath, PersonRepository personRepository,
                          ProductRepository productRepository,
                          AccessoryRepository accessoryRepository) {
        this.filePath = filePath;
        this.personRepository = personRepository;
        this.productRepository = productRepository;
        this.accessoryRepository = accessoryRepository;
    }

    /**
     * Saves all sales, replacing the file content.
     *
     * @param sales sales to save
     * @throws IOException if the file cannot be written
     */
    public void saveAll(List<Sale> sales) throws IOException {
        Path path = Paths.get(filePath);
        if (path.getParent() != null) {
            Files.createDirectories(path.getParent());
        }
        List<String> lines = new ArrayList<>();
        for (Sale sale : sales) {
            lines.add(toLine(sale));
        }
        Files.write(path, lines, StandardCharsets.UTF_8);
    }

    /**
     * Loads all sales, resolving customer, seller and item references.
     * Lines with unresolvable references are skipped.
     *
     * @return all sales, or an empty list if the file is missing
     */
    public List<Sale> loadAll() {
        Path path = Paths.get(filePath);
        List<Sale> result = new ArrayList<>();
        if (!Files.exists(path)) {
            return result;
        }
        Map<String, Person> people = new HashMap<>();
        for (Person person : personRepository.loadAll()) {
            people.put(person.getNationalId(), person);
        }
        Map<String, Product> products = new HashMap<>();
        for (Product product : productRepository.loadAll()) {
            products.put(product.getId(), product);
        }
        for (Accessory accessory : accessoryRepository.loadAll()) {
            products.put(accessory.getId(), accessory);
        }
        try {
            for (String line : Files.readAllLines(path, StandardCharsets.UTF_8)) {
                if (line == null || line.trim().isEmpty()) {
                    continue;
                }
                Sale sale = fromLine(line, people, products);
                if (sale != null) {
                    result.add(sale);
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Could not read " + filePath, e);
        }
        return result;
    }

    /**
     * Finds a sale by its identifier.
     *
     * @param id sale identifier
     * @return matching sale, or null if absent
     */
    public Sale findById(String id) {
        if (id == null) {
            return null;
        }
        for (Sale sale : loadAll()) {
            if (id.equals(sale.getId())) {
                return sale;
            }
        }
        return null;
    }

    /**
     * Converts a sale to its CSV line, storing only identifiers.
     *
     * @param sale sale to convert
     * @return CSV line
     */
    private String toLine(Sale sale) {
        String itemIds = sale.getItems().stream()
                .map(Product::getId)
                .collect(Collectors.joining(","));
        String promoName = sale.getAppliedPromotionName() == null ? "" : sale.getAppliedPromotionName();
        return sale.getId() + ";" + sale.getDate()
                + ";" + sale.getCustomer().getNationalId()
                + ";" + sale.getSeller().getNationalId()
                + ";" + itemIds
                + ";" + sale.getSubtotal() + ";" + sale.getDiscountAmount()
                + ";" + promoName + ";" + sale.getWarrantyCost() + ";" + sale.getTotal();
    }

    /**
     * Parses one CSV line into a sale.
     *
     * @param line CSV line
     * @param people people indexed by national id
     * @param products products indexed by id
     * @return parsed sale, or null if references cannot be resolved
     */
    private Sale fromLine(String line, Map<String, Person> people, Map<String, Product> products) {
        String[] parts = line.split(";", -1);
        if (parts.length != 10) {
            return null;
        }
        try {
            Person customer = people.get(parts[2].trim());
            Person seller = people.get(parts[3].trim());
            if (!(customer instanceof Customer) || !(seller instanceof Seller)) {
                return null;
            }
            List<Product> items = new ArrayList<>();
            String itemIds = parts[4].trim();
            if (!itemIds.isEmpty()) {
                for (String itemId : itemIds.split(",")) {
                    Product item = products.get(itemId.trim());
                    if (item == null) {
                        return null;
                    }
                    items.add(item);
                }
            }
            String promoName = parts[7].trim();
            return new Sale(parts[0].trim(), LocalDate.parse(parts[1].trim()),
                    (Customer) customer, (Seller) seller, items,
                    Double.parseDouble(parts[5].trim()), Double.parseDouble(parts[6].trim()),
                    promoName.isEmpty() ? null : promoName,
                    Double.parseDouble(parts[8].trim()), Double.parseDouble(parts[9].trim()));
        } catch (Exception e) {
            return null;
        }
    }
}
