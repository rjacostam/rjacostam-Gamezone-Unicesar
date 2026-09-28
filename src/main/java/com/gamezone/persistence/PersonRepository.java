package com.gamezone.persistence;

import com.gamezone.model.Customer;
import com.gamezone.model.Person;
import com.gamezone.model.Seller;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * CSV persistence for persons.
 * Format: type;name;nationalId;phone;extra1;extra2
 * SELLER extra1=employeeCode extra2=shift, CUSTOMER extra1=email.
 */
public class PersonRepository {
    private final Path file;

    /**
     * Creates a repository.
     * @param file csv file path
     */
    public PersonRepository(Path file) {
        this.file = file;
    }

    /**
     * Loads all persons.
     * @return persons list
     * @throws IOException on read error
     */
    public List<Person> loadAll() throws IOException {
        List<Person> result = new ArrayList<>();
        if (!Files.exists(file)) {
            return result;
        }
        for (String line : Files.readAllLines(file)) {
            if (line.isBlank() || line.startsWith("type;")) {
                continue;
            }
            String[] p = line.split(";", -1);
            if (p.length < 5) {
                continue;
            }
            String type = p[0].trim();
            if ("SELLER".equalsIgnoreCase(type)) {
                result.add(new Seller(p[1], p[2], p[3], p[4], p.length > 5 ? p[5] : "MORNING"));
            } else {
                result.add(new Customer(p[1], p[2], p[3], p[4]));
            }
        }
        return result;
    }

    /**
     * Saves all persons.
     * @param persons persons list
     * @throws IOException on write error
     */
    public void saveAll(List<Person> persons) throws IOException {
        StringBuilder sb = new StringBuilder("type;name;id;phone;extra1;extra2\n");
        for (Person person : persons) {
            if (person instanceof Seller) {
                Seller s = (Seller) person;
                sb.append("SELLER;").append(s.getName()).append(";").append(s.getNationalId())
                  .append(";").append(s.getPhone()).append(";").append(s.getEmployeeCode())
                  .append(";").append(s.getShift()).append("\n");
            } else if (person instanceof Customer) {
                Customer c = (Customer) person;
                sb.append("CUSTOMER;").append(c.getName()).append(";").append(c.getNationalId())
                  .append(";").append(c.getPhone()).append(";").append(c.getEmail())
                  .append(";\n");
            }
        }
        if (file.getParent() != null) {
            Files.createDirectories(file.getParent());
        }
        Files.writeString(file, sb.toString());
    }
}
