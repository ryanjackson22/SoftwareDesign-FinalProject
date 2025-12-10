package crm.controller.command;

import crm.observer.event.CRMEvent;
import crm.observer.event.EventType;
import customer.Customer;
import customer.Interaction;
import customer.RegularCustomer;
import notification.EmailNotification;
import notification.SMSNotification;
import repository.CustomerRepository;
import repository.InMemoryCustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ContactCustomerCommandTest {

    private CustomerRepository repository;
    private ContactCustomerCommand command;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        repository = new InMemoryCustomerRepository();
        command = new ContactCustomerCommand(repository);
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    /**
     * Helper method to set the preferredContact using reflection
     */
    private void setPreferredContact(Customer customer, notification.NotificationStrategy strategy) throws Exception {
        Field field = Customer.class.getDeclaredField("preferredContact");
        field.setAccessible(true);
        field.set(customer, strategy);
    }

    @Test
    void testExecuteContactsCustomerWithMessage() throws Exception {
        // Arrange
        Customer customer = new RegularCustomer("John Doe", "john@example.com", "555-1111");
        customer.setPreferredContactMethod(new EmailNotification());
//        setPreferredContact(customer, new EmailNotification());
        repository.createCustomer(customer);
        int customerId = customer.getId();

        String input = customerId + "\nHello, this is a test message\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Act
        command.execute();

        // Assert
        String output = outContent.toString();
        assertTrue(output.contains("Notification sent successfully!"));
        assertTrue(output.contains("Contacting: John Doe"));
    }

    @Test
    void testExecuteReturnsCorrectEvent() throws Exception {
        // Arrange
        Customer customer = new RegularCustomer("Jane Smith", "jane@example.com", "555-2222");
        customer.setPreferredContactMethod(new SMSNotification());
//        setPreferredContact(customer, new SMSNotification());
        repository.createCustomer(customer);
        int customerId = customer.getId();

        String input = customerId + "\nTest message\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Act
        CRMEvent event = command.execute();

        // Assert
        assertNotNull(event);
        assertEquals(EventType.NOTIFICATION_SENT, event.getEventType());
        assertTrue(event.toString().contains("Notification Sent"));
    }

    @Test
    void testExecuteWithNullCustomerReturnsEmptyEvent() {
        // Arrange
        String input = "99999\n"; // Non-existent customer ID
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Act
        CRMEvent event = command.execute();

        // Assert
        assertNotNull(event);
        assertEquals(EventType.NOTIFICATION_SENT, event.getEventType());
        String output = outContent.toString();
        assertTrue(output.contains("Customer not found!"));
    }

    @Test
    void testExecuteAddsInteractionToCustomerHistory() throws Exception {
        // Arrange
        Customer customer = new RegularCustomer("Bob Jones", "bob@example.com", "555-3333");
        customer.setPreferredContactMethod(new EmailNotification());
//        setPreferredContact(customer, new EmailNotification(customer.getEmail()));
        repository.createCustomer(customer);
        int customerId = customer.getId();

        String message = "Important notification message";
        String input = customerId + "\n" + message + "\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Act
        command.execute();

        // Assert
        Customer contactedCustomer = repository.getCustomerFromId(customerId);
        List<Interaction> history = contactedCustomer.getInteractionHistory();

        assertFalse(history.isEmpty(), "Customer should have interaction history");

        Interaction lastInteraction = history.get(history.size() - 1);
        assertEquals(EventType.NOTIFICATION_SENT, lastInteraction.getEventType());
        assertTrue(lastInteraction.getDetails().contains(message));
        assertTrue(lastInteraction.getDetails().contains("Contacted via"));
    }

    @Test
    void testExecuteDisplaysPreferredContactMethod() throws Exception {
        // Arrange
        Customer customer = new RegularCustomer("Alice Brown", "alice@example.com", "555-4444");
        customer.setPreferredContactMethod(new EmailNotification());
//        setPreferredContact(customer, new EmailNotification(customer.getEmail()));
        repository.createCustomer(customer);
        int customerId = customer.getId();

        String input = customerId + "\nTest message\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Act
        command.execute();

        // Assert
        String output = outContent.toString();
        assertTrue(output.contains("Preferred Method:"));
        assertTrue(output.contains("EmailNotification"));
    }

    @Test
    void testExecuteWithSMSNotification() throws Exception {
        // Arrange
        Customer customer = new RegularCustomer("SMS User", "sms@example.com", "555-5555");
        customer.setPreferredContactMethod(new SMSNotification());
//        setPreferredContact(customer, new SMSNotification(customer.getPhone()));
        repository.createCustomer(customer);
        int customerId = customer.getId();

        String input = customerId + "\nSMS message\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Act
        CRMEvent event = command.execute();

        // Assert
        String output = outContent.toString();
        assertTrue(output.contains("SMSNotification"));
        assertTrue(event.toString().contains("Contact Method: SMSNotification"));
    }

    @Test
    void testExecuteTracksCustomerIdForUndo() throws Exception {
        // Arrange
        Customer customer = new RegularCustomer("Undo Test", "undo@example.com", "555-6666");
        customer.setPreferredContactMethod(new EmailNotification());
//        setPreferredContact(customer, new EmailNotification(customer.getEmail()));
        repository.createCustomer(customer);
        int customerId = customer.getId();

        String input = customerId + "\nTest message\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Act
        command.execute();

        // Assert - Undo should not throw exception if ID was tracked
        assertDoesNotThrow(() -> command.undo());
    }

    @Test
    void testUndoDisplaysCannotUndoMessage() throws Exception {
        // Arrange
        Customer customer = new RegularCustomer("Cannot Undo", "cannotundo@example.com", "555-7777");
        customer.setPreferredContactMethod(new EmailNotification());
//        setPreferredContact(customer, new EmailNotification(customer.getEmail()));
        repository.createCustomer(customer);
        int customerId = customer.getId();

        String input = customerId + "\nMessage\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Act
        command.execute();
        outContent.reset(); // Clear output
        command.undo();

        // Assert
        String output = outContent.toString();
        assertTrue(output.contains("cannot be undone") || output.contains("already sent"));
    }

    @Test
    void testUndoOnEmptyStack() {
        // Act
        outContent.reset();
        command.undo();

        // Assert
        String output = outContent.toString();
        assertTrue(output.contains("No notifications to undo"));
    }

    @Test
    void testMultipleContactsAndUndos() throws Exception {
        // Arrange
        Customer customer1 = new RegularCustomer("Customer 1", "c1@example.com", "555-0001");
        Customer customer2 = new RegularCustomer("Customer 2", "c2@example.com", "555-0002");
        customer1.setPreferredContactMethod(new EmailNotification());
        customer2.setPreferredContactMethod(new EmailNotification());
//        setPreferredContact(customer1, new EmailNotification(customer1.getEmail()));
//        setPreferredContact(customer2, new EmailNotification(customer2.getEmail()));
        repository.createCustomer(customer1);
        repository.createCustomer(customer2);

        int id1 = customer1.getId();
        int id2 = customer2.getId();

        // Act - Contact both customers
        System.setIn(new ByteArrayInputStream((id1 + "\nMessage 1\n").getBytes()));
        command.execute();

        System.setIn(new ByteArrayInputStream((id2 + "\nMessage 2\n").getBytes()));
        command.execute();

        // Assert - Both should have interactions
        assertTrue(repository.getCustomerFromId(id1).getInteractionHistory().size() > 0);
        assertTrue(repository.getCustomerFromId(id2).getInteractionHistory().size() > 0);

        // Act - Undo both (LIFO order)
        assertDoesNotThrow(() -> command.undo());
        assertDoesNotThrow(() -> command.undo());
    }

    @Test
    void testGetNameReturnsCorrectName() {
        // Act & Assert
        assertEquals("Send Notification", command.getName());
    }

    @Test
    void testGetEventTypeReturnsCorrectType() {
        // Act & Assert
        assertEquals(EventType.NOTIFICATION_SENT, command.getEventType());
    }

    @Test
    void testEventContainsContactMethodInformation() throws Exception {
        // Arrange
        Customer customer = new RegularCustomer("Event Test", "event@example.com", "555-8888");
        customer.setPreferredContactMethod(new EmailNotification());
//        setPreferredContact(customer, new EmailNotification(customer.getEmail()));
        repository.createCustomer(customer);
        int customerId = customer.getId();

        String input = customerId + "\nTest\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Act
        CRMEvent event = command.execute();

        // Assert
        String eventString = event.toString();
        assertTrue(eventString.contains("Contact Method:"));
        assertTrue(eventString.contains("EmailNotification") || eventString.contains("SMSNotification"));
    }
}
