package contact.repository;

import contact.Contact;

public interface ContactRepository {
    void createContact(Contact contact);
    void updateContact(Contact contact);
    void deleteContact(Contact contact);
    Contact getContactById(String id);
}
