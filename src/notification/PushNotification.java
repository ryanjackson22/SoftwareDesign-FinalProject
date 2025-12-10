/*
 * 	Author: Ryan Jackson & Ryan Buck
 * 	Created: 12/10/2025
 * 	Final Project - Console Customer Relationship Management Tool
 */

package notification;

import customer.Customer;

public class PushNotification implements NotificationStrategy {
    @Override
    public void send(Customer customer, String message) {
        System.out.println("Sent Push Notification: '" + message + "' to " + customer.getPhone());
    }

    @Override
    public String toString() {
        return "Push Notification";
    }
}
