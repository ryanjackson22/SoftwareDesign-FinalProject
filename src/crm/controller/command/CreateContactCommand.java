package crm.controller.command;

import contact.Contact;
import contact.repository.ContactRepository;

public class CreateContactCommand implements CRMCommand {
    ContactRepository contactRepository;
    Contact createdContact;

    public CreateContactCommand(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @Override
    public void execute(Contact contact) {
        createdContact = contact;
        contactRepository.createContact(contact);
    }

    @Override
    public void undo() {
        contactRepository.deleteContact(createdContact);
    }
}
