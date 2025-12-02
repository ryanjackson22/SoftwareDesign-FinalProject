package crm.controller.command;

import contact.Contact;
import contact.repository.ContactRepository;

import java.util.Stack;

public class UpdateContactCommand implements CRMCommand {
    private final ContactRepository contactRepository;
    private final Stack<Contact> previousContacts = new Stack<>();

    public UpdateContactCommand(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @Override
    public void execute(Contact contact) {
        previousContacts.push(contact);
        contactRepository.updateContact(contact);
    }

    @Override
    public void undo() {
        contactRepository.updateContact(previousContacts.pop());
    }
}
