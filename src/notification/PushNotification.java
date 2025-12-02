package notification;

import customer.Customer;

public class PushNotification implements NotificationStrategy {
    @Override
    public void send(Customer customer, String message) {
        System.out.println(
            "Sending push notification." +
            "  Customer: " + customer.getName() +
            "  Message: " + message
        );
    }
}
