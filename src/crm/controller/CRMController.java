package crm.controller;

import crm.observer.CRMObserver;
import customer.Customer;
import customer.LeadCustomer;
import customer.RegularCustomer;
import customer.VIPCustomer;

import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

public class CRMController {
    private CommandHistory commandHistory = new CommandHistory();
    private List<Customer> customers = new ArrayList<Customer>();
    private List<CRMObserver> observers = new ArrayList<CRMObserver>();

    public void createContact(Customer customer) {
        customers.add(customer);
    }

    public void createContact() {
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
                customers.add(new LeadCustomer(fullName, email, phone));
                break;
            case "2":
                customers.add(new RegularCustomer(fullName, email, phone));
                break;
            case "3":
                customers.add(new VIPCustomer(fullName, email, phone));
                break;
        }
    }

    public void deleteContact(Customer customer) {
        customers.remove(customer);
    }

    public void updateContactType(String data) {
        // stub
    }

    public void updateContactInfo(String data) {
        // stub
    }

    public void makeSale(String data) {
        // stub
    }

    public void undo() {
        // stub
    }

    public void addObserver(CRMObserver observer) {
        // stub
    }

    public void removeObserver(CRMObserver observer) {
        // stub
    }

    public void notifyObservers() {
        // stub
    }

    public void printCustomers() {
        for (Customer customer : customers) {
            System.out.println(customer.toString());
        }
    }
}
