package crm.controller.command;

import contact.Contact;
import contact.factory.CustomerFactory;
import contact.factory.RegularCustomerFactory;

public class CreateContactCommand implements CRMCommand {
    @Override
    public void execute(Contact contact) {
        CustomerFactory customerFactory = new RegularCustomerFactory();
    }

    @Override
    public void undo() {

    }
}
