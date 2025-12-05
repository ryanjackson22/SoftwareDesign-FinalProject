package crm.controller;

import crm.observer.CRMObserver;
import customer.Customer;

import java.util.List;
import java.util.ArrayList;

public class CRMController {
    private CommandHistory commandHistory = new CommandHistory();
    private List<Customer> customers = new ArrayList<Customer>();
    private List<CRMObserver> observers = new ArrayList<CRMObserver>();

    public void createContact(Customer customer) {
        customers.add(customer);
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
}
