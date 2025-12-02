package crm.controller;

import contact.Contact;
import crm.controller.command.CRMCommand;
import crm.observer.CRMObserver;
import crm.observer.event.CRMEvent;
import crm.observer.event.EventType;
import notification.NotificationService;

import java.util.List;
import java.util.ArrayList;
import java.util.Stack;

public class CRMController {
    private final Stack<CRMCommand> commandHistory = new Stack<CRMCommand>();
    private final NotificationService notificationService = new NotificationService();
    private final List<CRMObserver> observers = new ArrayList<CRMObserver>();
    private CRMCommand createContactCommand;
    private CRMCommand updateContactCommand;
    private CRMCommand deleteContactCommand;
    private CRMCommand makeSaleCommand;

    public void createContact(Contact contact) {
        createContactCommand.execute(contact);
        notifyObservers(EventType.CONTACT_CREATED);
    }

    public void updateContact(Contact contact) {
        updateContactCommand.execute(contact);
        notifyObservers(EventType.CONTACT_UPDATED);
    }

    public void deleteContact(Contact contact) {
        deleteContactCommand.execute(contact);
        notifyObservers(EventType.CONTACT_DELETED);
    }

    public void makeSale(Contact contact) {
        makeSaleCommand.execute(contact);
        notifyObservers(EventType.SALE_MADE);
    }

    public void undo() {
        commandHistory.pop().undo();
        notifyObservers(EventType.COMMAND_UNDONE);
    }

    public void notifyContact(Contact contact, String message) {
        this.notificationService.notifyContact(contact, message);
        notifyObservers(EventType.NOTIFICATION_SENT);
    }

    public void addObserver(CRMObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(CRMObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers(EventType eventType) {
        observers.forEach(observer -> observer.onEvent(eventType));
    }
}
