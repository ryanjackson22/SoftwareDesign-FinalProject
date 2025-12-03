package customer.factory;

import customer.Customer;

public interface CustomerFactory {
    public Customer createCustomer(String id, String name, String email, String phone);
}
