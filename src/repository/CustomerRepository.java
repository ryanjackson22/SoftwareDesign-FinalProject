package repository;

import customer.*;

import java.util.List;

public interface CustomerRepository {
    void createCustomer(Customer customer);
    void updateCustomer(Customer customer);
    void deleteCustomer(Customer customer);
    Customer getCustomerFromId(int customerId);
    List<Customer> getAllCustomers();
}
