package crm.controller.command;

import crm.observer.event.EventType;
import customer.Customer;
import repository.CustomerRepository;

import java.util.Scanner;
import java.util.Stack;

public class DeleteCustomerCommand implements CRMCommand {
    private final String name = "Delete Customer";
    private final EventType eventType = EventType.CUSTOMER_DELETED;

    private final CustomerRepository customerRepository;
    private Stack<Customer> deletedCustomers = new Stack<>();

    public DeleteCustomerCommand(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public void execute() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Customer Id: ");
        int customerId = Integer.parseInt(scanner.nextLine());

        Customer customerToDelete = customerRepository.getCustomerFromId(customerId);

        deletedCustomers.push(customerToDelete);
        customerRepository.deleteCustomer(customerToDelete);
    }

    @Override
    public void undo() {
        customerRepository.createCustomer(deletedCustomers.pop());
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
