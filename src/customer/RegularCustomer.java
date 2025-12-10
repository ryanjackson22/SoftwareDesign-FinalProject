/*
 * 	Author: Ryan Jackson & Ryan Buck
 * 	Created: 12/10/2025
 * 	Final Project - Console Customer Relationship Management Tool
 */

package customer;

public class RegularCustomer extends Customer {
    public RegularCustomer(String name, String email, String phone) {
        super(name, email, phone);
    }

    @Override
    public String toString() {
        return String.format("Name: %s, ID: %d, Email: %s, Phone: %s", getName(), getId(), getEmail(), getPhone());
    }
}
