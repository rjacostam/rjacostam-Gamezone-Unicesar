package com.gamezone.service;

import com.gamezone.model.Product;
import com.gamezone.model.Return;
import com.gamezone.model.Sale;
import com.gamezone.persistence.ReturnRepository;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Return service R3 with partial returns, 30-day window,
 * proportional refund (fix A5), stock restore (fix A4)
 * and monthly balance (fix A6).
 */
public class ReturnService {
    private final ReturnRepository repository;
    private final List<Return> returns;
    private final WarrantyService warrantyService;

    /**
     * Creates a service loading persisted data.
     * @param repository repository
     * @param warrantyService warranty service for A7 cancellation, may be null
     */
    public ReturnService(ReturnRepository repository, WarrantyService warrantyService) {
        this.repository = repository;
        this.warrantyService = warrantyService;
        try {
            this.returns = new ArrayList<>(repository.loadAll());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Registers a partial return.
     * Validates 30-day window, product belonging and restores stock.
     * @param sale sale (must contain products)
     * @param productIds product ids to return
     * @param reason reason
     * @param date return date
     * @return created return
     */
    public Return registerReturn(Sale sale, List<String> productIds, String reason, LocalDate date) {
        if (sale == null) {
            throw new IllegalArgumentException("Sale is required");
        }
        if (productIds == null || productIds.isEmpty()) {
            throw new IllegalArgumentException("At least one product is required");
        }
        if (!sale.canBeReturned(date)) {
            throw new IllegalArgumentException("Return exceeds 30 days for sale: " + sale.getId());
        }
        Map<String, Product> byId = new HashMap<>();
        for (Product p : sale.getProducts()) {
            byId.put(p.getId(), p);
        }
        double refund = 0;
        for (String pid : productIds) {
            Product p = byId.get(pid);
            if (p == null) {
                throw new IllegalArgumentException("Product " + pid + " does not belong to sale");
            }
            refund += Return.calculateRefundAmount(p.getPrice(), sale.getSubtotal(), sale.getDiscountAmount());
            p.setStock(p.getStock() + 1);
            if (warrantyService != null) {
                warrantyService.cancelWarranties(pid);
            }
        }
        String id = "R-" + (returns.size() + 1);
        Return ret = new Return(id, date, sale.getId(), productIds, reason, refund);
        returns.add(ret);
        persist();
        return ret;
    }

    /** @return all returns copy */
    public List<Return> viewAll() {
        return new ArrayList<>(returns);
    }

    /**
     * Views returns by sale.
     * @param saleId sale id
     * @return filtered list
     */
    public List<Return> viewBySale(String saleId) {
        List<Return> result = new ArrayList<>();
        for (Return r : returns) {
            if (r.getSaleId().equals(saleId)) {
                result.add(r);
            }
        }
        return result;
    }

    /**
     * Views returns by customer via sale lookup.
     * @param customerId national id
     * @param sales all sales for lookup
     * @return filtered list
     */
    public List<Return> viewByCustomer(String customerId, List<Sale> sales) {
        Map<String, String> saleToCustomer = new HashMap<>();
        for (Sale s : sales) {
            if (s.getCustomer() != null) {
                saleToCustomer.put(s.getId(), s.getCustomer().getNationalId());
            }
        }
        List<Return> result = new ArrayList<>();
        for (Return r : returns) {
            if (customerId.equals(saleToCustomer.get(r.getSaleId()))) {
                result.add(r);
            }
        }
        return result;
    }

    /**
     * Monthly sales total helper (fix A6).
     * @param sales sales list
     * @param month month
     * @return total
     */
    public double calculateMonthlySales(List<Sale> sales, YearMonth month) {
        double total = 0;
        for (Sale s : sales) {
            if (s.getDate() != null && YearMonth.from(s.getDate()).equals(month)) {
                total += s.getTotal();
            }
        }
        return total;
    }

    /**
     * Monthly returns total helper (fix A6).
     * @param month month
     * @return total refund
     */
    public double calculateMonthlyReturns(YearMonth month) {
        double total = 0;
        for (Return r : returns) {
            if (r.getDate() != null && YearMonth.from(r.getDate()).equals(month)) {
                total += r.getRefundAmount();
            }
        }
        return total;
    }

    /**
     * Monthly balance report.
     * @param sales sales list
     * @param month month
     * @return report text
     */
    public String generateMonthlyBalance(List<Sale> sales, YearMonth month) {
        double salesTotal = calculateMonthlySales(sales, month);
        double returnsTotal = calculateMonthlyReturns(month);
        return "Balance[" + month + "] sales=" + salesTotal
                + " returns=" + returnsTotal
                + " net=" + (salesTotal - returnsTotal);
    }

    private void persist() {
        try {
            repository.saveAll(returns);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
