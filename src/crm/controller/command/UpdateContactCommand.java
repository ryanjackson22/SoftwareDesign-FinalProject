package crm.controller.command;

import contact.Contact;
import contact.repository.ContactRepository;

public class UpdateContactCommand implements CRMCommand {
    ContactRepository contactRepository;
    Contact previousContactValue;

    public UpdateContactCommand(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @Override
    public void execute(Contact contact) {
        previousContactValue = contact;
        contactRepository.updateContact(contact);
    }

    @Override
    public void undo() {
        contactRepository.updateContact(previousContactValue);
    }
}
