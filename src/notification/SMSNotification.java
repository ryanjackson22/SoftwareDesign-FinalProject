/*
 * 	Author: Ryan Jackson & Ryan Buck
 * 	Created: 12/10/2025
 * 	Final Project - Console Customer Relationship Management Tool
 */

package notification;

import customer.Customer;

public class SMSNotification implements NotificationStrategy {
    @Override
    public void send(Customer customer, String message) {
        System.out.println("Sent: '" + message + "' to " + customer.getPhone());
    }

    @Override
    public String toString() {
        return "SMS";
    }
}
