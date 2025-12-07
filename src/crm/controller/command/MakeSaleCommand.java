package crm.controller.command;

import crm.observer.event.EventType;
import customer.Customer;
import repository.CustomerRepository;

import java.util.Scanner;
import java.util.Stack;

public class MakeSaleCommand implements CRMCommand {
    private final String name = "Make Sale";
    private final EventType eventType = EventType.SALE_MADE;

    private final CustomerRepository customerRepository;
    private Stack<Customer> customerSales;

    public MakeSaleCommand(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public void execute() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Customer Id: ");
        int customerId = Integer.parseInt(scanner.nextLine());

        Customer saleCustomer = customerRepository.getCustomerFromId(customerId);
        customerSales.push(saleCustomer);
        System.out.println("Made sale with " + saleCustomer.getName());
    }

    @Override
    public void undo() {
        Customer saleCustomer = customerSales.pop();
        System.out.println("Unmade sale with " + saleCustomer.getName());
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
