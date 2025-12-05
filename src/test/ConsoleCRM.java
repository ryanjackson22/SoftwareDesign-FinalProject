package test;


import crm.controller.CRMController;
import customer.Customer;
import customer.LeadCustomer;
import java.util.Scanner;

public class ConsoleCRM {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        CRMController crm = new CRMController();
        crm.createContact(new LeadCustomer("John Doe", "john.doe@gmail.com", "(603)555-1234"));


        System.out.println("=========== Console CRM ===========");
        System.out.println("1. Manage Contacts");
        System.out.println("2. View Analytics");
        String input = scanner.nextLine();
        if (input.equals("1")) {
            clear();
            System.out.println("========= Contact Management ===========");
            System.out.println("1. Create Contact");
            System.out.println("2. Update Contact");
            System.out.println("3. Delete Contact");
            System.out.println("4. Search Contact");
            input = scanner.nextLine();
        }

        scanner.close();


    }

    public static void clear() {
        for (int i = 0; i < 15; i++) {
            System.out.println();
        }
    }
}