package crm.controller.command;

import contact.Contact;
import contact.repository.ContactRepository;

import java.util.Stack;

public class CreateContactCommand implements CRMCommand {
    private final ContactRepository contactRepository;
    private final Stack<Contact> createdContacts = new Stack<>();

    public CreateContactCommand(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @Override
    public void execute(Contact contact) {
        createdContacts.push(contact);
        contactRepository.createContact(contact);
    }

    @Override
    public void undo() {
        contactRepository.deleteContact(createdContacts.pop());
    }
}
