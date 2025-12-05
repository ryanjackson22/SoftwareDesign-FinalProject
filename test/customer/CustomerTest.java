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
        // Create a concrete test customer using RegularCustomer
        testCustomer = new RegularCustomer("John Smith", "customer@example.com", "555-9999");

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

    @Test
    void testToStringFormat() {
        // Arrange & Act
        String result = testCustomer.toString();

        // Assert
        assertTrue(result.startsWith("{"), "toString should start with '{'");
        assertTrue(result.endsWith("}"), "toString should end with '}'");
        assertTrue(result.contains("Name: John Smith"), "toString should contain customer name");
        assertTrue(result.contains("Email: customer@example.com"), "toString should contain customer email");
        assertTrue(result.contains("Phone: 555-9999"), "toString should contain customer phone");
        assertTrue(result.contains("ID:"), "toString should contain ID field");
    }

    @Test
    void testToStringWithDifferentCustomerTypes() {
        // Test with VIPCustomer
        Customer vipCustomer = new VIPCustomer("Jane Doe", "jane@vip.com", "(800)123-4567");
        String vipResult = vipCustomer.toString();
        assertTrue(vipResult.contains("Name: Jane Doe"));
        assertTrue(vipResult.contains("Email: jane@vip.com"));
        assertTrue(vipResult.contains("Phone: (800)123-4567"));
        assertTrue(vipResult.contains("ID:"));

        // Test with LeadCustomer
        Customer leadCustomer = new LeadCustomer("Bob Smith", "bob@lead.com", "555-1111");
        String leadResult = leadCustomer.toString();
        assertTrue(leadResult.contains("Name: Bob Smith"));
        assertTrue(leadResult.contains("Email: bob@lead.com"));
        assertTrue(leadResult.contains("Phone: 555-1111"));
        assertTrue(leadResult.contains("ID:"));
    }

    @Test
    void testToStringContainsAllRequiredFields() {
        // Arrange
        Customer customer = new RegularCustomer("Test User", "test@example.com", "(555)000-0000");

        // Act
        String result = customer.toString();

        // Assert - verify all four fields are present in correct format
        assertTrue(result.matches(".*\\{ Name: .+, ID: \\d+, Email: .+, Phone: .+ \\}.*"),
                "toString should match format: { Name: X, ID: Y, Email: Z, Phone: W }");
    }

    @Test
    void testToStringIncrementsCustomerId() {
        // Arrange
        Customer customer1 = new RegularCustomer("User One", "user1@test.com", "111-1111");
        Customer customer2 = new RegularCustomer("User Two", "user2@test.com", "222-2222");

        // Act
        String result1 = customer1.toString();
        String result2 = customer2.toString();

        // Assert - customer2's ID should be greater than customer1's ID
        int id1 = extractIdFromToString(result1);
        int id2 = extractIdFromToString(result2);
        assertTrue(id2 > id1, "Second customer should have a higher ID than first customer");
    }

    /**
     * Helper method to extract ID from toString output
     */
    private int extractIdFromToString(String toStringResult) {
        // Extract the ID value from the format "{ Name: X, ID: Y, Email: Z, Phone: W }"
        String[] parts = toStringResult.split("ID: ");
        if (parts.length > 1) {
            String idPart = parts[1].split(",")[0].trim();
            return Integer.parseInt(idPart);
        }
        return -1;
    }
}
