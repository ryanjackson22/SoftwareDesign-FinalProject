package crm.controller.command;

import customer.Customer;

public interface CRMCommand {
    public void execute(Customer customer);
    public void undo();
}
