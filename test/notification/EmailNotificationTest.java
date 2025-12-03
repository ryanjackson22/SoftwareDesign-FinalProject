package notification;

import customer.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class EmailNotificationTest {

    private EmailNotification emailNotification;
    private Customer testCustomer;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        emailNotification = new EmailNotification();

        // Create a test customer using anonymous class
        testCustomer = new Customer("0", "John Smith", "johnsmith@test.com", "(111)555-5555") {
            {
                setEmail("test@example.com");
                setPhone("555-1234");
            }
        };

        // Redirect System.out to capture console output
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        // Restore original System.out
        System.setOut(originalOut);
    }

    @Test
    void testSendEmailNotification() {
        // Arrange
        String message = "Hello, this is a test email";

        // Act
        emailNotification.send(testCustomer, message);

        // Assert
        String expectedOutput = "Sent: 'Hello, this is a test email' to test@example.com";
        assertTrue(outContent.toString().trim().contains(expectedOutput),
                "Email notification should print the correct message");
    }

    @Test
    void testSendEmailWithEmptyMessage() {
        // Arrange
        String message = "";

        // Act
        emailNotification.send(testCustomer, message);

        // Assert
        String expectedOutput = "Sent: '' to test@example.com";
        assertTrue(outContent.toString().trim().contains(expectedOutput),
                "Email notification should handle empty messages");
    }

    @Test
    void testSendEmailToCustomerWithEmail() {
        // Arrange
        String message = "Important notification";

        // Act
        emailNotification.send(testCustomer, message);
        String output = outContent.toString().trim();

        // Assert
        assertTrue(output.contains("test@example.com"),
                "Email notification should include customer's email address");
        assertTrue(output.contains(message),
                "Email notification should include the message content");
    }

    @Test
    void testNotificationStrategyInterface() {
        // Assert
        assertInstanceOf(NotificationStrategy.class, emailNotification, "EmailNotification should implement NotificationStrategy interface");
    }
}
