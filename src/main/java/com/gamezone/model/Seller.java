package com.gamezone.model;

/**
 * Store seller with employee data.
 */
public class Seller extends Person {
    private String employeeCode;
    private String shift;

    /**
     * Creates a seller.
     * @param name full name
     * @param nationalId identification number
     * @param phone contact phone
     * @param employeeCode employee code, required
     * @param shift shift (MORNING, AFTERNOON, NIGHT)
     */
    public Seller(String name, String nationalId, String phone, String employeeCode, String shift) {
        super(name, nationalId, phone);
        if (employeeCode == null || employeeCode.isBlank()) {
            throw new IllegalArgumentException("Employee code is required");
        }
        if (shift == null || shift.isBlank()) {
            throw new IllegalArgumentException("Shift is required");
        }
        this.employeeCode = employeeCode;
        this.shift = shift;
    }

    /** @return employee code */
    public String getEmployeeCode() { return employeeCode; }
    /** @return shift */
    public String getShift() { return shift; }
    /** @param shift shift, required */
    public void setShift(String shift) {
        if (shift == null || shift.isBlank()) {
            throw new IllegalArgumentException("Shift is required");
        }
        this.shift = shift;
    }

    @Override
    public String getRoleLabel() { return "SELLER"; }
}
