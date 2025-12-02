package notification;

import contact.Contact;

public class NotificationService {
    public void notifyContact(Contact contact, String message) {
        NotificationStrategy notificationStrategy = contact.getPreferredNotificationMethod();
        notificationStrategy.send(contact, message);
    }
}
