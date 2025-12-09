package crm.controller.command;

import crm.observer.event.EventType;
import customer.Customer;
import customer.Interaction;
import repository.CustomerRepository;

import java.util.List;
import java.util.Scanner;

public class ViewCustomerHistoryCommand implements CRMCommand {
    private final EventType eventType = EventType.NO_EVENT;

    private final CustomerRepository customerRepository;

    public ViewCustomerHistoryCommand(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public void execute() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Customer Id: ");
        int customerId = Integer.parseInt(scanner.nextLine());

        Customer customer = customerRepository.getCustomerFromId(customerId);

        if (customer == null) {
            System.out.println("Customer not found!");
            return;
        }

        System.out.println("\n========================================");
        System.out.println("Interaction History for: " + customer.getName());
        System.out.println("Customer ID: " + customer.getId());
        System.out.println("========================================\n");

        List<Interaction> history = customer.getInteractionHistory();

        if (history.isEmpty()) {
            System.out.println("No interactions recorded for this customer.");
        } else {
            for (Interaction interaction : history) {
                System.out.println(interaction);
            }
        }

        System.out.println("\n========================================");
        System.out.println("Total interactions: " + history.size());
        System.out.println("========================================");

        System.out.println("\nPress enter to continue...");
        scanner.nextLine();
    }

    @Override
    public void undo() {
        // Viewing history doesn't need undo
    }

    @Override
    public String getName() {
        return "View Customer History";
    }

    @Override
    public EventType getEventType() {
        return eventType;
    }
}
