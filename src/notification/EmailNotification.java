package notification;

import contact.Contact;

public class EmailNotification implements NotificationStrategy {

    @Override
    public void send(Contact contact, String message) {
        System.out.println(
                "Sending email notification." +
                        "  Customer: " + contact.getName() +
                        "  Message: " + message
        );
    }
}
