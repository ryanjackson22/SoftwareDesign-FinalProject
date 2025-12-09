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

class DeleteCustomerCommandTest {

    private CustomerRepository repository;
    private DeleteCustomerCommand command;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        repository = new InMemoryCustomerRepository();
        command = new DeleteCustomerCommand(repository);
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void testExecuteDeletesCustomerFromRepository() {
        // Arrange
        Customer customer = new RegularCustomer("John Doe", "john@example.com", "555-1111");
        repository.createCustomer(customer);
        int customerId = customer.getId();

        String input = customerId + "\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Act
        command.execute();

        // Assert
        assertNull(repository.getCustomerFromId(customerId), "Customer should be deleted from repository");
        assertTrue(outContent.toString().contains("Customer " + customerId + " deleted successfully!"));
    }

    @Test
    void testExecuteReturnsCorrectEvent() {
        // Arrange
        Customer customer = new RegularCustomer("Jane Smith", "jane@example.com", "555-2222");
        repository.createCustomer(customer);
        int customerId = customer.getId();

        String input = customerId + "\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Act
        CRMEvent event = command.execute();

        // Assert
        assertNotNull(event, "Event should not be null");
        assertEquals(EventType.CUSTOMER_DELETED, event.getEventType());
        assertTrue(event.toString().contains("Customer Deleted"));
        assertTrue(event.toString().contains("Customer deleted"));
    }

    @Test
    void testUndoRestoresDeletedCustomer() {
        // Arrange
        Customer customer = new RegularCustomer("Bob Jones", "bob@example.com", "555-3333");
        repository.createCustomer(customer);
        int customerId = customer.getId();
        String customerName = customer.getName();

        String input = customerId + "\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Act
        command.execute();
        assertNull(repository.getCustomerFromId(customerId), "Customer should be deleted");

        command.undo();

        // Assert
        Customer restoredCustomer = repository.getCustomerFromId(customerId);
        assertNotNull(restoredCustomer, "Customer should be restored");
        assertEquals(customerId, restoredCustomer.getId());
        assertEquals(customerName, restoredCustomer.getName());
    }

    @Test
    void testUndoPrintsCorrectMessage() {
        // Arrange
        Customer customer = new RegularCustomer("Alice Brown", "alice@example.com", "555-4444");
        repository.createCustomer(customer);
        int customerId = customer.getId();

        String input = customerId + "\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Act
        command.execute();
        outContent.reset(); // Clear output before undo
        command.undo();

        // Assert
        String output = outContent.toString();
        assertTrue(output.contains("Customer " + customerId + " deletion successfully undone!"));
    }

    @Test
    void testMultipleDeletesAndUndos() {
        // Arrange
        Customer customer1 = new RegularCustomer("Customer 1", "c1@example.com", "555-0001");
        Customer customer2 = new RegularCustomer("Customer 2", "c2@example.com", "555-0002");
        repository.createCustomer(customer1);
        repository.createCustomer(customer2);

        int id1 = customer1.getId();
        int id2 = customer2.getId();

        // Act - Delete customer 1
        System.setIn(new ByteArrayInputStream((id1 + "\n").getBytes()));
        command.execute();

        // Act - Delete customer 2
        System.setIn(new ByteArrayInputStream((id2 + "\n").getBytes()));
        command.execute();

        // Assert - Both deleted
        assertNull(repository.getCustomerFromId(id1));
        assertNull(repository.getCustomerFromId(id2));

        // Act - Undo customer 2 deletion (LIFO)
        command.undo();

        // Assert - Customer 2 restored, customer 1 still deleted
        assertNull(repository.getCustomerFromId(id1), "Customer 1 should still be deleted");
        assertNotNull(repository.getCustomerFromId(id2), "Customer 2 should be restored");

        // Act - Undo customer 1 deletion
        command.undo();

        // Assert - Both restored
        assertNotNull(repository.getCustomerFromId(id1), "Customer 1 should be restored");
        assertNotNull(repository.getCustomerFromId(id2), "Customer 2 should be restored");
    }

    @Test
    void testGetNameReturnsCorrectName() {
        // Act & Assert
        assertEquals("Delete Customer", command.getName());
    }

    @Test
    void testGetEventTypeReturnsCorrectType() {
        // Act & Assert
        assertEquals(EventType.CUSTOMER_DELETED, command.getEventType());
    }

    @Test
    void testDeleteCustomerPushedToStack() {
        // Arrange
        Customer customer = new RegularCustomer("Test Customer", "test@example.com", "555-5555");
        repository.createCustomer(customer);
        int customerId = customer.getId();

        String input = customerId + "\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Act
        command.execute();

        // Assert - If undo works, the customer was successfully pushed to stack
        assertDoesNotThrow(() -> command.undo(), "Undo should not throw exception if customer was pushed to stack");
    }

    @Test
    void testUndoOnlyPopsStackOnce() {
        // This test verifies the bug fix where undo() was popping twice
        // Arrange
        Customer customer = new RegularCustomer("Single Pop Test", "single@example.com", "555-6666");
        repository.createCustomer(customer);
        int customerId = customer.getId();

        String input = customerId + "\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Act
        command.execute();

        // Assert - Should not throw exception (would throw EmptyStackException if popping twice)
        assertDoesNotThrow(() -> command.undo(), "Undo should only pop once and not cause EmptyStackException");

        // Verify customer was restored
        assertNotNull(repository.getCustomerFromId(customerId), "Customer should be restored after undo");
    }
}
