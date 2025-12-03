package customer.factory;

import customer.Customer;
import customer.LeadCustomer;

public class LeadCustomerFactory implements CustomerFactory {
    @Override
    public Customer createCustomer(String id, String name, String email, String phone) {
        return new LeadCustomer(id, name, email, phone);
    }
}
