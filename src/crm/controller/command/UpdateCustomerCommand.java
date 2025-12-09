package crm.controller.command;

import crm.observer.event.CRMEvent;
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
    public CRMEvent execute() {
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

        String updateInfo;
        switch (toChange) {
            case "1":
                System.out.print("Full Name: ");
                String fullName = scanner.nextLine();
                customerToUpdate.setName(fullName);
                customerToUpdate.addInteraction(EventType.CUSTOMER_UPDATED, "Name updated to: " + fullName);
                updateInfo = "Name updated";
                break;
            default:
            case "2":
                System.out.print("Email Address: ");
                String email = scanner.nextLine();
                customerToUpdate.setEmail(email);
                customerToUpdate.addInteraction(EventType.CUSTOMER_UPDATED, "Email updated to: " + email);
                updateInfo = "Email updated";
                break;
            case "3":
                System.out.print("Phone Number: ");
                String phone = scanner.nextLine();
                customerToUpdate.setPhone(phone);
                customerToUpdate.addInteraction(EventType.CUSTOMER_UPDATED, "Phone updated to: " + phone);
                updateInfo = "Phone updated";
                break;
        }

        updatedCustomers.push(customerToUpdate);
        customerRepository.updateCustomer(customerToUpdate);
        System.out.printf("Customer %d successfully updated!\n", customerToUpdate.getId());

        return new CRMEvent(eventType, customerId, updateInfo);
    }

    @Override
    public void undo() {
        Customer updatedCustomer = updatedCustomers.pop();
        // TODO add undo functionality
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
