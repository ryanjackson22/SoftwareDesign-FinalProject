package notification;

import contact.Contact;

public class PushNotification implements NotificationStrategy {
    @Override
    public void send(Contact contact, String message) {
        System.out.println(
            "Sending push notification." +
            "  Customer: " + contact.getName() +
            "  Message: " + message
        );
    }
}
