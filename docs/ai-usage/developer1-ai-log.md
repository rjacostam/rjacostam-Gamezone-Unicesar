# Bitacora de uso de IA — Desarrollador 1 (modelo)

## Entrada 1
- Fecha: 2026-09-28
- Herramienta: Muse Spark (agente de codigo OpenCode)
- Fase y rama: Fase 1, `feature/accessory-module`
- Objetivo: Implementar la jerarquia de accesorios del modelo sin romper la base del Taller.
- Consulta: Que clases del modelo le tocan al Desarrollador 1 y contra que API de Sale deben compilar.
- Respuesta: Dev1 implementa Accessory y subclases; el Sale real del repo usa `getItems()` y `getSubtotal()`, y `Sale.canBeReturned()` ya existe.
- Decision: Se acepto disenar Accessory extendiendo Product con lista de consolas compatibles y `getAccessoryType()` como discriminador. Se descarto crear una jerarquia independiente porque duplicaba id, titulo, precio y stock. No se toco `Sale.java` (propiedad del Lider).
- Commit relacionado: feat: add Controller Cable and Memory accessory types

## Entrada 2
- Fecha: 2026-09-28
- Herramienta: Muse Spark (agente de codigo OpenCode)
- Fase y rama: Fase 1, `feature/accessory-module`
- Objetivo: Verificar que el modelo compile contra el `Sale.java` real del repositorio.
- Consulta: Como comprobar compatibilidad si `develop` no compila (faltan Customer y Seller del Desarrollador 2).
- Respuesta: Compilar en carpeta temporal el `Sale.java` real mas los archivos del Dev1 con stubs temporales de Person, Customer y Seller (solo verificacion, no se publican).
- Decision: Se acepto; la verificacion paso con `javac --release 17` sin errores. Los stubs se eliminaron y no entraron a ningun commit.
- Commit relacionado: docs: record accessory module in developer1 AI usage log

## Entrada 3
- Fecha: 2026-09-28
- Herramienta: Muse Spark (agente de codigo OpenCode)
- Fase y rama: Fase 2, `feature/promotion-module`
- Objetivo: Disenar la jerarquia de promociones con calculos distintos por tipo.
- Consulta: Como lograr que cada promocion calcule su descuento sin que el resto del sistema conozca los tipos concretos.
- Respuesta: Clase abstracta Promotion con `isActive(LocalDate)` concreto y `calculateDiscount(Sale)` abstracto; cada subclase lo implementa con `@Override`. La seleccion de la mejor promocion queda en PromotionService (Desarrollador 2), no en Sale ni en el menu.
- Decision: Se acepto. PercentageDiscount aplica sobre `getSubtotal()`; BulkPurchaseDiscount retorna 0 bajo el minimo; CategoryDiscount filtra con `instanceof` sobre `getItems()` (API real del Sale del repo).
- Commit relacionado: feat: add BulkPurchaseDiscount with minimum quantity

## Entrada 4
- Fecha: 2026-09-28
- Herramienta: Muse Spark (agente de codigo OpenCode)
- Fase y rama: Fase 2, `feature/promotion-module`
- Objetivo: Definir el tipo de la categoria objetivo de CategoryDiscount.
- Consulta: String o enum para la categoria (VIDEOGAME, CONSOLE).
- Respuesta: El enunciado R2 fija String con esos dos valores; A1 agregara ACCESSORY en su propia rama.
- Decision: Se acepto String con validacion en constructor y setter. Se descarto el enum para no desviarse del enunciado. El soporte de accesorios queda diferido a `feature/accessory-category-discount`.
- Commit relacionado: docs: document promotion category decision in AI usage log

## Entrada 5
- Fecha: 2026-09-28
- Herramienta: Muse Spark (agente de codigo OpenCode)
- Fase y rama: Fase 3, `feature/warranty-module`
- Objetivo: Disenar la jerarquia de garantias segun R4 sin reintroducir la dependencia circular de A2.
- Consulta: Referencias a objetos (Product, Sale) o solo identificadores en la clase Warranty.
- Respuesta: R4 exige producto y venta asociados con getters; A2 solo cambia persistencia (guardar identificadores) y cableado de servicios, no el modelo. Referencias objeto en el modelo no crean el ciclo.
- Decision: Se acepto Warranty con referencias a Product y Sale, fecha de fin calculada en el constructor con `getDurationInMonths()`, `getAdditionalCost()` sin parametros (10% del precio asociado en la extendida) y certificado en espanol.
- Commit relacionado: feat: add ExtendedWarranty with ten percent cost
