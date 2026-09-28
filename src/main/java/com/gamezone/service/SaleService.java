package com.gamezone.service;

import com.gamezone.model.Accessory;
import com.gamezone.model.Console;
import com.gamezone.model.Customer;
import com.gamezone.model.Person;
import com.gamezone.model.Product;
import com.gamezone.model.Promotion;
import com.gamezone.model.Sale;
import com.gamezone.model.Seller;
import com.gamezone.persistence.SaleRepository;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Unified sale registration integrating accessories, promotions and warranties.
 */
public class SaleService {
    private final SaleRepository saleRepository;
    private final PersonService personService;
    private final ProductService productService;
    private final AccessoryService accessoryService;
    private final PromotionService promotionService;
    private final WarrantyService warrantyService;

    /**
     * Creates the service with constructor injection.
     * @param saleRepository sale repository
     * @param personService person service
     * @param productService product service
     * @param accessoryService accessory service
     * @param promotionService promotion service
     * @param warrantyService warranty service
     */
    public SaleService(SaleRepository saleRepository, PersonService personService,
            ProductService productService, AccessoryService accessoryService,
            PromotionService promotionService, WarrantyService warrantyService) {
        this.saleRepository = saleRepository;
        this.personService = personService;
        this.productService = productService;
        this.accessoryService = accessoryService;
        this.promotionService = promotionService;
        this.warrantyService = warrantyService;
    }

    /**
     * Registers a sale executing the unified flow.
     * @param saleId sale identifier
     * @param customerId customer id
     * @param sellerId seller id
     * @param itemIds product or accessory ids
     * @param extendedIds console ids with extended warranty
     * @return registered sale
     * @throws IOException on persistence error
     */
    public Sale registerSale(String saleId, String customerId, String sellerId,
            List<String> itemIds, List<String> extendedIds) throws IOException {
        // 1. Validate at least one item.
        if (itemIds == null || itemIds.isEmpty()) {
            throw new IllegalArgumentException("Sale requires at least one item");
        }
        Person c = personService.findById(customerId);
        Person s = personService.findById(sellerId);
        if (!(c instanceof Customer) || !(s instanceof Seller)) {
            throw new IllegalArgumentException("Invalid customer or seller");
        }
        // 2. Resolve items and validate stock.
        List<Product> items = new ArrayList<>();
        for (String itemId : itemIds) {
            Product p = productService.findById(itemId);
            if (p == null) {
                Accessory a = accessoryService.findById(itemId);
                if (a == null) {
                    throw new IllegalArgumentException("Unknown item: " + itemId);
                }
                if (a.getStock() < 1) {
                    throw new IllegalArgumentException("Insufficient stock: " + itemId);
                }
                items.add(a);
            } else {
                if (p.getStock() < 1) {
                    throw new IllegalArgumentException("Insufficient stock: " + itemId);
                }
                items.add(p);
            }
        }
        // 3. Subtotal.
        double subtotal = 0;
        for (Product p : items) {
            subtotal += p.getPrice();
        }
        Sale draft = new Sale(saleId, LocalDate.now(), (Customer) c, (Seller) s,
                items, subtotal, 0, null, 0, subtotal);
        // 4. Best promotion on subtotal only.
        double discount = 0;
        String promoName = null;
        Promotion best = promotionService.findBestPromotionFor(draft);
        if (best != null) {
            discount = best.calculateDiscount(draft);
            promoName = best.getName();
        }
        // 5. Warranties: basic for each console + requested extended.
        double warrantyCost = 0;
        List<String> extended = extendedIds == null ? new ArrayList<>() : extendedIds;
        // Persist sale first so warranties can resolve it.
        Sale base = new Sale(saleId, LocalDate.now(), (Customer) c, (Seller) s,
                items, subtotal, discount, promoName, 0, subtotal - discount);
        saveSale(base);
        for (Product p : items) {
            if (p instanceof Console) {
                warrantyService.assignBasicWarranty("W-" + UUID.randomUUID().toString().substring(0, 8),
                        p.getId(), saleId, LocalDate.now());
                if (extended.contains(p.getId())) {
                    com.gamezone.model.ExtendedWarranty ew = warrantyService.assignExtendedWarranty(
                            "WE-" + UUID.randomUUID().toString().substring(0, 8),
                            p.getId(), saleId, LocalDate.now());
                    warrantyCost += ew.getAdditionalCost();
                }
            }
        }
        // 6. Final total.
        double total = subtotal - discount + warrantyCost;
        Sale result = new Sale(saleId, LocalDate.now(), (Customer) c, (Seller) s,
                items, subtotal, discount, promoName, warrantyCost, total);
        // 7. Update inventory.
        for (Product p : items) {
            if (p instanceof Accessory) {
                accessoryService.decreaseStock(p.getId(), 1);
            } else {
                productService.decreaseStock(p.getId(), 1);
            }
        }
        // 8. Persist final sale.
        saveSale(result);
        return result;
    }

    /**
     * Saves a sale replacing same id.
     * @param sale sale
     * @throws IOException on error
     */
    public void saveSale(Sale sale) throws IOException {
        List<Sale> all = saleRepository.loadAll();
        List<Sale> updated = new ArrayList<>();
        for (Sale e : all) {
            if (!sale.getId().equals(e.getId())) {
                updated.add(e);
            }
        }
        updated.add(sale);
        saleRepository.saveAll(updated);
    }

    /** @return all sales */
    public List<Sale> listAll() { return saleRepository.loadAll(); }

    /** @param id id @return sale or null */
    public Sale findById(String id) { return saleRepository.findById(id); }

    /** @param nationalId customer id @return sales */
    public List<Sale> listByCustomer(String nationalId) {
        List<Sale> result = new ArrayList<>();
        for (Sale sale : saleRepository.loadAll()) {
            if (sale.getCustomer() != null && nationalId.equals(sale.getCustomer().getNationalId())) {
                result.add(sale);
            }
        }
        return result;
    }

    /** @param employeeCode seller code @return sales */
    public List<Sale> listBySeller(String employeeCode) {
        List<Sale> result = new ArrayList<>();
        for (Sale sale : saleRepository.loadAll()) {
            if (sale.getSeller() != null && employeeCode.equals(sale.getSeller().getEmployeeCode())) {
                result.add(sale);
            }
        }
        return result;
    }
}
