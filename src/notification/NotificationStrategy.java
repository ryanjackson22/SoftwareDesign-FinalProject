package notification;

import customer.Customer;

public interface NotificationStrategy {
    public void send(Customer customer, String message);
}
