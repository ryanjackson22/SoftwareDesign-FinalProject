package notification;

import contact.Contact;

public class SMSNotification implements NotificationStrategy {
    @Override
    public void send(Contact contact, String message) {
        System.out.println(
                "Sending SMS notification." +
                        "  Customer: " + contact.getName() +
                        "  Message: " + message
        );
    }
}
