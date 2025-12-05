package customer;

public class LostCustomer extends Customer {
    public LostCustomer(String name, String email, String phone) {
        super(name, email, phone);
    }

    @Override
    public String toString() {
        return String.format("{ Name: %s, ID: %d, Email: %s, Phone: %s }", getName(), getId(), getEmail(), getPhone());
    }
}
