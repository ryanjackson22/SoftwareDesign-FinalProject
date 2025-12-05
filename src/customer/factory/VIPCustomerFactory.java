package customer.factory;

import customer.Customer;
import customer.VIPCustomer;

public class VIPCustomerFactory implements CustomerFactory {
    @Override
    public Customer createCustomer(String name, String email, String phone) {
        return new VIPCustomer(name, email, phone);
    }
}
