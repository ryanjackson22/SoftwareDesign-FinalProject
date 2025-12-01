package crm.controller;

import crm.observer.CRMObserver;
import java.util.List;
import java.util.ArrayList;

public class CRMController {
    private CommandHistory commandHistory = new CommandHistory();
//    private CommandRepository commandRepository;
    private List<CRMObserver> observers = new ArrayList<CRMObserver>();

    public void createContact(String data) {
        // stub
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
