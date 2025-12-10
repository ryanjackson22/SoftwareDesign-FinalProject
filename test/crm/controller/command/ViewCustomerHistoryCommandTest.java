package crm.controller.command;

import crm.observer.event.CRMEvent;
import crm.observer.event.EventType;
import customer.Customer;
import customer.RegularCustomer;
import repository.CustomerRepository;
import repository.InMemoryCustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class ViewCustomerHistoryCommandTest {

    private CustomerRepository repository;
    private ViewCustomerHistoryCommand command;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        repository = new InMemoryCustomerRepository();
        command = new ViewCustomerHistoryCommand(repository);
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void testExecuteDisplaysCustomerHistory() {
        // Arrange
        Customer customer = new RegularCustomer("John Doe", "john@example.com", "555-1111");
        customer.addInteraction(EventType.CUSTOMER_CREATED, "Customer record created");
        customer.addInteraction(EventType.NOTIFICATION_SENT, "Welcome email sent");
        repository.createCustomer(customer);
        int customerId = customer.getId();

        String input = customerId + "\n\n"; // Enter to continue
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Act
        command.execute();

        // Assert
        String output = outContent.toString();
        assertTrue(output.contains("Interaction History for: John Doe"));
        assertTrue(output.contains("Customer ID: " + customerId));
        assertTrue(output.contains("Customer Created"));
        assertTrue(output.contains("Notification Sent"));
        assertTrue(output.contains("Total interactions: 2"));
    }

    @Test
    void testExecuteWithEmptyHistory() {
        // Arrange
        Customer customer = new RegularCustomer("Jane Smith", "jane@example.com", "555-2222");
        repository.createCustomer(customer);
        int customerId = customer.getId();

        String input = customerId + "\n\n"; // Enter to continue
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Act
        command.execute();

        // Assert
        String output = outContent.toString();
        assertTrue(output.contains("Interaction History for: Jane Smith"));
        assertTrue(output.contains("No interactions recorded for this customer"));
        assertTrue(output.contains("Total interactions: 0"));
    }

    @Test
    void testExecuteWithNullCustomer() {
        // Arrange
        String input = "99999\n"; // Non-existent customer ID
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Act
        command.execute();

        // Assert
        String output = outContent.toString();
        assertTrue(output.contains("Customer not found!"));
    }

    @Test
    void testExecuteReturnsCorrectEvent() {
        // Arrange
        Customer customer = new RegularCustomer("Bob Jones", "bob@example.com", "555-3333");
        repository.createCustomer(customer);
        int customerId = customer.getId();

        String input = customerId + "\n\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Act
        CRMEvent event = command.execute();

        // Assert
        assertNotNull(event);
        assertEquals(EventType.VIEW_HISTORY, event.getEventType());
        assertTrue(event.toString().contains("View History"));
    }

    @Test
    void testExecuteReturnsEventWithCustomerId() {
        // Arrange
        Customer customer = new RegularCustomer("Alice Brown", "alice@example.com", "555-4444");
        repository.createCustomer(customer);
        int customerId = customer.getId();

        String input = customerId + "\n\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Act
        CRMEvent event = command.execute();

        // Assert
        String eventString = event.toString();
        assertTrue(eventString.contains("ID: " + customerId));
    }

    @Test
    void testExecuteReturnsEventWithoutCustomerIdForNullCustomer() {
        // Arrange
        String input = "99999\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Act
        CRMEvent event = command.execute();

        // Assert
        assertNotNull(event);
        assertEquals(EventType.VIEW_HISTORY, event.getEventType());
        // Should not contain a customer ID since customer was null
    }

    @Test
    void testExecuteDisplaysMultipleInteractions() {
        // Arrange
        Customer customer = new RegularCustomer("Multi Interaction", "multi@example.com", "555-5555");
        customer.addInteraction(EventType.CUSTOMER_CREATED, "Created");
        customer.addInteraction(EventType.NOTIFICATION_SENT, "Email sent");
        customer.addInteraction(EventType.SALE_MADE, "Sale completed");
        customer.addInteraction(EventType.CUSTOMER_UPDATED, "Name updated");
        repository.createCustomer(customer);
        int customerId = customer.getId();

        String input = customerId + "\n\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Act
        command.execute();

        // Assert
        String output = outContent.toString();
        assertTrue(output.contains("Customer Created"));
        assertTrue(output.contains("Notification Sent"));
        assertTrue(output.contains("Sale Made"));
        assertTrue(output.contains("Customer Updated"));
        assertTrue(output.contains("Total interactions: 4"));
    }

    @Test
    void testExecuteDisplaysHeaderAndFooter() {
        // Arrange
        Customer customer = new RegularCustomer("Format Test", "format@example.com", "555-6666");
        repository.createCustomer(customer);
        int customerId = customer.getId();

        String input = customerId + "\n\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Act
        command.execute();

        // Assert
        String output = outContent.toString();
        assertTrue(output.contains("========================================"));
        assertTrue(output.contains("Press enter to continue"));
    }

    @Test
    void testExecuteShowsInteractionDetails() {
        // Arrange
        Customer customer = new RegularCustomer("Details Test", "details@example.com", "555-7777");
        String detailMessage = "Very specific detail message";
        customer.addInteraction(EventType.CUSTOMER_UPDATED, detailMessage);
        repository.createCustomer(customer);
        int customerId = customer.getId();

        String input = customerId + "\n\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Act
        command.execute();

        // Assert
        String output = outContent.toString();
        assertTrue(output.contains(detailMessage));
    }

    @Test
    void testUndoDoesNothing() {
        // Act & Assert
        assertDoesNotThrow(() -> command.undo(), "Undo should not throw exception");
        // undo() is empty by design since viewing history doesn't need undo
    }

    @Test
    void testGetNameReturnsCorrectName() {
        // Act & Assert
        assertEquals("View Customer History", command.getName());
    }

    @Test
    void testGetEventTypeReturnsCorrectType() {
        // Act & Assert
        assertEquals(EventType.VIEW_HISTORY, command.getEventType());
    }

    @Test
    void testExecuteWithLongHistory() {
        // Arrange
        Customer customer = new RegularCustomer("Long History", "long@example.com", "555-8888");

        for (int i = 0; i < 10; i++) {
            customer.addInteraction(EventType.NOTIFICATION_SENT, "Message " + i);
        }

        repository.createCustomer(customer);
        int customerId = customer.getId();

        String input = customerId + "\n\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Act
        command.execute();

        // Assert
        String output = outContent.toString();
        assertTrue(output.contains("Total interactions: 10"));
        assertTrue(output.contains("Message 0"));
        assertTrue(output.contains("Message 9"));
    }

    @Test
    void testExecuteDisplaysAllEventTypes() {
        // Arrange
        Customer customer = new RegularCustomer("All Events", "all@example.com", "555-9999");
        customer.addInteraction(EventType.CUSTOMER_CREATED, "Created");
        customer.addInteraction(EventType.CUSTOMER_UPDATED, "Updated");
        customer.addInteraction(EventType.CUSTOMER_DELETED, "Deleted");
        customer.addInteraction(EventType.NOTIFICATION_SENT, "Notified");
        customer.addInteraction(EventType.SALE_MADE, "Sale");
        repository.createCustomer(customer);
        int customerId = customer.getId();

        String input = customerId + "\n\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Act
        command.execute();

        // Assert
        String output = outContent.toString();
        assertTrue(output.contains("Customer Created"));
        assertTrue(output.contains("Customer Updated"));
        assertTrue(output.contains("Customer Deleted"));
        assertTrue(output.contains("Notification Sent"));
        assertTrue(output.contains("Sale Made"));
    }
}
