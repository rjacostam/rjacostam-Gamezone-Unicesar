package com.gamezone.model;

import java.util.List;

/**
 * Memory / storage accessory.
 */
public class Memory extends Accessory {
    private int capacityGB;
    private String memoryType;

    /**
     * Creates a memory accessory.
     * @param id product identifier
     * @param title product title
     * @param price unit price
     * @param stock available stock
     * @param compatibleConsoleIds compatible console ids
     * @param capacityGB capacity in GB
     * @param memoryType memory type (SSD, MicroSD, HDD)
     */
    public Memory(String id, String title, double price, int stock,
                  List<String> compatibleConsoleIds, int capacityGB, String memoryType) {
        super(id, title, price, stock, compatibleConsoleIds);
        this.capacityGB = capacityGB;
        this.memoryType = memoryType;
    }

    /** @return capacity in GB */
    public int getCapacityGB() { return capacityGB; }
    /** @param capacityGB capacity in GB */
    public void setCapacityGB(int capacityGB) { this.capacityGB = capacityGB; }
    /** @return memory type */
    public String getMemoryType() { return memoryType; }
    /** @param memoryType memory type */
    public void setMemoryType(String memoryType) { this.memoryType = memoryType; }

    @Override
    public String getAccessoryType() { return "MEMORY"; }

    @Override
    public String getDescription() {
        return "Memory [" + getId() + "] " + getTitle()
                + " | Capacity: " + capacityGB + "GB"
                + " | Type: " + memoryType
                + " | Compatible: " + getCompatibleConsoleIds()
                + " | Price: " + getPrice()
                + " | Stock: " + getStock();
    }
}
