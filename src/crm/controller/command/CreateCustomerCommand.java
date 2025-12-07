package crm.controller.command;

import crm.observer.event.EventType;
import customer.Customer;
import customer.LeadCustomer;
import customer.RegularCustomer;
import customer.VIPCustomer;
import repository.CustomerRepository;

import java.util.Scanner;
import java.util.Stack;

public class CreateCustomerCommand implements CRMCommand {
    private final String name = "Create Customer";
    private final EventType eventType = EventType.CUSTOMER_CREATED;

    private final CustomerRepository customerRepository;
    private Stack<Customer> createdCustomers;

    public CreateCustomerCommand(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public void execute() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Customer Type: ");
        System.out.println("1. Lead Customer");
        System.out.println("2. Regular Customer");
        System.out.println("3. VIP Customer");
        String customerType = scanner.nextLine();

        System.out.println();
        System.out.println("============ Contact Info. ============");
        System.out.print("Full Name: ");
        String fullName = scanner.nextLine();

        System.out.print("Email Address: ");
        String email = scanner.nextLine();

        System.out.print("Phone Number: ");
        String phone = scanner.nextLine();

        Customer newCustomer;
        switch (customerType) {
            case "1":
                newCustomer = new LeadCustomer(fullName, email, phone);
                break;
            default:
            case "2":
                newCustomer = new RegularCustomer(fullName, email, phone);
                break;
            case "3":
                newCustomer = new VIPCustomer(fullName, email, phone);
                break;
        }

        createdCustomers.push(newCustomer);
        customerRepository.createCustomer(newCustomer);
    }

    @Override
    public void undo() {
        customerRepository.deleteCustomer(createdCustomers.pop());
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
