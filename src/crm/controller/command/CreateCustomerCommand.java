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
    enum CustomerType {
        LEAD("1", "Lead"),
        REGULAR("2", "Regular"),
        VIP("3", "VIP");

        private final String code;
        private final String displayName;

        CustomerType(String code, String displayName) {
            this.code = code;
            this.displayName = displayName;
        }

        static CustomerType fromCode(String code) {
            for (CustomerType type : values()) {
                if (type.code.equals(code)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Invalid Customer Type: " + code);
        }

        Customer create(String name, String email, String phone) {
            switch (this) {
                case LEAD:
                    return new LeadCustomer(name, email, phone);
                case REGULAR:
                    return new RegularCustomer(name, email, phone);
                case VIP:
                    return new VIPCustomer(name, email, phone);
                default:
                    throw new IllegalStateException("Unexpected customer type: " + this);
            }
        }

        String getDisplayName() {
            return displayName;
        }
    }

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
        String customerTypeCode = scanner.nextLine();

        System.out.println();
        System.out.println("============ Contact Info. ============");
        System.out.print("Full Name: ");
        String fullName = scanner.nextLine();

        System.out.print("Email Address: ");
        String email = scanner.nextLine();

        System.out.print("Phone Number: ");
        String phone = scanner.nextLine();

        CustomerType type = CustomerType.fromCode(customerTypeCode);
        Customer newCustomer = type.create(fullName, email, phone);

        newCustomer.addInteraction(EventType.CUSTOMER_CREATED, "Customer record created");
        createdCustomers.push(newCustomer);
        customerRepository.createCustomer(newCustomer);

        System.out.printf("Customer %d created successfully!\n", newCustomer.getId());

        return new CRMEvent(eventType, newCustomer.getId(), "Type: " + type.getDisplayName());
    }

    @Override
    public void undo() {
        Customer customer = createdCustomers.pop();
        customerRepository.deleteCustomer(customer);
        System.out.printf("Customer %d creation successfully undone!\n", customer.getId());
    }

    @Override
    public String getName() {
        return EventType.CUSTOMER_CREATED.toString();
    }

    @Override
    public EventType getEventType() {
        return eventType;
    }
}
