package crm.controller.command;

import customer.Customer;
import customer.factory.CustomerFactory;
import customer.factory.RegularCustomerFactory;

public class CreateContactCommand implements CRMCommand {
    @Override
    public void execute(Customer customer) {
        CustomerFactory customerFactory = new RegularCustomerFactory();
    }

    @Override
    public void undo() {

    }
}
