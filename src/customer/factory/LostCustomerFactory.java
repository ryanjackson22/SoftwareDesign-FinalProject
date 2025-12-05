package customer.factory;

import customer.Customer;
import customer.LostCustomer;

public class LostCustomerFactory implements CustomerFactory {
    @Override
    public Customer createCustomer(String name, String email, String phone) {
        return new LostCustomer(name, email, phone);
    }
}
