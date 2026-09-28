package com.gamezone.model;

import java.util.List;

/**
 * Cable accessory.
 */
public class Cable extends Accessory {
    private double lengthMeters;
    private String connectorType;

    /**
     * Creates a cable.
     * @param id product identifier
     * @param title product title
     * @param price unit price
     * @param stock available stock
     * @param compatibleConsoleIds compatible console ids
     * @param lengthMeters cable length in meters
     * @param connectorType connector type (HDMI, USB-C, Power)
     */
    public Cable(String id, String title, double price, int stock,
                 List<String> compatibleConsoleIds, double lengthMeters, String connectorType) {
        super(id, title, price, stock, compatibleConsoleIds);
        this.lengthMeters = lengthMeters;
        this.connectorType = connectorType;
    }

    /** @return length in meters */
    public double getLengthMeters() { return lengthMeters; }
    /** @param lengthMeters length in meters */
    public void setLengthMeters(double lengthMeters) { this.lengthMeters = lengthMeters; }
    /** @return connector type */
    public String getConnectorType() { return connectorType; }
    /** @param connectorType connector type */
    public void setConnectorType(String connectorType) { this.connectorType = connectorType; }

    @Override
    public String getAccessoryType() { return "CABLE"; }

    @Override
    public String getDescription() {
        return "Cable [" + getId() + "] " + getTitle()
                + " | Length: " + lengthMeters + "m"
                + " | Connector: " + connectorType
                + " | Compatible: " + getCompatibleConsoleIds()
                + " | Price: " + getPrice()
                + " | Stock: " + getStock();
    }
}
