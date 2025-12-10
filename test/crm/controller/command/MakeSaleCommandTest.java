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

class MakeSaleCommandTest {

    private CustomerRepository repository;
    private MakeSaleCommand command;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        repository = new InMemoryCustomerRepository();
        command = new MakeSaleCommand(repository);
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void testExecuteRecordsSaleInteraction() {
        // Arrange
        Customer customer = new RegularCustomer("John Doe", "john@example.com", "555-1111");
        repository.createCustomer(customer);
        int customerId = customer.getId();

        String input = customerId + "\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Act
        command.execute();

        // Assert
        Customer saleCustomer = repository.getCustomerFromId(customerId);
        List<Interaction> history = saleCustomer.getInteractionHistory();

        assertFalse(history.isEmpty(), "Customer should have interaction history");

        Interaction saleInteraction = history.get(history.size() - 1);
        assertEquals(EventType.SALE_MADE, saleInteraction.getEventType());
        assertEquals("Sale completed", saleInteraction.getDetails());
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
        assertNotNull(event);
        assertEquals(EventType.SALE_MADE, event.getEventType());
        assertTrue(event.toString().contains("Sale Made"));
        assertTrue(event.toString().contains("Sale completed"));
    }

    @Test
    void testExecutePrintsSuccessMessage() {
        // Arrange
        Customer customer = new RegularCustomer("Bob Jones", "bob@example.com", "555-3333");
        repository.createCustomer(customer);
        int customerId = customer.getId();
        String customerName = customer.getName();

        String input = customerId + "\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Act
        command.execute();

        // Assert
        String output = outContent.toString();
        assertTrue(output.contains("Sale with " + customerName + " made!"));
    }

    @Test
    void testExecuteEventContainsCustomerId() {
        // Arrange
        Customer customer = new RegularCustomer("Alice Brown", "alice@example.com", "555-4444");
        repository.createCustomer(customer);
        int customerId = customer.getId();

        String input = customerId + "\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Act
        CRMEvent event = command.execute();

        // Assert
        String eventString = event.toString();
        assertTrue(eventString.contains("ID: " + customerId));
    }

    @Test
    void testMultipleSalesToSameCustomer() {
        // Arrange
        Customer customer = new RegularCustomer("Multi Sale", "multi@example.com", "555-5555");
        repository.createCustomer(customer);
        int customerId = customer.getId();

        String input = customerId + "\n";

        // Act
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        command.execute();

        System.setIn(new ByteArrayInputStream(input.getBytes()));
        command.execute();

        // Assert
        Customer saleCustomer = repository.getCustomerFromId(customerId);
        List<Interaction> history = saleCustomer.getInteractionHistory();

        long saleCount = history.stream()
                .filter(i -> i.getEventType() == EventType.SALE_MADE)
                .count();

        assertEquals(2, saleCount, "Customer should have 2 sale interactions");
    }

    @Test
    void testMultipleSalesToDifferentCustomers() {
        // Arrange
        Customer customer1 = new RegularCustomer("Customer 1", "c1@example.com", "555-0001");
        Customer customer2 = new RegularCustomer("Customer 2", "c2@example.com", "555-0002");
        repository.createCustomer(customer1);
        repository.createCustomer(customer2);

        int id1 = customer1.getId();
        int id2 = customer2.getId();

        // Act
        System.setIn(new ByteArrayInputStream((id1 + "\n").getBytes()));
        command.execute();

        System.setIn(new ByteArrayInputStream((id2 + "\n").getBytes()));
        command.execute();

        // Assert
        assertTrue(repository.getCustomerFromId(id1).getInteractionHistory().stream()
                .anyMatch(i -> i.getEventType() == EventType.SALE_MADE));
        assertTrue(repository.getCustomerFromId(id2).getInteractionHistory().stream()
                .anyMatch(i -> i.getEventType() == EventType.SALE_MADE));
    }

    @Test
    void testUndoPrintsUndoMessage() {
        // Arrange
        Customer customer = new RegularCustomer("Undo Test", "undo@example.com", "555-6666");
        repository.createCustomer(customer);
        int customerId = customer.getId();
        String customerName = customer.getName();

        String input = customerId + "\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Act
        command.execute();
        outContent.reset(); // Clear output
        command.undo();

        // Assert
        String output = outContent.toString();
        assertTrue(output.contains("Sale with " + customerName + " successfully undone!"));
    }

    @Test
    void testUndoDoesNotThrowException() {
        // Arrange
        Customer customer = new RegularCustomer("No Exception", "noex@example.com", "555-7777");
        repository.createCustomer(customer);
        int customerId = customer.getId();

        String input = customerId + "\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Act & Assert
        command.execute();
        assertDoesNotThrow(() -> command.undo(), "Undo should not throw exception");
    }

    @Test
    void testMultipleSalesAndUndos() {
        // Arrange
        Customer customer1 = new RegularCustomer("Customer 1", "c1@example.com", "555-0001");
        Customer customer2 = new RegularCustomer("Customer 2", "c2@example.com", "555-0002");
        repository.createCustomer(customer1);
        repository.createCustomer(customer2);

        int id1 = customer1.getId();
        int id2 = customer2.getId();

        // Act - Make sales
        System.setIn(new ByteArrayInputStream((id1 + "\n").getBytes()));
        command.execute();

        System.setIn(new ByteArrayInputStream((id2 + "\n").getBytes()));
        command.execute();

        // Act - Undo in LIFO order
        outContent.reset();
        command.undo(); // Should undo customer2 sale

        // Assert
        String output = outContent.toString();
        assertTrue(output.contains(customer2.getName()));

        // Act - Undo again
        outContent.reset();
        command.undo(); // Should undo customer1 sale

        // Assert
        output = outContent.toString();
        assertTrue(output.contains(customer1.getName()));
    }

    @Test
    void testGetNameReturnsCorrectName() {
        // Act & Assert
        assertEquals("Make Sale", command.getName());
    }

    @Test
    void testGetEventTypeReturnsCorrectType() {
        // Act & Assert
        assertEquals(EventType.SALE_MADE, command.getEventType());
    }

    @Test
    void testSaleInteractionHasTimestamp() {
        // Arrange
        Customer customer = new RegularCustomer("Timestamp Test", "timestamp@example.com", "555-8888");
        repository.createCustomer(customer);
        int customerId = customer.getId();

        String input = customerId + "\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Act
        command.execute();

        // Assert
        Customer saleCustomer = repository.getCustomerFromId(customerId);
        Interaction saleInteraction = saleCustomer.getInteractionHistory().get(
                saleCustomer.getInteractionHistory().size() - 1
        );

        assertNotNull(saleInteraction.getTimestamp(), "Sale interaction should have a timestamp");
    }

    @Test
    void testSaleStackTracking() {
        // Arrange
        Customer customer = new RegularCustomer("Stack Test", "stack@example.com", "555-9999");
        repository.createCustomer(customer);
        int customerId = customer.getId();

        // Act - Execute multiple times, resetting input each time
        System.setIn(new ByteArrayInputStream((customerId + "\n").getBytes()));
        command.execute();

        System.setIn(new ByteArrayInputStream((customerId + "\n").getBytes()));
        command.execute();

        System.setIn(new ByteArrayInputStream((customerId + "\n").getBytes()));
        command.execute();

        // Assert - Should be able to undo all without exception
        assertDoesNotThrow(() -> command.undo());
        assertDoesNotThrow(() -> command.undo());
        assertDoesNotThrow(() -> command.undo());
    }
}
