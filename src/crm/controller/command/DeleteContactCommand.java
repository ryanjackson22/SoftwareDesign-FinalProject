package crm.controller.command;

import contact.Contact;
import contact.repository.ContactRepository;

import java.util.Stack;

public class DeleteContactCommand implements CRMCommand {
    private final ContactRepository contactRepository;
    private final Stack<Contact> previousContacts = new Stack<>();

    public DeleteContactCommand(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @Override
    public void execute(Contact contact) {
        previousContacts.push(contact);
        contactRepository.deleteContact(contact);
    }

    @Override
    public void undo() {
        contactRepository.createContact(previousContacts.pop());
    }
}
