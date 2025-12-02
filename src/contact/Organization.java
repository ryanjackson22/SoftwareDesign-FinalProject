package contact;

import java.util.ArrayList;
import java.util.List;

public class Organization extends Contact {
    private final List<Contact> contacts;

    public Organization(String id, String name, String email, String phone) {
        super(id, name, email, phone);
        contacts = new ArrayList<>();
    }

    public void addContact(Contact contact) {
        contacts.add(contact);
    }

    public void removeContact(Contact contact) {
        contacts.remove(contact);
    }

    public List<Contact> getContacts() {
        return contacts;
    }
}
