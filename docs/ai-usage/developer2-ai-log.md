# Dev2 AI log — Jose Cervantes (programador 2, persistence + service)

> Bitacora real de uso de IA. R5 permite IA con bitacora. No falsificar: cada fila corresponde a una consulta real y a un commit.

| Fecha | Herramienta | Fase/Rama | Objetivo | Consulta | Respuesta | Decision | Commit |
|---|---|---|---|---|---|---|---|
| 2026-09-28 | Muse Spark (OpenCode) | base `master` local | Estructurar modulo Personas coherente con Dev1 | Como organizar Person/Customer/Seller + PersonRepository/Service sin romper capas | Model abstracto + CSV `type;name;id;phone;extra` + Service con register/list/findById | Aplicado tal cual | `feat: add abstract Person with contact validation`, `feat: add Customer...`, `feat: add Seller...`, `feat: add PersonRepository...`, `feat: add PersonService...` |
| 2026-09-28 | Muse Spark (OpenCode) | R1 accessory | Persistencia y servicio de accesorios + A4 | Que metodos pide R1 y como restaurar stock en devolucion | AccessoryRepository con discriminador + AccessoryService register x3, listAll, listByType, findCompatibleWith, findById, updateStock/restoreStock | Aplicado, restoreStock delega en updateStock | `feat: add AccessoryRepository...`, `feat: add AccessoryService...` |
| 2026-09-28 | Muse Spark (OpenCode) | R2 promotion | Mejor promo por venta | Como elegir una sola promo con mayor descuento | PromotionRepository con fechas + findBestPromotionFor(Sale) filtrando isActive y max calculateDiscount | Aplicado | `feat: add PromotionRepository...`, `feat: add PromotionService...` |
| 2026-09-28 | Muse Spark (OpenCode) | R4 warranty + A2/A7 | Evitar dependencia circular y cancelar garantias en devolucion | Warranty guarda Product o productId | Solo productId String (A2), cancelWarranties(productId) para A7, assignBasic/Extended, listActive, listExpiringSoon | Aplicado | `fix: add WarrantyRepository without circular dependency`, `feat: add WarrantyService...` |
| 2026-09-28 | Muse Spark (OpenCode) | R3 return + A5/A6 | Reembolso proporcional y balance mensual | Como calcular reembolso con descuento y reporte mensual | Return.calculateRefundAmount proporcional + ReturnService registerReturn valida 30 dias y pertenencia, restaura stock, calculateMonthlySales/Returns, generateMonthlyBalance | Aplicado | `feat: add ReturnRepository...`, `feat: add ReturnService...` |

## Contrato publico para el lider (A3)

- `PersonService(PersonRepository)` : registerCustomer, registerSeller, listCustomers, listSellers, findById
- `AccessoryService(AccessoryRepository)` : registerController/Cable/Memory, listAll, listByType, findCompatibleWith, findById, updateStock, restoreStock
- `PromotionService(PromotionRepository)` : registerPercentage/Category/Bulk, listAll, listActive(date), findBestPromotionFor(Sale)
- `WarrantyService(WarrantyRepository)` : assignBasic, assignExtended(id, productId, start, productPrice), findByProduct, listActive, listExpiringSoon, cancelWarranties
- `ReturnService(ReturnRepository, WarrantyService)` : registerReturn(Sale, productIds, reason, date), viewAll/BySale/ByCustomer, calculateMonthlySales/Returns, generateMonthlyBalance
- `Sale` esperado: `getId, getDate(LocalDate), getCustomer, getSeller, getProducts, getSubtotal, getDiscountAmount, canBeReturned(today)`
- CSV: `data/persons.csv, accessories.csv, promotions.csv, warranties.csv, returns.csv` con headers de este proyecto.
