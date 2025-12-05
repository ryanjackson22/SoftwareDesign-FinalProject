package customer;

import notification.NotificationStrategy;
import notification.SMSNotification;

import java.util.Date;

public abstract class Customer {
    public static int currentID = 64000;
    private int id;
    private String name;
    private String email;
    private String phone;
    private Date lastContact;
    private NotificationStrategy preferredContactMethod;

    public Customer(String name, String email, String phone) {
        this.id = assignCustomerId();
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.preferredContactMethod = new SMSNotification();
    }

    public void contact(String message) {
        preferredContactMethod.send(this,message);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    @Override
    public String toString() {
        return String.format("{ Name: %s, ID: %d, Email: %s, Phone: %s }", name, id, email, phone);
    }

}
