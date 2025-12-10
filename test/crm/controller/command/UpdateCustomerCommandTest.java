package crm.controller.command;

import crm.observer.event.CRMEvent;
import crm.observer.event.EventType;
import customer.Customer;
import customer.Interaction;
import customer.RegularCustomer;
import repository.CustomerRepository;
import repository.InMemoryCustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UpdateCustomerCommandTest {

    private CustomerRepository repository;
    private UpdateCustomerCommand command;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        repository = new InMemoryCustomerRepository();
        command = new UpdateCustomerCommand(repository);
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void testExecuteUpdatesCustomerName() {
        // Arrange
        Customer customer = new RegularCustomer("Old Name", "email@example.com", "555-1111");
        repository.createCustomer(customer);
        int customerId = customer.getId();

        String input = customerId + "\n1\nNew Name\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Act
        command.execute();

        // Assert
        Customer updatedCustomer = repository.getCustomerFromId(customerId);
        assertEquals("New Name", updatedCustomer.getName());
        assertTrue(outContent.toString().contains("Customer " + customerId + " successfully updated!"));
    }

    @Test
    void testExecuteUpdatesCustomerEmail() {
        // Arrange
        Customer customer = new RegularCustomer("John Doe", "old@example.com", "555-2222");
        repository.createCustomer(customer);
        int customerId = customer.getId();

        String input = customerId + "\n2\nnew@example.com\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Act
        command.execute();

        // Assert
        Customer updatedCustomer = repository.getCustomerFromId(customerId);
        assertEquals("new@example.com", updatedCustomer.getEmail());
    }

    @Test
    void testExecuteUpdatesCustomerPhone() {
        // Arrange
        Customer customer = new RegularCustomer("Jane Smith", "jane@example.com", "555-0000");
        repository.createCustomer(customer);
        int customerId = customer.getId();

        String input = customerId + "\n3\n555-9999\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Act
        command.execute();

        // Assert
        Customer updatedCustomer = repository.getCustomerFromId(customerId);
        assertEquals("555-9999", updatedCustomer.getPhone());
    }

    @Test
    void testExecuteReturnsCorrectEventForNameUpdate() {
        // Arrange
        Customer customer = new RegularCustomer("Test User", "test@example.com", "555-3333");
        repository.createCustomer(customer);
        int customerId = customer.getId();

        String input = customerId + "\n1\nUpdated Name\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Act
        CRMEvent event = command.execute();

        // Assert
        assertNotNull(event);
        assertEquals(EventType.CUSTOMER_UPDATED, event.getEventType());
        assertTrue(event.toString().contains("Name updated"));
    }

    @Test
    void testExecuteReturnsCorrectEventForEmailUpdate() {
        // Arrange
        Customer customer = new RegularCustomer("Test User", "test@example.com", "555-4444");
        repository.createCustomer(customer);
        int customerId = customer.getId();

        String input = customerId + "\n2\nupdated@example.com\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Act
        CRMEvent event = command.execute();

        // Assert
        assertNotNull(event);
        assertEquals(EventType.CUSTOMER_UPDATED, event.getEventType());
        assertTrue(event.toString().contains("Email updated"));
    }

    @Test
    void testExecuteReturnsCorrectEventForPhoneUpdate() {
        // Arrange
        Customer customer = new RegularCustomer("Test User", "test@example.com", "555-5555");
        repository.createCustomer(customer);
        int customerId = customer.getId();

        String input = customerId + "\n3\n555-8888\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Act
        CRMEvent event = command.execute();

        // Assert
        assertNotNull(event);
        assertEquals(EventType.CUSTOMER_UPDATED, event.getEventType());
        assertTrue(event.toString().contains("Phone updated"));
    }

    @Test
    void testUpdateAddsInteractionToCustomerHistory() {
        // Arrange
        Customer customer = new RegularCustomer("History Test", "history@example.com", "555-6666");
        repository.createCustomer(customer);
        int customerId = customer.getId();

        String input = customerId + "\n1\nNew Name Value\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Act
        command.execute();

        // Assert
        Customer updatedCustomer = repository.getCustomerFromId(customerId);
        List<Interaction> history = updatedCustomer.getInteractionHistory();

        assertFalse(history.isEmpty(), "Customer should have interaction history");
        assertEquals(EventType.CUSTOMER_UPDATED, history.get(history.size() - 1).getEventType());
        assertTrue(history.get(history.size() - 1).getDetails().contains("Name updated to: New Name Value"));
    }

    @Test
    void testUpdateEmailAddsInteractionWithEmailDetails() {
        // Arrange
        Customer customer = new RegularCustomer("Email Test", "email@example.com", "555-7777");
        repository.createCustomer(customer);
        int customerId = customer.getId();

        String input = customerId + "\n2\nnewemail@example.com\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Act
        command.execute();

        // Assert
        Customer updatedCustomer = repository.getCustomerFromId(customerId);
        List<Interaction> history = updatedCustomer.getInteractionHistory();

        assertFalse(history.isEmpty());
        assertTrue(history.get(history.size() - 1).getDetails().contains("Email updated to: newemail@example.com"));
    }

    @Test
    void testUpdatePhoneAddsInteractionWithPhoneDetails() {
        // Arrange
        Customer customer = new RegularCustomer("Phone Test", "phone@example.com", "555-0000");
        repository.createCustomer(customer);
        int customerId = customer.getId();

        String input = customerId + "\n3\n555-1234\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Act
        command.execute();

        // Assert
        Customer updatedCustomer = repository.getCustomerFromId(customerId);
        List<Interaction> history = updatedCustomer.getInteractionHistory();

        assertFalse(history.isEmpty());
        assertTrue(history.get(history.size() - 1).getDetails().contains("Phone updated to: 555-1234"));
    }

    @Test
    void testMultipleUpdatesToSameCustomer() {
        // Arrange
        Customer customer = new RegularCustomer("Multi Update", "multi@example.com", "555-0001");
        repository.createCustomer(customer);
        int customerId = customer.getId();

        // Act - Update name
        String input1 = customerId + "\n1\nFirst Update\n";
        System.setIn(new ByteArrayInputStream(input1.getBytes()));
        command.execute();

        // Act - Update email
        String input2 = customerId + "\n2\nfirstupdate@example.com\n";
        System.setIn(new ByteArrayInputStream(input2.getBytes()));
        command.execute();

        // Assert
        Customer updatedCustomer = repository.getCustomerFromId(customerId);
        assertEquals("First Update", updatedCustomer.getName());
        assertEquals("firstupdate@example.com", updatedCustomer.getEmail());

        List<Interaction> history = updatedCustomer.getInteractionHistory();
        assertTrue(history.size() >= 2, "Should have at least 2 interactions");
    }

    @Test
    void testDefaultCaseUpdatesEmail() {
        // The switch statement has default case pointing to email update
        // Arrange
        Customer customer = new RegularCustomer("Default Test", "default@example.com", "555-0002");
        repository.createCustomer(customer);
        int customerId = customer.getId();

        String input = customerId + "\n99\ndefaultemail@example.com\n"; // Invalid choice, should default to email
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Act
        CRMEvent event = command.execute();

        // Assert
        Customer updatedCustomer = repository.getCustomerFromId(customerId);
        assertEquals("defaultemail@example.com", updatedCustomer.getEmail());
        assertTrue(event.toString().contains("Email updated"));
    }

    @Test
    void testGetNameReturnsCorrectName() {
        // Act & Assert
        assertEquals("Update Customer", command.getName());
    }

    @Test
    void testGetEventTypeReturnsCorrectType() {
        // Act & Assert
        assertEquals(EventType.CUSTOMER_UPDATED, command.getEventType());
    }

    @Test
    void testUndoDoesNotThrowException() {
        // Note: undo() is not fully implemented (has TODO), but should not crash
        // Arrange
        Customer customer = new RegularCustomer("Undo Test", "undo@example.com", "555-0003");
        repository.createCustomer(customer);
        int customerId = customer.getId();

        String input = customerId + "\n1\nNew Name\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Act
        command.execute();

        // Assert - Should not throw exception even though undo is incomplete
        assertDoesNotThrow(() -> command.undo(), "Undo should not throw exception");
    }
}
