package notification;

import customer.Customer;

public class NotificationService {
    private NotificationStrategy strategy;

    public void setStrategy(NotificationStrategy strategy) {
        this.strategy = strategy;
    }

    public void contactCustomer(Customer customer, String message) {
        // stub
    }
}
