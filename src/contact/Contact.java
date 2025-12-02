package contact;

import notification.EmailNotification;
import notification.NotificationStrategy;

import java.util.Date;

public abstract class Contact {
    private String id;
    private String name;
    private String email;
    private String phone;
    private Date lastContact;
    private NotificationStrategy preferredNotificationMethod;

    public Contact(String id, String name, String email, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.lastContact = new Date();
        this.preferredNotificationMethod = new EmailNotification();
    }

    public void contact(String message) {
        preferredNotificationMethod.send(this, message);
        lastContact = new Date();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public NotificationStrategy getPreferredNotificationMethod() {
        return preferredNotificationMethod;
    }

    public void setPreferredNotificationMethod(NotificationStrategy preferredNotificationMethod) {
        this.preferredNotificationMethod = preferredNotificationMethod;
    }
}
