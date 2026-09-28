package com.gamezone.model;

import java.util.List;

/**
 * Game controller accessory.
 */
public class Controller extends Accessory {
    private String connectionType;

    /**
     * Creates a controller.
     * @param id product identifier
     * @param title product title
     * @param price unit price
     * @param stock available stock
     * @param compatibleConsoleIds compatible console ids
     * @param connectionType connection type (WIRED, WIRELESS, USB-C)
     */
    public Controller(String id, String title, double price, int stock,
                      List<String> compatibleConsoleIds, String connectionType) {
        super(id, title, price, stock, compatibleConsoleIds);
        this.connectionType = connectionType;
    }

    /** @return connection type */
    public String getConnectionType() { return connectionType; }
    /** @param connectionType connection type */
    public void setConnectionType(String connectionType) { this.connectionType = connectionType; }

    @Override
    public String getAccessoryType() { return "CONTROLLER"; }

    @Override
    public String getDescription() {
        return "Controller [" + getId() + "] " + getTitle()
                + " | Connection: " + connectionType
                + " | Compatible: " + getCompatibleConsoles()
                + " | Price: " + getPrice()
                + " | Stock: " + getStock();
    }
}
