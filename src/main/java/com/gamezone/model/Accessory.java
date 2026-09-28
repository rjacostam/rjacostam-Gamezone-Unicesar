package com.gamezone.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for accessories. Extends Product.
 * Holds the list of compatible console ids.
 */
public abstract class Accessory extends Product {
    private List<String> compatibleConsoleIds;

    /**
     * Creates an accessory.
     * @param id product identifier
     * @param title product title
     * @param price unit price
     * @param stock available stock
     * @param compatibleConsoleIds compatible console ids (may be empty)
     */
    public Accessory(String id, String title, double price, int stock, List<String> compatibleConsoleIds) {
        super(id, title, price, stock);
        this.compatibleConsoleIds = compatibleConsoleIds == null
                ? new ArrayList<>()
                : new ArrayList<>(compatibleConsoleIds);
    }

    /**
     * @return copy of compatible console ids
     */
    public List<String> getCompatibleConsoleIds() {
        return new ArrayList<>(compatibleConsoleIds);
    }

    /**
     * @param consoleId console id to add
     */
    public void addCompatibleConsole(String consoleId) {
        if (consoleId != null && !consoleId.isBlank() && !compatibleConsoleIds.contains(consoleId)) {
            compatibleConsoleIds.add(consoleId);
        }
    }

    /**
     * @param consoleId console id to check
     * @return true if compatible
     */
    public boolean isCompatibleWith(String consoleId) {
        return compatibleConsoleIds.contains(consoleId);
    }

    /**
     * Accessory type discriminator used by services: CONTROLLER, CABLE, MEMORY.
     * @return accessory type
     */
    public abstract String getAccessoryType();
}
