package notification;

import contact.Contact;

public class NotificationService {
    private NotificationStrategy strategy;

    public void setStrategy(NotificationStrategy strategy) {
        this.strategy = strategy;
    }

    public void contactCustomer(Contact contact, String message) {
        // stub
    }
}
