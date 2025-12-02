package notification;

import contact.Contact;

public interface NotificationStrategy {
    public void send(Contact contact, String message);
}
