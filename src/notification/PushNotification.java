package notification;

import customer.Customer;

public class PushNotification implements NotificationStrategy {
    @Override
    public void send(Customer customer, String message) {
        System.out.println("Sent Push Notification: '" + message + "' to " + customer.getPhone());
    }
}
