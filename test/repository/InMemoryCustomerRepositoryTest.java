package repository;

import customer.Customer;
import customer.RegularCustomer;
import customer.LeadCustomer;
import customer.VIPCustomer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryCustomerRepositoryTest {

    private InMemoryCustomerRepository repository;
    private Customer testCustomer1;
    private Customer testCustomer2;

    @BeforeEach
    void setUp() {
        repository = new InMemoryCustomerRepository();
        testCustomer1 = new RegularCustomer("John Doe", "john@example.com", "555-1111");
        testCustomer2 = new VIPCustomer("Jane Smith", "jane@example.com", "555-2222");
    }

    @Test
    void testCreateCustomerAddsToRepository() {
        // Act
        repository.createCustomer(testCustomer1);

        // Assert
        List<Customer> customers = repository.getAllCustomers();
        assertEquals(1, customers.size(), "Repository should contain one customer");
        assertEquals(testCustomer1, customers.get(0));
    }

    @Test
    void testCreateMultipleCustomers() {
        // Act
        repository.createCustomer(testCustomer1);
        repository.createCustomer(testCustomer2);

        // Assert
        List<Customer> customers = repository.getAllCustomers();
        assertEquals(2, customers.size(), "Repository should contain two customers");
        assertTrue(customers.contains(testCustomer1));
        assertTrue(customers.contains(testCustomer2));
    }

    @Test
    void testGetCustomerFromIdReturnsCorrectCustomer() {
        // Arrange
        repository.createCustomer(testCustomer1);
        repository.createCustomer(testCustomer2);

        // Act
        Customer found = repository.getCustomerFromId(testCustomer1.getId());

        // Assert
        assertNotNull(found, "Customer should be found");
        assertEquals(testCustomer1.getId(), found.getId());
        assertEquals(testCustomer1.getName(), found.getName());
    }

    @Test
    void testGetCustomerFromIdReturnsNullForNonExistent() {
        // Arrange
        repository.createCustomer(testCustomer1);

        // Act
        Customer found = repository.getCustomerFromId(99999);

        // Assert
        assertNull(found, "Non-existent customer should return null");
    }

    @Test
    void testGetCustomerFromIdOnEmptyRepository() {
        // Act
        Customer found = repository.getCustomerFromId(1);

        // Assert
        assertNull(found, "Empty repository should return null");
    }

    @Test
    void testDeleteCustomerRemovesFromRepository() {
        // Arrange
        repository.createCustomer(testCustomer1);
        repository.createCustomer(testCustomer2);

        // Act
        repository.deleteCustomer(testCustomer1);

        // Assert
        List<Customer> customers = repository.getAllCustomers();
        assertEquals(1, customers.size(), "Repository should contain one customer after deletion");
        assertFalse(customers.contains(testCustomer1), "Deleted customer should not be in repository");
        assertTrue(customers.contains(testCustomer2), "Non-deleted customer should remain");
    }

    @Test
    void testDeleteNonExistentCustomerDoesNotThrow() {
        // Arrange
        repository.createCustomer(testCustomer1);
        Customer nonExistentCustomer = new RegularCustomer("Nobody", "nobody@example.com", "000-0000");

        // Act & Assert
        assertDoesNotThrow(() -> repository.deleteCustomer(nonExistentCustomer),
                "Deleting non-existent customer should not throw exception");
    }

    @Test
    void testUpdateCustomerReplacesOldCustomer() {
        // Arrange
        repository.createCustomer(testCustomer1);
        int originalId = testCustomer1.getId();
        String originalName = testCustomer1.getName();

        // Modify the customer
        testCustomer1.setName("John Updated");

        // Act
        repository.updateCustomer(testCustomer1);

        // Assert
        Customer updatedCustomer = repository.getCustomerFromId(originalId);
        assertNotNull(updatedCustomer, "Updated customer should be in repository");
        assertEquals(originalId, updatedCustomer.getId(), "Customer ID should remain the same");
        assertEquals("John Updated", updatedCustomer.getName(), "Customer name should be updated");
        assertNotEquals(originalName, updatedCustomer.getName(), "Name should have changed");
    }

    @Test
    void testUpdateNonExistentCustomerAddsToRepository() {
        // Arrange
        // testCustomer1 is not in repository

        // Act
        repository.updateCustomer(testCustomer1);

        // Assert
        List<Customer> customers = repository.getAllCustomers();
        assertEquals(1, customers.size(), "Repository should contain the customer after update");
        assertTrue(customers.contains(testCustomer1));
    }

    @Test
    void testGetAllCustomersReturnsEmptyListWhenEmpty() {
        // Act
        List<Customer> customers = repository.getAllCustomers();

        // Assert
        assertNotNull(customers, "getAllCustomers should not return null");
        assertTrue(customers.isEmpty(), "Empty repository should return empty list");
    }

    @Test
    void testGetAllCustomersReturnsAllCustomers() {
        // Arrange
        repository.createCustomer(testCustomer1);
        repository.createCustomer(testCustomer2);
        Customer testCustomer3 = new LeadCustomer("Bob Jones", "bob@example.com", "555-3333");
        repository.createCustomer(testCustomer3);

        // Act
        List<Customer> customers = repository.getAllCustomers();

        // Assert
        assertEquals(3, customers.size(), "Should return all three customers");
        assertTrue(customers.contains(testCustomer1));
        assertTrue(customers.contains(testCustomer2));
        assertTrue(customers.contains(testCustomer3));
    }

    @Test
    void testMultipleDeletesAndAdds() {
        // Arrange & Act
        repository.createCustomer(testCustomer1);
        repository.createCustomer(testCustomer2);
        assertEquals(2, repository.getAllCustomers().size());

        repository.deleteCustomer(testCustomer1);
        assertEquals(1, repository.getAllCustomers().size());

        Customer testCustomer3 = new LeadCustomer("Bob Jones", "bob@example.com", "555-3333");
        repository.createCustomer(testCustomer3);
        assertEquals(2, repository.getAllCustomers().size());

        repository.deleteCustomer(testCustomer2);
        repository.deleteCustomer(testCustomer3);
        assertEquals(0, repository.getAllCustomers().size());
    }

    @Test
    void testRepositoryWorksWithDifferentCustomerTypes() {
        // Arrange
        Customer lead = new LeadCustomer("Lead Customer", "lead@example.com", "555-0001");
        Customer regular = new RegularCustomer("Regular Customer", "regular@example.com", "555-0002");
        Customer vip = new VIPCustomer("VIP Customer", "vip@example.com", "555-0003");

        // Act
        repository.createCustomer(lead);
        repository.createCustomer(regular);
        repository.createCustomer(vip);

        // Assert
        List<Customer> customers = repository.getAllCustomers();
        assertEquals(3, customers.size());
        assertTrue(customers.stream().anyMatch(c -> c instanceof LeadCustomer));
        assertTrue(customers.stream().anyMatch(c -> c instanceof RegularCustomer));
        assertTrue(customers.stream().anyMatch(c -> c instanceof VIPCustomer));
    }
}
