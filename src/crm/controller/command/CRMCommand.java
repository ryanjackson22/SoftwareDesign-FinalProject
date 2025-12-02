package crm.controller.command;

import contact.Contact;

public interface CRMCommand {
    void execute(Contact contact);
    void undo();
}
