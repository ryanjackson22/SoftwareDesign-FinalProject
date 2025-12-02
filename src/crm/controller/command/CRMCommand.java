package crm.controller.command;

import contact.Contact;

public interface CRMCommand {
    public void execute(Contact contact);
    public void undo();
}
