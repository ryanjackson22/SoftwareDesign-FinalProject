//package crm.controller;
//
//import customer.Customer;
//import customer.LeadCustomer;
//import customer.RegularCustomer;
//import customer.VIPCustomer;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.AfterEach;
//
//import java.io.ByteArrayOutputStream;
//import java.io.PrintStream;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class CRMControllerTest {
//
//    private CRMController controller;
//    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
//    private final PrintStream originalOut = System.out;
//
//    @BeforeEach
//    void setUp() {
//        controller = new CRMController();
//        // Redirect System.out to capture console output
//        System.setOut(new PrintStream(outContent));
//    }
//
//    @AfterEach
//    void tearDown() {
//        // Restore original System.out
//        System.setOut(originalOut);
//    }
//
//    @Test
//    void testPrintCustomersWithEmptyList() {
//        // Act
//        controller.printCustomers();
//
//        // Assert
//        String output = outContent.toString();
//        assertTrue(output.isEmpty() || output.trim().isEmpty(),
//                "printCustomers should produce no output when customer list is empty");
//    }
//
//    @Test
//    void testPrintCustomersWithSingleCustomer() {
//        // Arrange
//        Customer customer = new RegularCustomer("John Doe", "johndoe@test.net", "(800)555-1234");
//        controller.createContact(customer);
//
//        // Act
//        controller.printCustomers();
//
//        // Assert
//        String output = outContent.toString().trim();
//        assertTrue(output.contains("Name: John Doe"),
//                "Output should contain customer name");
//        assertTrue(output.contains("Email: johndoe@test.net"),
//                "Output should contain customer email");
//        assertTrue(output.contains("Phone: (800)555-1234"),
//                "Output should contain customer phone");
//        assertTrue(output.contains("ID:"),
//                "Output should contain customer ID");
//    }
//
//    @Test
//    void testPrintCustomersWithMultipleCustomers() {
//        // Arrange
//        Customer customer1 = new RegularCustomer("Alice Smith", "alice@test.com", "111-1111");
//        Customer customer2 = new VIPCustomer("Bob Johnson", "bob@vip.com", "222-2222");
//        Customer customer3 = new LeadCustomer("Charlie Brown", "charlie@lead.com", "333-3333");
//
//        controller.createContact(customer1);
//        controller.createContact(customer2);
//        controller.createContact(customer3);
//
//        // Act
//        controller.printCustomers();
//
//        // Assert
//        String output = outContent.toString();
//        String[] lines = output.split(System.lineSeparator());
//
//        // Should have 3 lines of output (one per customer)
//        assertEquals(3, lines.length,
//                "Should print exactly 3 lines for 3 customers");
//
//        // Verify all customers are printed
//        assertTrue(output.contains("Alice Smith"),
//                "Output should contain first customer");
//        assertTrue(output.contains("Bob Johnson"),
//                "Output should contain second customer");
//        assertTrue(output.contains("Charlie Brown"),
//                "Output should contain third customer");
//    }
//
//    @Test
//    void testPrintCustomersFormat() {
//        // Arrange
//        Customer customer = new RegularCustomer("Test User", "test@example.com", "555-0000");
//        controller.createContact(customer);
//
//        // Act
//        controller.printCustomers();
//
//        // Assert
//        String output = outContent.toString().trim();
//        // Verify the format matches: { Name: X, ID: Y, Email: Z, Phone: W }
//        assertTrue(output.matches(".*\\{ Name: .+, ID: \\d+, Email: .+, Phone: .+ }.*"),
//                "Output should match the expected format");
//    }
//
//    @Test
//    void testPrintCustomersAfterDeletion() {
//        // Arrange
//        Customer customer1 = new RegularCustomer("Keep This", "keep@test.com", "111-1111");
//        Customer customer2 = new RegularCustomer("Delete This", "delete@test.com", "222-2222");
//
//        controller.createContact(customer1);
//        controller.createContact(customer2);
//
//        // Act - delete one customer and print
//        controller.deleteContact(customer2);
//        controller.printCustomers();
//
//        // Assert
//        String output = outContent.toString();
//        assertTrue(output.contains("Keep This"),
//                "Should still contain first customer");
//        assertFalse(output.contains("Delete This"),
//                "Should not contain deleted customer");
//    }
//
//    @Test
//    void testCreateContactAddsCustomerToList() {
//        // Arrange
//        Customer customer = new RegularCustomer("New Customer", "new@test.com", "555-1234");
//
//        // Act
//        controller.createContact(customer);
//        controller.printCustomers();
//
//        // Assert
//        String output = outContent.toString();
//        assertTrue(output.contains("New Customer"),
//                "Created customer should appear in printCustomers output");
//    }
//}
