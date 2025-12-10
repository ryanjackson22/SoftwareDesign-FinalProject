package crm.controller.command;

import crm.observer.event.EventType;
import customer.Customer;
import customer.Interaction;
import repository.CustomerRepository;
import repository.InMemoryCustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CreateCustomerCommandTest {

    private CustomerRepository repository;
    private CreateCustomerCommand command;

    @BeforeEach
    void setUp() {
        repository = new InMemoryCustomerRepository();
        command = new CreateCustomerCommand(repository);
    }

    @Test
    void testCreateCustomerRecordsInteraction() {
        // Arrange
        String input = "2\nTest User\ntest@example.com\n555-1234\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Act
        command.execute();

        // Assert
        List<Customer> customers = repository.getAllCustomers();
        assertEquals(1, customers.size(), "Should have one customer");

        Customer createdCustomer = customers.get(0);
        List<Interaction> history = createdCustomer.getInteractionHistory();

        assertEquals(1, history.size(), "Should have one interaction");
        assertEquals(EventType.CUSTOMER_CREATED, history.get(0).getEventType());
        assertEquals("Customer record created", history.get(0).getDetails());
    }

    @Test
    void testCreateMultipleCustomersRecordsMultipleInteractions() {
        // Arrange
        String input1 = "2\nUser One\nuser1@example.com\n555-0001\n";
        String input2 = "2\nUser Two\nuser2@example.com\n555-0002\n";

        // Act
        System.setIn(new ByteArrayInputStream(input1.getBytes()));
        command.execute();

        System.setIn(new ByteArrayInputStream(input2.getBytes()));
        command.execute();

        // Assert
        List<Customer> customers = repository.getAllCustomers();
        assertEquals(2, customers.size(), "Should have two customers");

        // Each customer should have their own interaction
        for (Customer customer : customers) {
            assertEquals(1, customer.getInteractionHistory().size(),
                    "Each customer should have one interaction");
            assertEquals(EventType.CUSTOMER_CREATED,
                    customer.getInteractionHistory().get(0).getEventType());
        }
    }

    @Test
    void testInteractionTimestampIsRecorded() {
        // Arrange
        String input = "2\nTest User\ntest@example.com\n555-1234\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Act
        command.execute();

        // Assert
        Customer createdCustomer = repository.getAllCustomers().get(0);
        Interaction interaction = createdCustomer.getInteractionHistory().get(0);

        assertNotNull(interaction.getTimestamp(), "Timestamp should not be null");
    }

    @Test
    void testDifferentCustomerTypesRecordInteractions() {
        // Arrange - Lead Customer
        String leadInput = "1\nLead User\nlead@example.com\n555-0001\n";
        System.setIn(new ByteArrayInputStream(leadInput.getBytes()));
        command.execute();

        // Arrange - VIP Customer
        String vipInput = "3\nVIP User\nvip@example.com\n555-0003\n";
        System.setIn(new ByteArrayInputStream(vipInput.getBytes()));
        command.execute();

        // Assert
        List<Customer> customers = repository.getAllCustomers();
        assertEquals(2, customers.size());

        // Both customer types should record interactions
        for (Customer customer : customers) {
            assertEquals(1, customer.getInteractionHistory().size());
            assertEquals(EventType.CUSTOMER_CREATED,
                    customer.getInteractionHistory().get(0).getEventType());
        }
    }
}
