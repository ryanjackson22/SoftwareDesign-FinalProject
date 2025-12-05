package customer.factory;

import customer.Customer;
import customer.RegularCustomer;

public class RegularCustomerFactory implements CustomerFactory {
    @Override
    public Customer createCustomer(String name, String email, String phone) {
        return new RegularCustomer(name, email, phone);
    }
}
