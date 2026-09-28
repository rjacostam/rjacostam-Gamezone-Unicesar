package com.gamezone.model;

/**
 * Store customer with email contact.
 */
public class Customer extends Person {
    private String email;

    /**
     * Creates a customer.
     * @param name full name
     * @param nationalId identification number
     * @param phone contact phone
     * @param email email, required
     */
    public Customer(String name, String nationalId, String phone, String email) {
        super(name, nationalId, phone);
        if (email == null || email.isBlank() || !email.contains("@")) {
            throw new IllegalArgumentException("Valid email is required");
        }
        this.email = email;
    }

    /** @return email */
    public String getEmail() { return email; }
    /** @param email email, must contain @ */
    public void setEmail(String email) {
        if (email == null || email.isBlank() || !email.contains("@")) {
            throw new IllegalArgumentException("Valid email is required");
        }
        this.email = email;
    }

    @Override
    public String getRoleLabel() { return "CUSTOMER"; }
}
