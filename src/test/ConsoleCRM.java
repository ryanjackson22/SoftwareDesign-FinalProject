package test;

import crm.controller.CRMController;
import crm.observer.FileLogger;
import crm.observer.SalesLogger;
import customer.*;
import crm.controller.command.*;
import repository.CustomerRepository;
import repository.InMemoryCustomerRepository;

import java.util.List;
import java.util.Scanner;

public class ConsoleCRM {
    public static void main(String[] args) {
        clear();
        Scanner scanner = new Scanner(System.in);

        CustomerRepository customerRepository = new InMemoryCustomerRepository();
        customerRepository.createCustomer(new RegularCustomer("John Brown", "john.brown@tomato.mail", "123-123-1234"));
        customerRepository.createCustomer(new RegularCustomer("Alice Sharp", "alice.sharp@olive.mail", "987-987-9876"));
        customerRepository.createCustomer(new LeadCustomer("President Greeble", "greeble@whitehouse.gov", "111-222-3333"));
        customerRepository.createCustomer(new VIPCustomer("Big Tim", "bigtim123@hotmail.com", "123-456-7890"));
        customerRepository.createCustomer(new LostCustomer("Old Man Jenkins", "jenkins@aol.com", "321-654-0987"));

        CRMController crm = getCrmController(customerRepository);

        while (true) {
            System.out.println("========= CRM Commands ===========");
            List<String> commandNames =  crm.getCommandListing();

            int i;
            for (i = 0; i < crm.getCommandListing().size(); i++) {
                System.out.printf("%d. %s%n", i+1, commandNames.get(i));
            }

            System.out.printf("%d. %s%n", i+1, "Print Customers");
            System.out.printf("%d. %s%n", i+2, "Undo");
            System.out.printf("%d. %s%n", i+3, "Exit");
            System.out.print("Your choice: ");
            int input = Integer.parseInt(scanner.nextLine());

            clear();
            if (input < i+1) {
                crm.executeCommand(input - 1);
            } else if (input == i+1) {
                customerRepository.getAllCustomers().forEach(System.out::println);
                System.out.println("Press enter to continue...");
                scanner.nextLine();
            } else if (input == i+2) {
                crm.undoCommand();
            } else if (input == i+3) {
                break;
            }
            clear();
        }

        scanner.close();
    }

    private static CRMController getCrmController(CustomerRepository customerRepository) {
        CRMController crm = new CRMController();

        // Register loggers
        FileLogger fileLogger = new FileLogger("crm_activity.log");
        SalesLogger salesLogger = new SalesLogger("sales.log");
        crm.addObserver(fileLogger);
        crm.addObserver(salesLogger);

        CRMCommand createCustomerCommand = new CreateCustomerCommand(customerRepository);
        CRMCommand updateCustomerCommand = new UpdateCustomerCommand(customerRepository);
        CRMCommand deleteCustomerCommand = new DeleteCustomerCommand(customerRepository);
        CRMCommand makeSaleCommand = new MakeSaleCommand(customerRepository);
        CRMCommand contactCustomerCommand = new ContactCustomerCommand(customerRepository);
        CRMCommand viewCustomerHistoryCommand = new ViewCustomerHistoryCommand(customerRepository);

        crm.addCommand(createCustomerCommand);
        crm.addCommand(updateCustomerCommand);
        crm.addCommand(deleteCustomerCommand);
        crm.addCommand(makeSaleCommand);
        crm.addCommand(contactCustomerCommand);
        crm.addCommand(viewCustomerHistoryCommand);
        return crm;
    }

    public static void clear() {
        for (int i = 0; i < 15; i++) {
            System.out.println();
        }
    }
}
