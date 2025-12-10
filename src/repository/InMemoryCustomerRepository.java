/*
 * 	Author: Ryan Jackson & Ryan Buck
 * 	Created: 12/10/2025
 * 	Final Project - Console Customer Relationship Management Tool
 */

package repository;

import customer.*;

import java.util.ArrayList;
import java.util.List;

public class InMemoryCustomerRepository implements CustomerRepository {
    private final ArrayList<Customer> customers = new ArrayList<>();

    @Override
    public void createCustomer(Customer customer) {
        customers.add(customer);
    }

    @Override
    public void updateCustomer(Customer customer) {
        Customer oldCustomer = getCustomerFromId(customer.getId());
        deleteCustomer(oldCustomer);
        createCustomer(customer);
    }

    @Override
    public void deleteCustomer(Customer customer) {
        customers.remove(customer);
    }

    @Override
    public Customer getCustomerFromId(int customerId) {
        for (Customer customer : customers) {
            if (customer.getId() == customerId) {
                return customer;
            }
        }
        return null;
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customers;
    }
}
