package crm.controller.command;

import contact.Contact;

import java.util.Stack;

public class MakeSaleCommand implements CRMCommand {
    private final Stack<Contact> contacts = new Stack<>();

    @Override
    public void execute(Contact contact) {
        this.contacts.push(contact);
        System.out.println("Made sale to: " + contact.getName());
    }

    @Override
    public void undo() {
        System.out.println("Undid sale to: " + contacts.pop().getName());
    }
}
