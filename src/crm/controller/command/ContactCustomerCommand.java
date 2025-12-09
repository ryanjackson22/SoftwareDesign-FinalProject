package crm.controller.command;

import crm.observer.event.EventType;
import customer.Customer;
import repository.CustomerRepository;

import java.util.Scanner;
import java.util.Stack;

public class ContactCustomerCommand implements CRMCommand {
    private final String name = "Contact Customer";
    private final EventType eventType = EventType.NOTIFICATION_SENT;

    private final CustomerRepository customerRepository;
    private Stack<Integer> contactedCustomerIds = new Stack<>();

    public ContactCustomerCommand(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public void execute() {
        Scanner scanner = new Scanner(System.in);

        // Get customer
        System.out.println("Enter Customer Id: ");
        int customerId = Integer.parseInt(scanner.nextLine());

        Customer customer = customerRepository.getCustomerFromId(customerId);

        if (customer == null) {
            System.out.println("Customer not found!");
            return;
        }

        System.out.println("Contacting: " + customer.getName());
        System.out.println("Preferred Method: " + customer.getPreferredContactMethod().getClass().getSimpleName());
        System.out.println();

        // Get message
        System.out.print("Enter message to send: ");
        String message = scanner.nextLine();

        // Send notification using the Strategy pattern
        customer.contact(message);

        // Record interaction
        customer.addInteraction(EventType.NOTIFICATION_SENT,
            "Contacted via " + customer.getPreferredContactMethod() + " with message: " + message);

        // Track for undo
        contactedCustomerIds.push(customerId);

        System.out.println("\nNotification sent successfully!");
    }

    @Override
    public void undo() {
        if (contactedCustomerIds.isEmpty()) {
            System.out.println("No notifications to undo");
            return;
        }

        contactedCustomerIds.pop();
        System.out.println("Note: Notification already sent - cannot be recalled");
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public EventType getEventType() {
        return eventType;
    }
}
