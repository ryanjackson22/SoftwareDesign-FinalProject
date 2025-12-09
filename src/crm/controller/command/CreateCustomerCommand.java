package crm.controller.command;

import crm.observer.event.CRMEvent;
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
    private final Stack<Customer> createdCustomers = new Stack<>();

    public CreateCustomerCommand(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public CRMEvent execute() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Customer Type: ");
        System.out.println("1. Lead Customer");
        System.out.println("2. Regular Customer");
        System.out.println("3. VIP Customer");
        System.out.print("Your Choice: ");
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
        String customerTypeStr;
        switch (customerType) {
            case "1":
                newCustomer = new LeadCustomer(fullName, email, phone);
                customerTypeStr = "Lead";
                break;
            default:
            case "2":
                newCustomer = new RegularCustomer(fullName, email, phone);
                customerTypeStr = "Regular";
                break;
            case "3":
                newCustomer = new VIPCustomer(fullName, email, phone);
                customerTypeStr = "VIP";
                break;
        }

        newCustomer.addInteraction(EventType.CUSTOMER_CREATED, "Customer record created");
        createdCustomers.push(newCustomer);
        customerRepository.createCustomer(newCustomer);

        System.out.printf("Customer %d created successfully!\n", newCustomer.getId());

        return new CRMEvent(eventType, newCustomer.getId(), "Type: " + customerTypeStr);
    }

    @Override
    public void undo() {
        customerRepository.deleteCustomer(createdCustomers.pop());
        System.out.printf("Customer %d creation successfully undone!\n", createdCustomers.pop().getId());
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
