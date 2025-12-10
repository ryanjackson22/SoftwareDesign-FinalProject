/*
 * 	Author: Ryan Jackson & Ryan Buck
 * 	Created: 12/10/2025
 * 	Final Project - Console Customer Relationship Management Tool
 */

package notification;

import customer.Customer;

public interface NotificationStrategy {
    public void send(Customer customer, String message);
}
