package com.gamezone.ui;

import com.gamezone.model.Product;
import com.gamezone.model.Sale;
import com.gamezone.service.AccessoryService;
import com.gamezone.service.PersonService;
import com.gamezone.service.ProductService;
import com.gamezone.service.PromotionService;
import com.gamezone.service.ReturnService;
import com.gamezone.service.SaleService;
import com.gamezone.service.WarrantyService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Console menu integrating all modules.
 */
public class ConsoleMenu {
    private final PersonService personService;
    private final ProductService productService;
    private final AccessoryService accessoryService;
    private final PromotionService promotionService;
    private final SaleService saleService;
    private final ReturnService returnService;
    private final WarrantyService warrantyService;
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Creates the menu with services.
     * @param personService person service
     * @param productService product service
     * @param accessoryService accessory service
     * @param promotionService promotion service
     * @param saleService sale service
     * @param returnService return service
     * @param warrantyService warranty service
     */
    public ConsoleMenu(PersonService personService, ProductService productService,
            AccessoryService accessoryService, PromotionService promotionService,
            SaleService saleService, ReturnService returnService, WarrantyService warrantyService) {
        this.personService = personService;
        this.productService = productService;
        this.accessoryService = accessoryService;
        this.promotionService = promotionService;
        this.saleService = saleService;
        this.returnService = returnService;
        this.warrantyService = warrantyService;
    }

    /** Starts the main loop. */
    public void start() {
        while (true) {
            System.out.println("=== GameZone Unicesar ===");
            System.out.println("1. Productos 2. Personas 3. Ventas 4. Accesorios 5. Promociones 6. Devoluciones 7. Garantias 0. Salir");
            String opt = scanner.nextLine();
            try {
                switch (opt) {
                    case "1": productMenu(); break;
                    case "2": personMenu(); break;
                    case "3": saleMenu(); break;
                    case "4": accessoryMenu(); break;
                    case "5": promotionMenu(); break;
                    case "6": returnMenu(); break;
                    case "7": warrantyMenu(); break;
                    case "0": return;
                    default: System.out.println("Opcion invalida");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void productMenu() throws Exception {
        System.out.println("1. Registrar videojuego 2. Registrar consola 3. Listar productos");
        String o = scanner.nextLine();
        if (o.equals("1")) {
            System.out.println("id titulo precio stock plataforma genero edad");
            String[] p = scanner.nextLine().split(";");
            productService.registerVideoGame(p[0], p[1], Double.parseDouble(p[2]),
                    Integer.parseInt(p[3]), p[4], p[5], p[6]);
            System.out.println("Videojuego registrado");
        } else if (o.equals("2")) {
            System.out.println("id titulo precio stock marca modelo generacion");
            String[] p = scanner.nextLine().split(";");
            productService.registerConsole(p[0], p[1], Double.parseDouble(p[2]),
                    Integer.parseInt(p[3]), p[4], p[5], p[6]);
            System.out.println("Consola registrada");
        } else {
            for (Product p : productService.listAll()) {
                System.out.println(p.getId() + " " + p.getDescription() + " stock:" + p.getStock());
            }
        }
    }

    private void personMenu() throws Exception {
        System.out.println("1. Registrar cliente 2. Listar clientes 3. Listar vendedores");
        String o = scanner.nextLine();
        if (o.equals("1")) {
            System.out.println("nombre;id;telefono;email");
            String[] p = scanner.nextLine().split(";");
            personService.registerCustomer(p[0], p[1], p[2], p[3]);
            System.out.println("Cliente registrado");
        } else if (o.equals("2")) {
            personService.listCustomers().forEach(c -> System.out.println(c.getNationalId() + " " + c.getName()));
        } else {
            personService.listSellers().forEach(s -> System.out.println(s.getNationalId() + " " + s.getName()));
        }
    }

    private void saleMenu() throws Exception {
        System.out.println("1. Registrar venta 2. Historial 3. Por cliente 4. Por vendedor");
        String o = scanner.nextLine();
        if (o.equals("1")) {
            System.out.println("saleId;customerId;sellerId;itemIds(coma);consolasConExtendida(coma o vacio)");
            String[] p = scanner.nextLine().split(";", -1);
            List<String> items = Arrays.asList(p[3].split(","));
            List<String> ext = p.length > 4 && !p[4].isEmpty() ? Arrays.asList(p[4].split(",")) : new ArrayList<>();
            Sale s = saleService.registerSale(p[0], p[1], p[2], items, ext);
            System.out.println(s.generateReceipt());
        } else if (o.equals("2")) {
            saleService.listAll().forEach(s -> System.out.println(s.generateReceipt()));
        } else if (o.equals("3")) {
            System.out.println("id cliente:");
            saleService.listByCustomer(scanner.nextLine()).forEach(s -> System.out.println(s.generateReceipt()));
        } else {
            System.out.println("codigo vendedor:");
            saleService.listBySeller(scanner.nextLine()).forEach(s -> System.out.println(s.generateReceipt()));
        }
    }

    private void accessoryMenu() throws Exception {
        System.out.println("1. Control 2. Cable 3. Memoria 4. Listar 5. Por tipo 6. Compatibles consola");
        String o = scanner.nextLine();
        if (o.equals("1")) {
            System.out.println("id;titulo;precio;stock;conexion;consolasCompat(coma)");
            String[] p = scanner.nextLine().split(";", -1);
            accessoryService.registerController(p[0], p[1], Double.parseDouble(p[2]),
                    Integer.parseInt(p[3]), p[4], Arrays.asList(p[5].split(",")));
            System.out.println("Control registrado");
        } else if (o.equals("2")) {
            System.out.println("id;titulo;precio;stock;longitud;conector");
            String[] p = scanner.nextLine().split(";");
            accessoryService.registerCable(p[0], p[1], Double.parseDouble(p[2]),
                    Integer.parseInt(p[3]), Double.parseDouble(p[4]), p[5], new ArrayList<>());
            System.out.println("Cable registrado");
        } else if (o.equals("3")) {
            System.out.println("id;titulo;precio;stock;capacidad;tipo");
            String[] p = scanner.nextLine().split(";");
            accessoryService.registerMemory(p[0], p[1], Double.parseDouble(p[2]),
                    Integer.parseInt(p[3]), Integer.parseInt(p[4]), p[5], new ArrayList<>());
            System.out.println("Memoria registrada");
        } else if (o.equals("4")) {
            accessoryService.listAll().forEach(a -> System.out.println(a.getId() + " " + a.getDescription()));
        } else if (o.equals("5")) {
            System.out.println("tipo:");
            accessoryService.listByType(scanner.nextLine()).forEach(a -> System.out.println(a.getId() + " " + a.getDescription()));
        } else {
            System.out.println("id consola:");
            accessoryService.findCompatibleWith(scanner.nextLine()).forEach(a -> System.out.println(a.getId() + " " + a.getDescription()));
        }
    }

    private void promotionMenu() throws Exception {
        System.out.println("1. Porcentaje 2. Categoria 3. Volumen 4. Todas 5. Vigentes");
        String o = scanner.nextLine();
        if (o.equals("4")) {
            promotionService.listAll().forEach(p -> System.out.println(p.getId() + " " + p.getName()));
        } else if (o.equals("5")) {
            promotionService.listActive().forEach(p -> System.out.println(p.getId() + " " + p.getName()));
        } else {
            System.out.println("id;nombre;inicio(yyyy-MM-dd);fin;porcentaje;extra(categoria o minQty)");
            String[] p = scanner.nextLine().split(";");
            if (o.equals("1")) {
                promotionService.registerPercentageDiscount(p[0], p[1], LocalDate.parse(p[2]),
                        LocalDate.parse(p[3]), Double.parseDouble(p[4]));
            } else if (o.equals("2")) {
                promotionService.registerCategoryDiscount(p[0], p[1], LocalDate.parse(p[2]),
                        LocalDate.parse(p[3]), Double.parseDouble(p[4]), p[5]);
            } else {
                promotionService.registerBulkPurchaseDiscount(p[0], p[1], LocalDate.parse(p[2]),
                        LocalDate.parse(p[3]), Integer.parseInt(p[5]), Double.parseDouble(p[4]));
            }
            System.out.println("Promocion registrada");
        }
    }

    private void returnMenu() throws Exception {
        System.out.println("1. Registrar 2. Todas 3. Por cliente 4. Por venta 5. Balance mensual");
        String o = scanner.nextLine();
        if (o.equals("1")) {
            System.out.println("saleId;itemIds(coma);motivo");
            String[] p = scanner.nextLine().split(";", -1);
            System.out.println(returnService.registerReturn(p[0], Arrays.asList(p[1].split(",")), p[2]).generateReturnReceipt());
        } else if (o.equals("2")) {
            returnService.viewAll().forEach(r -> System.out.println(r.generateReturnReceipt()));
        } else if (o.equals("3")) {
            System.out.println("id cliente:");
            returnService.viewByCustomer(scanner.nextLine()).forEach(r -> System.out.println(r.generateReturnReceipt()));
        } else if (o.equals("4")) {
            System.out.println("id venta:");
            returnService.viewBySale(scanner.nextLine()).forEach(r -> System.out.println(r.generateReturnReceipt()));
        } else {
            System.out.println("mes anio:");
            String[] p = scanner.nextLine().split(" ");
            System.out.println(returnService.generateMonthlyBalance(Integer.parseInt(p[0]), Integer.parseInt(p[1])));
        }
    }

    private void warrantyMenu() throws Exception {
        System.out.println("1. Por producto/venta 2. Todas 3. Vigentes 4. Proximas a vencer");
        String o = scanner.nextLine();
        if (o.equals("1")) {
            System.out.println("productId saleId:");
            String[] p = scanner.nextLine().split(" ");
            System.out.println(warrantyService.findWarrantyByProduct(p[0], p[1]).generateWarrantyCertificate());
        } else if (o.equals("2")) {
            warrantyService.listAll().forEach(w -> System.out.println(w.generateWarrantyCertificate()));
        } else if (o.equals("3")) {
            warrantyService.listActive().forEach(w -> System.out.println(w.generateWarrantyCertificate()));
        } else {
            System.out.println("dias:");
            warrantyService.listExpiringSoon(Integer.parseInt(scanner.nextLine()))
                    .forEach(w -> System.out.println(w.generateWarrantyCertificate()));
        }
    }
}
