package com.gamezone;

import com.gamezone.persistence.AccessoryRepository;
import com.gamezone.persistence.PersonRepository;
import com.gamezone.persistence.ProductRepository;
import com.gamezone.persistence.PromotionRepository;
import com.gamezone.persistence.ReturnRepository;
import com.gamezone.persistence.SaleRepository;
import com.gamezone.persistence.WarrantyRepository;
import com.gamezone.service.AccessoryService;
import com.gamezone.service.PersonService;
import com.gamezone.service.ProductService;
import com.gamezone.service.PromotionService;
import com.gamezone.service.ReturnService;
import com.gamezone.service.SaleService;
import com.gamezone.service.WarrantyService;
import com.gamezone.ui.ConsoleMenu;

/**
 * Application entry point wiring all layers.
 */
public class Main {
    /**
     * Starts the system.
     * @param args program arguments
     */
    public static void main(String[] args) {
        PersonRepository personRepo = new PersonRepository();
        ProductRepository productRepo = new ProductRepository();
        AccessoryRepository accessoryRepo = new AccessoryRepository();
        PromotionRepository promotionRepo = new PromotionRepository();
        SaleRepository saleRepo = new SaleRepository(personRepo, productRepo, accessoryRepo);
        WarrantyRepository warrantyRepo = new WarrantyRepository(productRepo, accessoryRepo, saleRepo);
        ReturnRepository returnRepo = new ReturnRepository(saleRepo);

        PersonService personService = new PersonService(personRepo);
        ProductService productService = new ProductService(productRepo);
        AccessoryService accessoryService = new AccessoryService(accessoryRepo);
        PromotionService promotionService = new PromotionService(promotionRepo);
        WarrantyService warrantyService = new WarrantyService(warrantyRepo, saleRepo, productService);
        SaleService saleService = new SaleService(saleRepo, personService, productService,
                accessoryService, promotionService, warrantyService);
        ReturnService returnService = new ReturnService(returnRepo, saleService,
                productService, accessoryService, warrantyService);

        ConsoleMenu menu = new ConsoleMenu(personService, productService, accessoryService,
                promotionService, saleService, returnService, warrantyService);
        menu.start();
    }
}
