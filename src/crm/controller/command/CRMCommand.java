package crm.controller.command;

import customer.Customer;

public interface CRMCommand {
    public void execute();
    public void undo();
}
