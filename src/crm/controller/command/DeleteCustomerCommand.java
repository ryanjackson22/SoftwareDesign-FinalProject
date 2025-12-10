package crm.controller.command;

import crm.observer.event.CRMEvent;
import crm.observer.event.EventType;
import customer.Customer;
import repository.CustomerRepository;

import java.util.Scanner;
import java.util.Stack;

public class DeleteCustomerCommand implements CRMCommand {
    private final EventType eventType = EventType.CUSTOMER_DELETED;

    private final CustomerRepository customerRepository;
    private final Stack<Customer> deletedCustomers = new Stack<>();

    public DeleteCustomerCommand(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public CRMEvent execute() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Customer Id: ");
        int customerId = Integer.parseInt(scanner.nextLine());

        Customer customerToDelete = customerRepository.getCustomerFromId(customerId);

        deletedCustomers.push(customerToDelete);
        customerRepository.deleteCustomer(customerToDelete);

        System.out.printf("Customer %d deleted successfully!\n", customerId);

        return new CRMEvent(eventType, customerId, "Customer deleted");
    }

    @Override
    public void undo() {
        Customer deletedCustomer = deletedCustomers.pop();
        customerRepository.createCustomer(deletedCustomer);
        System.out.printf("Customer %d deletion successfully undone!\n", deletedCustomer.getId());
    }

    @Override
    public String getName() {
        return "Delete Customer";
    }

    @Override
    public EventType getEventType() {
        return eventType;
    }
}
