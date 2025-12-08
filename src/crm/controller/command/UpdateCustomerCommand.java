package crm.controller.command;

import crm.observer.event.EventType;
import customer.Customer;
import repository.CustomerRepository;

import java.util.Scanner;
import java.util.Stack;

public class UpdateCustomerCommand implements CRMCommand {
    private final String name = "Update Customer";
    private final EventType eventType = EventType.CUSTOMER_UPDATED;

    private final CustomerRepository customerRepository;
    private Stack<Customer> updatedCustomers = new Stack<>();

    public UpdateCustomerCommand(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public void execute() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Customer Id: ");
        int customerId = Integer.parseInt(scanner.nextLine());

        Customer customerToUpdate = customerRepository.getCustomerFromId(customerId);
        System.out.println(customerToUpdate);

        System.out.println("What do you want to change?: ");
        System.out.println("1. Name");
        System.out.println("2. Email");
        System.out.println("3. Phone Number");
        System.out.print("Your Choice: ");
        String toChange = scanner.nextLine();

        switch (toChange) {
            case "1":
                System.out.print("Full Name: ");
                String fullName = scanner.nextLine();
                customerToUpdate.setName(fullName);
                break;
            default:
            case "2":
                System.out.print("Email Address: ");
                String email = scanner.nextLine();
                customerToUpdate.setEmail(email);
                break;
            case "3":
                System.out.print("Phone Number: ");
                String phone = scanner.nextLine();
                customerToUpdate.setPhone(phone);
                break;
        }

        updatedCustomers.push(customerToUpdate);
        customerRepository.updateCustomer(customerToUpdate);
    }

    @Override
    public void undo() {
        Customer updatedCustomer = updatedCustomers.pop();
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
