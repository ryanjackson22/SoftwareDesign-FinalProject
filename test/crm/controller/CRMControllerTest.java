package crm.controller;

import crm.controller.command.CRMCommand;
import crm.observer.CRMObserver;
import crm.observer.event.CRMEvent;
import crm.observer.event.EventType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CRMControllerTest {

    private CRMController controller;

    // Mock command for testing
    private static class MockCommand implements CRMCommand {
        private final String name;
        private final EventType eventType;
        private boolean executed = false;
        private boolean undone = false;
        private int executeCount = 0;
        private int undoCount = 0;

        public MockCommand(String name, EventType eventType) {
            this.name = name;
            this.eventType = eventType;
        }

        @Override
        public void execute() {
            executed = true;
            executeCount++;
        }

        @Override
        public void undo() {
            undone = true;
            undoCount++;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public EventType getEventType() {
            return eventType;
        }

        public boolean wasExecuted() {
            return executed;
        }

        public boolean wasUndone() {
            return undone;
        }

        public int getExecuteCount() {
            return executeCount;
        }

        public int getUndoCount() {
            return undoCount;
        }

    }

    // Mock observer for testing
    private static class MockObserver implements CRMObserver {
        private final List<CRMEvent> receivedEvents = new ArrayList<>();

        @Override
        public void onEvent(EventType eventType) {
//            receivedEvents.add(eventType);
        }

        @Override
        public void onEvent(CRMEvent crmEvent) {
            receivedEvents.add(crmEvent);
        }

        public List<CRMEvent> getReceivedEvents() {
            return new ArrayList<>(receivedEvents);
        }

        public int getEventCount() {
            return receivedEvents.size();
        }

    }

    @BeforeEach
    void setUp() {
        controller = new CRMController();
    }

    // ==================== Command Management Tests ====================

    @Test
    void testAddCommand() {
        // Arrange
        MockCommand command = new MockCommand("Test Command", EventType.CUSTOMER_CREATED);

        // Act
        controller.addCommand(command);
        List<String> listing = controller.getCommandListing();

        // Assert
        assertEquals(1, listing.size(), "Should have one command");
        assertEquals("Test Command", listing.get(0), "Command name should match");
    }

    @Test
    void testAddMultipleCommands() {
        // Arrange
        MockCommand command1 = new MockCommand("Command 1", EventType.CUSTOMER_CREATED);
        MockCommand command2 = new MockCommand("Command 2", EventType.CUSTOMER_UPDATED);
        MockCommand command3 = new MockCommand("Command 3", EventType.CUSTOMER_DELETED);

        // Act
        controller.addCommand(command1);
        controller.addCommand(command2);
        controller.addCommand(command3);
        List<String> listing = controller.getCommandListing();

        // Assert
        assertEquals(3, listing.size(), "Should have three commands");
        assertEquals("Command 1", listing.get(0));
        assertEquals("Command 2", listing.get(1));
        assertEquals("Command 3", listing.get(2));
    }

    @Test
    void testGetCommandListingEmptyController() {
        // Act
        List<String> listing = controller.getCommandListing();

        // Assert
        assertNotNull(listing, "Listing should not be null");
        assertEquals(0, listing.size(), "Empty controller should return empty list");
    }

    // ==================== Command Execution Tests ====================

    @Test
    void testExecuteCommand() {
        // Arrange
        MockCommand command = new MockCommand("Test Command", EventType.CUSTOMER_CREATED);
        controller.addCommand(command);

        // Act
        controller.executeCommand(0);

        // Assert
        assertTrue(command.wasExecuted(), "Command should be executed");
        assertEquals(1, command.getExecuteCount(), "Command should be executed once");
    }

    @Test
    void testExecuteMultipleCommands() {
        // Arrange
        MockCommand command1 = new MockCommand("Command 1", EventType.CUSTOMER_CREATED);
        MockCommand command2 = new MockCommand("Command 2", EventType.CUSTOMER_UPDATED);
        controller.addCommand(command1);
        controller.addCommand(command2);

        // Act
        controller.executeCommand(0);
        controller.executeCommand(1);

        // Assert
        assertTrue(command1.wasExecuted(), "First command should be executed");
        assertTrue(command2.wasExecuted(), "Second command should be executed");
    }

    @Test
    void testExecuteCommandBySlot() {
        // Arrange
        MockCommand command1 = new MockCommand("Command 1", EventType.CUSTOMER_CREATED);
        MockCommand command2 = new MockCommand("Command 2", EventType.CUSTOMER_UPDATED);
        MockCommand command3 = new MockCommand("Command 3", EventType.CUSTOMER_DELETED);
        controller.addCommand(command1);
        controller.addCommand(command2);
        controller.addCommand(command3);

        // Act
        controller.executeCommand(1); // Execute middle command

        // Assert
        assertFalse(command1.wasExecuted(), "First command should not be executed");
        assertTrue(command2.wasExecuted(), "Second command should be executed");
        assertFalse(command3.wasExecuted(), "Third command should not be executed");
    }

    // ==================== Undo Tests ====================

    @Test
    void testUndoCommand() {
        // Arrange
        MockCommand command = new MockCommand("Test Command", EventType.CUSTOMER_CREATED);
        controller.addCommand(command);
        controller.executeCommand(0);

        // Act
        controller.undoCommand();

        // Assert
        assertTrue(command.wasUndone(), "Command should be undone");
        assertEquals(1, command.getUndoCount(), "Command should be undone once");
    }

    @Test
    void testUndoMultipleCommands() {
        // Arrange
        MockCommand command1 = new MockCommand("Command 1", EventType.CUSTOMER_CREATED);
        MockCommand command2 = new MockCommand("Command 2", EventType.CUSTOMER_UPDATED);
        controller.addCommand(command1);
        controller.addCommand(command2);
        controller.executeCommand(0);
        controller.executeCommand(1);

        // Act
        controller.undoCommand(); // Should undo command2
        controller.undoCommand(); // Should undo command1

        // Assert
        assertTrue(command1.wasUndone(), "First command should be undone");
        assertTrue(command2.wasUndone(), "Second command should be undone");
    }

    @Test
    void testUndoInReverseOrder() {
        // Arrange
        MockCommand command1 = new MockCommand("Command 1", EventType.CUSTOMER_CREATED);
        MockCommand command2 = new MockCommand("Command 2", EventType.CUSTOMER_UPDATED);
        controller.addCommand(command1);
        controller.addCommand(command2);
        controller.executeCommand(0);
        controller.executeCommand(1);

        // Act & Assert
        controller.undoCommand(); // Should undo command2 (last executed)
        assertEquals(0, command1.getUndoCount(), "First command should not be undone yet");
        assertEquals(1, command2.getUndoCount(), "Second command should be undone first");

        controller.undoCommand(); // Should undo command1
        assertEquals(1, command1.getUndoCount(), "First command should now be undone");
    }

    @Test
    void testUndoEmptyHistory() {
        // Act & Assert (should not throw exception)
        assertDoesNotThrow(() -> controller.undoCommand(),
                "Undo on empty history should not throw exception");
    }

    @Test
    void testUndoWithoutExecution() {
        // Arrange
        MockCommand command = new MockCommand("Test Command", EventType.CUSTOMER_CREATED);
        controller.addCommand(command);

        // Act
        controller.undoCommand();

        // Assert
        assertFalse(command.wasUndone(), "Command should not be undone if never executed");
    }

    @Test
    void testMultipleExecutionsOfSameCommand() {
        // Arrange
        MockCommand command = new MockCommand("Test Command", EventType.CUSTOMER_CREATED);
        controller.addCommand(command);

        // Act
        controller.executeCommand(0);
        controller.executeCommand(0);
        controller.executeCommand(0);

        // Assert
        assertEquals(3, command.getExecuteCount(), "Command should be executed three times");
    }

    // ==================== Observer Tests ====================

    @Test
    void testAddObserver() {
        // Arrange
        MockObserver observer = new MockObserver();

        // Act
        controller.addObserver(observer);
        MockCommand command = new MockCommand("Test Command", EventType.CUSTOMER_CREATED);
        controller.addCommand(command);
        controller.executeCommand(0);

        // Assert
        assertEquals(1, observer.getEventCount(), "Observer should receive one event");
        assertEquals(EventType.CUSTOMER_CREATED, observer.getReceivedEvents().get(0).getEventType());
    }

    @Test
    void testAddMultipleObservers() {
        // Arrange
        MockObserver observer1 = new MockObserver();
        MockObserver observer2 = new MockObserver();
        MockObserver observer3 = new MockObserver();

        // Act
        controller.addObserver(observer1);
        controller.addObserver(observer2);
        controller.addObserver(observer3);
        MockCommand command = new MockCommand("Test Command", EventType.SALE_MADE);
        controller.addCommand(command);
        controller.executeCommand(0);

        // Assert
        assertEquals(1, observer1.getEventCount(), "First observer should receive event");
        assertEquals(1, observer2.getEventCount(), "Second observer should receive event");
        assertEquals(1, observer3.getEventCount(), "Third observer should receive event");
        assertEquals(EventType.SALE_MADE, observer1.getReceivedEvents().get(0).getEventType());
        assertEquals(EventType.SALE_MADE, observer2.getReceivedEvents().get(0).getEventType());
        assertEquals(EventType.SALE_MADE, observer3.getReceivedEvents().get(0).getEventType());
    }

    @Test
    void testRemoveObserver() {
        // Arrange
        MockObserver observer1 = new MockObserver();
        MockObserver observer2 = new MockObserver();
        controller.addObserver(observer1);
        controller.addObserver(observer2);

        // Act
        controller.removeObserver(observer1);
        MockCommand command = new MockCommand("Test Command", EventType.CUSTOMER_DELETED);
        controller.addCommand(command);
        controller.executeCommand(0);

        // Assert
        assertEquals(0, observer1.getEventCount(), "Removed observer should not receive event");
        assertEquals(1, observer2.getEventCount(), "Remaining observer should receive event");
    }

    @Test
    void testObserverReceivesMultipleEvents() {
        // Arrange
        MockObserver observer = new MockObserver();
        controller.addObserver(observer);
        MockCommand command1 = new MockCommand("Command 1", EventType.CUSTOMER_CREATED);
        MockCommand command2 = new MockCommand("Command 2", EventType.CUSTOMER_UPDATED);
        MockCommand command3 = new MockCommand("Command 3", EventType.SALE_MADE);
        controller.addCommand(command1);
        controller.addCommand(command2);
        controller.addCommand(command3);

        // Act
        controller.executeCommand(0);
        controller.executeCommand(1);
        controller.executeCommand(2);

        // Assert
        assertEquals(3, observer.getEventCount(), "Observer should receive three events");
        List<CRMEvent> events = observer.getReceivedEvents();
        assertEquals(EventType.CUSTOMER_CREATED, events.get(0).getEventType());
        assertEquals(EventType.CUSTOMER_UPDATED, events.get(1).getEventType());
        assertEquals(EventType.SALE_MADE, events.get(2).getEventType());
    }

    @Test
    void testObserverReceivesUndoEvent() {
        // Arrange
        MockObserver observer = new MockObserver();
        controller.addObserver(observer);
        MockCommand command = new MockCommand("Test Command", EventType.CUSTOMER_CREATED);
        controller.addCommand(command);

        // Act
        controller.executeCommand(0);
        controller.undoCommand();

        // Assert
        assertEquals(2, observer.getEventCount(), "Observer should receive two events");
        List<CRMEvent> events = observer.getReceivedEvents();
        assertEquals(EventType.CUSTOMER_CREATED, events.get(0).getEventType(), "First event should be from execution");
        assertEquals(EventType.COMMAND_UNDONE, events.get(1).getEventType(), "Second event should be undo");
    }

    @Test
    void testNotifyObserversWithNoObservers() {
        // Arrange
        MockCommand command = new MockCommand("Test Command", EventType.CUSTOMER_CREATED);
        controller.addCommand(command);

        // Act & Assert (should not throw exception)
        assertDoesNotThrow(() -> controller.executeCommand(0),
                "Executing command with no observers should not throw exception");
    }

    // ==================== Integration Tests ====================

    @Test
    void testCompleteWorkflow() {
        // Arrange
        MockObserver observer = new MockObserver();
        controller.addObserver(observer);
        MockCommand createCommand = new MockCommand("Create", EventType.CUSTOMER_CREATED);
        MockCommand updateCommand = new MockCommand("Update", EventType.CUSTOMER_UPDATED);
        MockCommand deleteCommand = new MockCommand("Delete", EventType.CUSTOMER_DELETED);
        controller.addCommand(createCommand);
        controller.addCommand(updateCommand);
        controller.addCommand(deleteCommand);

        // Act
        controller.executeCommand(0); // Create
        controller.executeCommand(1); // Update
        controller.executeCommand(2); // Delete
        controller.undoCommand();     // Undo delete
        controller.undoCommand();     // Undo update

        // Assert
        assertTrue(createCommand.wasExecuted());
        assertTrue(updateCommand.wasExecuted());
        assertTrue(deleteCommand.wasExecuted());
        assertFalse(createCommand.wasUndone());
        assertTrue(updateCommand.wasUndone());
        assertTrue(deleteCommand.wasUndone());

        // Verify observer received all events
        assertEquals(5, observer.getEventCount());
        List<CRMEvent> events = observer.getReceivedEvents();
        assertEquals(EventType.CUSTOMER_CREATED, events.get(0).getEventType());
        assertEquals(EventType.CUSTOMER_UPDATED, events.get(1).getEventType());
        assertEquals(EventType.CUSTOMER_DELETED, events.get(2).getEventType());
        assertEquals(EventType.COMMAND_UNDONE, events.get(3).getEventType());
        assertEquals(EventType.COMMAND_UNDONE, events.get(4).getEventType());
    }

    @Test
    void testCommandListingDoesNotChange() {
        // Arrange
        MockCommand command1 = new MockCommand("Command 1", EventType.CUSTOMER_CREATED);
        MockCommand command2 = new MockCommand("Command 2", EventType.CUSTOMER_UPDATED);
        controller.addCommand(command1);
        controller.addCommand(command2);

        // Act
        List<String> listingBefore = controller.getCommandListing();
        controller.executeCommand(0);
        controller.undoCommand();
        List<String> listingAfter = controller.getCommandListing();

        // Assert
        assertEquals(listingBefore.size(), listingAfter.size(),
                "Command listing should not change after execution/undo");
        assertEquals(listingBefore.get(0), listingAfter.get(0));
        assertEquals(listingBefore.get(1), listingAfter.get(1));
    }
}
