package notification;

import customer.Customer;

public class SMSNotification implements NotificationStrategy {
    @Override
    public void send(Customer customer, String message) {
        System.out.println(
                "Sending SMS notification." +
                        "  Customer: " + customer.getName() +
                        "  Message: " + message
        );
    }
}
