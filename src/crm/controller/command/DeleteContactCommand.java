package crm.controller.command;

import contact.Contact;
import contact.repository.ContactRepository;

public class DeleteContactCommand implements CRMCommand {
    ContactRepository contactRepository;
    Contact previousContactValue;

    public DeleteContactCommand(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @Override
    public void execute(Contact contact) {
        previousContactValue = contact;
        contactRepository.deleteContact(contact);
    }

    @Override
    public void undo() {
        contactRepository.createContact(previousContactValue);
    }
}
