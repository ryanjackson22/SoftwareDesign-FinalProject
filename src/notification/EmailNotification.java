package notification;

import customer.Customer;

public class EmailNotification implements NotificationStrategy {

    @Override
    public void send(Customer customer, String message) {
        System.out.println("Sent: '" + message + "' to " + customer.getEmail());
    }
}
