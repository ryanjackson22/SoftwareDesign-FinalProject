package customer;

import crm.observer.event.EventType;
import notification.NotificationStrategy;
import notification.SMSNotification;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class Customer {
    public static int currentID = 64000;

    private final int id;
    private String name;
    private String email;
    private String phone;
    private Date lastContact;
    private NotificationStrategy preferredContactMethod;
    private List<Interaction> interactionHistory;

    public abstract String toString();

    public Customer(String name, String email, String phone) {
        this.id = assignCustomerId();
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.lastContact = null;
        this.preferredContactMethod = new SMSNotification();
        this.interactionHistory = new ArrayList<>();
    }

    public void contact(String message) {
        preferredContactMethod.send(this,message);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getLastContact() {
        return lastContact;
    }

    public void setLastContact(Date lastContact) {
        this.lastContact = lastContact;
    }

    public NotificationStrategy getPreferredContactMethod() {
        return preferredContactMethod;
    }

    public void setPreferredContactMethod(NotificationStrategy preferredContactMethod) {
        this.preferredContactMethod = preferredContactMethod;
    }

    private int assignCustomerId() {
        currentID++;
        return currentID;
    }

    public void addInteraction(EventType eventType, String details) {
        interactionHistory.add(new Interaction(eventType, details));
    }

    public List<Interaction> getInteractionHistory() {
        return new ArrayList<>(interactionHistory);
    }
}
