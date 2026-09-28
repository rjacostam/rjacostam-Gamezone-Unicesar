package com.gamezone.model;

/**
 * Base class for people interacting with the store.
 * Holds common contact data. Abstract because only concrete roles exist.
 */
public abstract class Person {
    private String name;
    private String nationalId;
    private String phone;

    /**
     * Creates a person.
     * @param name full name, required
     * @param nationalId identification number, required
     * @param phone contact phone, required
     */
    public Person(String name, String nationalId, String phone) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name is required");
        }
        if (nationalId == null || nationalId.isBlank()) {
            throw new IllegalArgumentException("National id is required");
        }
        if (phone == null || phone.isBlank()) {
            throw new IllegalArgumentException("Phone is required");
        }
        this.name = name;
        this.nationalId = nationalId;
        this.phone = phone;
    }

    /** @return full name */
    public String getName() { return name; }
    /** @param name full name, required */
    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name is required");
        }
        this.name = name;
    }
    /** @return identification number */
    public String getNationalId() { return nationalId; }
    /** @return contact phone */
    public String getPhone() { return phone; }
    /** @param phone contact phone, required */
    public void setPhone(String phone) {
        if (phone == null || phone.isBlank()) {
            throw new IllegalArgumentException("Phone is required");
        }
        this.phone = phone;
    }

    /**
     * Short role label implemented by subclasses.
     * @return role description
     */
    public abstract String getRoleLabel();
}
