package crm.controller.command;

import customer.*;

import java.util.Scanner;

public class CreateContactCommand implements CRMCommand {

    @Override
    public void execute() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Contact Type: ");
        System.out.println("1. Customer Lead");
        System.out.println("2. Current Customer");
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

        switch (customerType) {
            case "1":
//                customers.add(new LeadCustomer(fullName, email, phone));
                break;
            case "2":
//                customers.add(new RegularCustomer(fullName, email, phone));
                break;
            case "3":
//                customers.add(new VIPCustomer(fullName, email, phone));
                break;
        }
    }

    @Override
    public void undo() {

    }
}
