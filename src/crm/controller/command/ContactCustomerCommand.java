/*
 * 	Author: Ryan Jackson & Ryan Buck
 * 	Created: 12/10/2025
 * 	Final Project - Console Customer Relationship Management Tool
 */

package crm.controller.command;

import crm.observer.event.CRMEvent;
import crm.observer.event.EventType;
import customer.Customer;
import repository.CustomerRepository;

import java.util.Scanner;
import java.util.Stack;

public class ContactCustomerCommand implements CRMCommand {
    private final EventType eventType = EventType.NOTIFICATION_SENT;

    private final CustomerRepository customerRepository;
    private final Stack<Integer> contactedCustomerIds = new Stack<>();

    public ContactCustomerCommand(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public CRMEvent execute() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Customer Id: ");
        int customerId = Integer.parseInt(scanner.nextLine());

        Customer customer = customerRepository.getCustomerFromId(customerId);

        if (customer == null) {
            System.out.println("Customer not found!");
            return new CRMEvent(eventType);
        }

        System.out.println("Contacting: " + customer.getName());
        System.out.println("Preferred Method: " + customer.getPreferredContactMethod().getClass().getSimpleName());
        System.out.println();

        System.out.print("Enter message to send: ");
        String message = scanner.nextLine();

        customer.contact(message);

        customer.addInteraction(EventType.NOTIFICATION_SENT,"Contacted via " + customer.getPreferredContactMethod() + " with message: " + message);

        contactedCustomerIds.push(customerId);

        System.out.println("\nNotification sent successfully!");

        String contactMethod = customer.getPreferredContactMethod().getClass().getSimpleName();
        return new CRMEvent(eventType, customerId, "Contact Method: " + contactMethod);
    }

    @Override
    public void undo() {
        if (contactedCustomerIds.isEmpty()) {
            System.out.println("No notifications to undo");
            return;
        }

        contactedCustomerIds.pop();
        System.out.println("Note: Notification already sent - cannot be undone");
    }

    @Override
    public String getName() {
        return "Send Notification";
    }

    @Override
    public EventType getEventType() {
        return eventType;
    }
}
