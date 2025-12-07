package crm.controller;

import crm.controller.command.CRMCommand;
import crm.observer.CRMObserver;
import crm.observer.event.EventType;

import java.util.List;
import java.util.ArrayList;
import java.util.Stack;

public class CRMController {
    private final Stack<CRMCommand> commandHistory = new Stack<>();
    private final List<CRMObserver> observers = new ArrayList<>();
    private List<CRMCommand> commands;

    public void addCommand(CRMCommand command) {
        commandHistory.push(command);
    }

    public List<String> getCommandListing() {
        List<String> commandNames = new ArrayList<>();

        for (CRMCommand command : commands) {
            commandNames.add(command.getName());
        }

        return commandNames;
    }

    public void executeCommand(int slot) {
        CRMCommand command = commands.get(slot);

        commandHistory.push(command);
        command.execute();
        notifyObservers(command.getEventType());
    }

    public void undoCommand() {
        commandHistory.pop().undo();
        notifyObservers(EventType.COMMAND_UNDONE);
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
