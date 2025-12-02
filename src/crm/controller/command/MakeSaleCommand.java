package crm.controller.command;

import contact.Contact;

public class MakeSaleCommand implements CRMCommand {
    private Contact contact;

    @Override
    public void execute(Contact contact) {
        this.contact = contact;
        System.out.println("Made sale to: " + contact.getName());
    }

    @Override
    public void undo() {
        System.out.println("Undid sale to: " + contact.getName());
    }
}
