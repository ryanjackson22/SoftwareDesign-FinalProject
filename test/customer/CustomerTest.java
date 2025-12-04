package customer;

import notification.EmailNotification;
import notification.NotificationStrategy;
import notification.SMSNotification;
import notification.PushNotification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    private Customer testCustomer;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        // Create a concrete test customer using anonymous class
        testCustomer = new Customer("0", "John Smith", "customer@example.com", "555-9999") {};

        // Redirect System.out to capture console output
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        // Restore original System.out
        System.setOut(originalOut);
    }

    /**
     * Helper method to set the preferredContact using reflection
     * since there's no public setter for it
     */
    private void setPreferredContact(Customer customer, NotificationStrategy strategy) throws Exception {
        Field field = Customer.class.getDeclaredField("preferredContact");
        field.setAccessible(true);
        field.set(customer, strategy);
    }

    @Test
    void testContactWithEmailNotification() throws Exception {
        // Arrange
        testCustomer.setPreferredContactMethod(new EmailNotification());
        String message = "Test email message";

        // Act
        testCustomer.contact(message);

        // Assert
        String output = outContent.toString().trim();
        assertTrue(output.contains("customer@example.com"),
                "Contact method should send to customer's email");
        assertTrue(output.contains(message),
                "Contact method should include the message");
    }

    @Test
    void testContactWithSMSNotification() throws Exception {
        // Arrange
        testCustomer.setPreferredContactMethod(new SMSNotification());
        String message = "Test SMS message";

        // Act
        testCustomer.contact(message);

        // Assert
        String output = outContent.toString().trim();
        assertTrue(output.contains("555-9999"),
                "Contact method should send to customer's phone");
        assertTrue(output.contains(message),
                "Contact method should include the message");
    }

    @Test
    void testContactWithPushNotification() throws Exception {
        // Arrange
        testCustomer.setPreferredContactMethod(new PushNotification());
        String message = "Test push message";

        // Act
        testCustomer.contact(message);

        // Assert
        String output = outContent.toString().trim();
        assertTrue(output.contains("Sent Push Notification:"),
                "Contact method should use push notification");
        assertTrue(output.contains(message),
                "Contact method should include the message");
    }

    @Test
    void testContactWithEmptyMessage() throws Exception {
        // Arrange
        testCustomer.setPreferredContactMethod(new EmailNotification());
        String message = "";

        // Act
        testCustomer.contact(message);

        // Assert
        String output = outContent.toString().trim();
        assertTrue(output.contains("customer@example.com"),
                "Contact method should send even with empty message");
    }

    @Test
    void testContactWithDifferentStrategies() throws Exception {
        // Arrange
        String message = "Switching strategies";

        // Test with Email
        testCustomer.setPreferredContactMethod(new EmailNotification());
        testCustomer.contact(message);
        assertTrue(outContent.toString().contains("customer@example.com"));

        // Reset output stream
        outContent.reset();

        // Test with SMS
        testCustomer.setPreferredContactMethod(new SMSNotification());
        testCustomer.contact(message);
        assertTrue(outContent.toString().contains("555-9999"));
    }
}
