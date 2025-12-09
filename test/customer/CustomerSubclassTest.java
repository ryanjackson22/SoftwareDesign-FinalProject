package customer;

import notification.EmailNotification;
import notification.SMSNotification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class CustomerSubclassTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    // ========== LeadCustomer Tests ==========

    @Test
    void testLeadCustomerCreation() {
        // Act
        LeadCustomer lead = new LeadCustomer("John Lead", "lead@example.com", "555-1111");

        // Assert
        assertNotNull(lead);
        assertEquals("John Lead", lead.getName());
        assertEquals("lead@example.com", lead.getEmail());
        assertEquals("555-1111", lead.getPhone());
    }

    @Test
    void testLeadCustomerIsInstanceOfCustomer() {
        // Arrange & Act
        LeadCustomer lead = new LeadCustomer("Test Lead", "test@example.com", "555-0000");

        // Assert
        assertTrue(lead instanceof Customer);
    }

    @Test
    void testLeadCustomerToString() {
        // Arrange
        LeadCustomer lead = new LeadCustomer("Jane Lead", "jane.lead@example.com", "555-2222");

        // Act
        String result = lead.toString();

        // Assert
        assertTrue(result.contains("Name: Jane Lead"));
        assertTrue(result.contains("ID: " + lead.getId()));
        assertTrue(result.contains("Email: jane.lead@example.com"));
        assertTrue(result.contains("Phone: 555-2222"));
    }

    @Test
    void testLeadCustomerInheritsContactMethod() {
        // Arrange
        LeadCustomer lead = new LeadCustomer("Contact Test", "contact@example.com", "555-3333");

        // Act
        lead.contact("Test message");

        // Assert
        String output = outContent.toString();
        assertTrue(output.contains("Test message"));
    }

    @Test
    void testLeadCustomerCanAddInteractions() {
        // Arrange
        LeadCustomer lead = new LeadCustomer("Interaction Test", "interaction@example.com", "555-4444");

        // Act
        lead.addInteraction(crm.observer.event.EventType.CUSTOMER_CREATED, "Lead created");

        // Assert
        assertEquals(1, lead.getInteractionHistory().size());
    }

    // ========== VIPCustomer Tests ==========

    @Test
    void testVIPCustomerCreation() {
        // Act
        VIPCustomer vip = new VIPCustomer("Alice VIP", "vip@example.com", "555-5555");

        // Assert
        assertNotNull(vip);
        assertEquals("Alice VIP", vip.getName());
        assertEquals("vip@example.com", vip.getEmail());
        assertEquals("555-5555", vip.getPhone());
    }

    @Test
    void testVIPCustomerIsInstanceOfCustomer() {
        // Arrange & Act
        VIPCustomer vip = new VIPCustomer("Test VIP", "test@example.com", "555-0000");

        // Assert
        assertTrue(vip instanceof Customer);
    }

    @Test
    void testVIPCustomerToString() {
        // Arrange
        VIPCustomer vip = new VIPCustomer("Bob VIP", "bob.vip@example.com", "555-6666");

        // Act
        String result = vip.toString();

        // Assert
        assertTrue(result.contains("Name: Bob VIP"));
        assertTrue(result.contains("ID: " + vip.getId()));
        assertTrue(result.contains("Email: bob.vip@example.com"));
        assertTrue(result.contains("Phone: 555-6666"));
    }

    @Test
    void testVIPCustomerInheritsContactMethod() {
        // Arrange
        VIPCustomer vip = new VIPCustomer("VIP Contact", "vipcontact@example.com", "555-7777");

        // Act
        vip.contact("VIP message");

        // Assert
        String output = outContent.toString();
        assertTrue(output.contains("VIP message"));
    }

    @Test
    void testVIPCustomerCanAddInteractions() {
        // Arrange
        VIPCustomer vip = new VIPCustomer("VIP Interaction", "vipinteraction@example.com", "555-8888");

        // Act
        vip.addInteraction(crm.observer.event.EventType.SALE_MADE, "VIP purchase");

        // Assert
        assertEquals(1, vip.getInteractionHistory().size());
    }

    // ========== LostCustomer Tests ==========

    @Test
    void testLostCustomerCreation() {
        // Act
        LostCustomer lost = new LostCustomer("Charlie Lost", "lost@example.com", "555-9999");

        // Assert
        assertNotNull(lost);
        assertEquals("Charlie Lost", lost.getName());
        assertEquals("lost@example.com", lost.getEmail());
        assertEquals("555-9999", lost.getPhone());
    }

    @Test
    void testLostCustomerIsInstanceOfCustomer() {
        // Arrange & Act
        LostCustomer lost = new LostCustomer("Test Lost", "test@example.com", "555-0000");

        // Assert
        assertTrue(lost instanceof Customer);
    }

    @Test
    void testLostCustomerToString() {
        // Arrange
        LostCustomer lost = new LostCustomer("Diana Lost", "diana.lost@example.com", "555-1234");

        // Act
        String result = lost.toString();

        // Assert
        assertTrue(result.contains("Name: Diana Lost"));
        assertTrue(result.contains("ID: " + lost.getId()));
        assertTrue(result.contains("Email: diana.lost@example.com"));
        assertTrue(result.contains("Phone: 555-1234"));
    }

    @Test
    void testLostCustomerInheritsContactMethod() {
        // Arrange
        LostCustomer lost = new LostCustomer("Lost Contact", "lostcontact@example.com", "555-5678");

        // Act
        lost.contact("Re-engagement message");

        // Assert
        String output = outContent.toString();
        assertTrue(output.contains("Re-engagement message"));
    }

    @Test
    void testLostCustomerCanAddInteractions() {
        // Arrange
        LostCustomer lost = new LostCustomer("Lost Interaction", "lostinteraction@example.com", "555-9012");

        // Act
        lost.addInteraction(crm.observer.event.EventType.CUSTOMER_UPDATED, "Marked as lost");

        // Assert
        assertEquals(1, lost.getInteractionHistory().size());
    }

    // ========== RegularCustomer Tests ==========

    @Test
    void testRegularCustomerCreation() {
        // Act
        RegularCustomer regular = new RegularCustomer("Eve Regular", "regular@example.com", "555-3456");

        // Assert
        assertNotNull(regular);
        assertEquals("Eve Regular", regular.getName());
        assertEquals("regular@example.com", regular.getEmail());
        assertEquals("555-3456", regular.getPhone());
    }

    @Test
    void testRegularCustomerIsInstanceOfCustomer() {
        // Arrange & Act
        RegularCustomer regular = new RegularCustomer("Test Regular", "test@example.com", "555-0000");

        // Assert
        assertTrue(regular instanceof Customer);
    }

    @Test
    void testRegularCustomerToString() {
        // Arrange
        RegularCustomer regular = new RegularCustomer("Frank Regular", "frank@example.com", "555-7890");

        // Act
        String result = regular.toString();

        // Assert
        assertTrue(result.contains("Name: Frank Regular"));
        assertTrue(result.contains("ID: " + regular.getId()));
        assertTrue(result.contains("Email: frank@example.com"));
        assertTrue(result.contains("Phone: 555-7890"));
    }

    // ========== Comparison Tests Between Subclasses ==========

    @Test
    void testAllSubclassesHaveUniqueIds() {
        // Arrange & Act
        LeadCustomer lead = new LeadCustomer("Lead", "lead@example.com", "555-0001");
        VIPCustomer vip = new VIPCustomer("VIP", "vip@example.com", "555-0002");
        LostCustomer lost = new LostCustomer("Lost", "lost@example.com", "555-0003");
        RegularCustomer regular = new RegularCustomer("Regular", "regular@example.com", "555-0004");

        // Assert
        assertNotEquals(lead.getId(), vip.getId());
        assertNotEquals(lead.getId(), lost.getId());
        assertNotEquals(lead.getId(), regular.getId());
        assertNotEquals(vip.getId(), lost.getId());
        assertNotEquals(vip.getId(), regular.getId());
        assertNotEquals(lost.getId(), regular.getId());
    }

    @Test
    void testAllSubclassesCanSetName() {
        // Arrange
        LeadCustomer lead = new LeadCustomer("Original", "lead@example.com", "555-0001");
        VIPCustomer vip = new VIPCustomer("Original", "vip@example.com", "555-0002");
        LostCustomer lost = new LostCustomer("Original", "lost@example.com", "555-0003");
        RegularCustomer regular = new RegularCustomer("Original", "regular@example.com", "555-0004");

        // Act
        lead.setName("Lead Updated");
        vip.setName("VIP Updated");
        lost.setName("Lost Updated");
        regular.setName("Regular Updated");

        // Assert
        assertEquals("Lead Updated", lead.getName());
        assertEquals("VIP Updated", vip.getName());
        assertEquals("Lost Updated", lost.getName());
        assertEquals("Regular Updated", regular.getName());
    }

    @Test
    void testAllSubclassesCanSetEmail() {
        // Arrange
        LeadCustomer lead = new LeadCustomer("Lead", "old@example.com", "555-0001");
        VIPCustomer vip = new VIPCustomer("VIP", "old@example.com", "555-0002");
        LostCustomer lost = new LostCustomer("Lost", "old@example.com", "555-0003");
        RegularCustomer regular = new RegularCustomer("Regular", "old@example.com", "555-0004");

        // Act
        lead.setEmail("leadnew@example.com");
        vip.setEmail("vipnew@example.com");
        lost.setEmail("lostnew@example.com");
        regular.setEmail("regularnew@example.com");

        // Assert
        assertEquals("leadnew@example.com", lead.getEmail());
        assertEquals("vipnew@example.com", vip.getEmail());
        assertEquals("lostnew@example.com", lost.getEmail());
        assertEquals("regularnew@example.com", regular.getEmail());
    }

    @Test
    void testAllSubclassesCanSetPhone() {
        // Arrange
        LeadCustomer lead = new LeadCustomer("Lead", "lead@example.com", "000-0000");
        VIPCustomer vip = new VIPCustomer("VIP", "vip@example.com", "000-0000");
        LostCustomer lost = new LostCustomer("Lost", "lost@example.com", "000-0000");
        RegularCustomer regular = new RegularCustomer("Regular", "regular@example.com", "000-0000");

        // Act
        lead.setPhone("111-1111");
        vip.setPhone("222-2222");
        lost.setPhone("333-3333");
        regular.setPhone("444-4444");

        // Assert
        assertEquals("111-1111", lead.getPhone());
        assertEquals("222-2222", vip.getPhone());
        assertEquals("333-3333", lost.getPhone());
        assertEquals("444-4444", regular.getPhone());
    }

    @Test
    void testAllSubclassesToStringFormatIsConsistent() {
        // Arrange
        LeadCustomer lead = new LeadCustomer("Lead Name", "lead@example.com", "555-1111");
        VIPCustomer vip = new VIPCustomer("VIP Name", "vip@example.com", "555-2222");
        LostCustomer lost = new LostCustomer("Lost Name", "lost@example.com", "555-3333");
        RegularCustomer regular = new RegularCustomer("Regular Name", "regular@example.com", "555-4444");

        // Act
        String leadStr = lead.toString();
        String vipStr = vip.toString();
        String lostStr = lost.toString();
        String regularStr = regular.toString();

        // Assert - All should follow the same format
        assertTrue(leadStr.matches(".*Name: .*ID: .*Email: .*Phone: .*"));
        assertTrue(vipStr.matches(".*Name: .*ID: .*Email: .*Phone: .*"));
        assertTrue(lostStr.matches(".*Name: .*ID: .*Email: .*Phone: .*"));
        assertTrue(regularStr.matches(".*Name: .*ID: .*Email: .*Phone: .*"));
    }

    @Test
    void testAllSubclassesInheritInteractionHistory() {
        // Arrange
        LeadCustomer lead = new LeadCustomer("Lead", "lead@example.com", "555-0001");
        VIPCustomer vip = new VIPCustomer("VIP", "vip@example.com", "555-0002");
        LostCustomer lost = new LostCustomer("Lost", "lost@example.com", "555-0003");
        RegularCustomer regular = new RegularCustomer("Regular", "regular@example.com", "555-0004");

        // Act
        lead.addInteraction(crm.observer.event.EventType.CUSTOMER_CREATED, "Lead created");
        vip.addInteraction(crm.observer.event.EventType.CUSTOMER_CREATED, "VIP created");
        lost.addInteraction(crm.observer.event.EventType.CUSTOMER_CREATED, "Lost created");
        regular.addInteraction(crm.observer.event.EventType.CUSTOMER_CREATED, "Regular created");

        // Assert
        assertFalse(lead.getInteractionHistory().isEmpty());
        assertFalse(vip.getInteractionHistory().isEmpty());
        assertFalse(lost.getInteractionHistory().isEmpty());
        assertFalse(regular.getInteractionHistory().isEmpty());
    }

    @Test
    void testDifferentCustomerTypesCanBeStoredTogether() {
        // Arrange & Act
        Customer[] customers = new Customer[] {
                new LeadCustomer("Lead", "lead@example.com", "555-0001"),
                new VIPCustomer("VIP", "vip@example.com", "555-0002"),
                new LostCustomer("Lost", "lost@example.com", "555-0003"),
                new RegularCustomer("Regular", "regular@example.com", "555-0004")
        };

        // Assert
        assertEquals(4, customers.length);
        assertTrue(customers[0] instanceof LeadCustomer);
        assertTrue(customers[1] instanceof VIPCustomer);
        assertTrue(customers[2] instanceof LostCustomer);
        assertTrue(customers[3] instanceof RegularCustomer);
    }
}
