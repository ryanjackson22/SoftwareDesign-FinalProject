package customer;

import notification.NotificationStrategy;

import java.util.Date;

public abstract class Customer {
    private String id;
    private String name;
    private String email;
    private String phone;
    private Date lastContact;
    private NotificationStrategy preferredContact;

    public void contact(String message) {
        // stub
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
}
