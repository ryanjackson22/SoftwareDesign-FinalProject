package crm.controller.command;

import crm.observer.event.CRMEvent;
import crm.observer.event.EventType;
import customer.Customer;
import repository.CustomerRepository;

import java.util.Scanner;
import java.util.Stack;

public class MakeSaleCommand implements CRMCommand {
    private final String name = "Make Sale";
    private final EventType eventType = EventType.SALE_MADE;

    private final CustomerRepository customerRepository;
    private Stack<Customer> customerSales = new Stack<>();

    public MakeSaleCommand(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public CRMEvent execute() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Customer Id: ");
        int customerId = Integer.parseInt(scanner.nextLine());

        Customer saleCustomer = customerRepository.getCustomerFromId(customerId);
        saleCustomer.addInteraction(EventType.SALE_MADE, "Sale completed");
        customerSales.push(saleCustomer);
        System.out.printf("Sale with %s made!\n", saleCustomer.getName());

        return new CRMEvent(eventType, customerId, "Sale completed");
    }

    @Override
    public void undo() {
        Customer saleCustomer = customerSales.pop();
        System.out.printf("Sale with %s successfully undone!\n", saleCustomer.getName());
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
