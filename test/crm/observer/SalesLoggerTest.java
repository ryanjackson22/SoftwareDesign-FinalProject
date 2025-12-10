package crm.observer;

import crm.observer.event.CRMEvent;
import crm.observer.event.EventType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SalesLoggerTest {

    @TempDir
    Path tempDir;

    private SalesLogger salesLogger;
    private Path logFilePath;
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalErr = System.out;

    @BeforeEach
    void setUp() throws IOException {
        logFilePath = tempDir.resolve("sales-log.txt");
        salesLogger = new SalesLogger(logFilePath.toString());
        System.setOut(new PrintStream(errContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalErr);
    }

    @Test
    void testOnEventWithSaleMadeEventTypeWritesToFile() throws IOException {
        // Act
        salesLogger.onEvent(EventType.SALE_MADE);

        // Assert
        assertTrue(Files.exists(logFilePath), "Log file should be created");

        List<String> lines = Files.readAllLines(logFilePath);
        assertEquals(1, lines.size(), "Should have one line");
        assertEquals("Sale Made", lines.get(0));
    }

    @Test
    void testOnEventWithNonSaleEventTypeDoesNotWriteToFile() throws IOException {
        // Act
        salesLogger.onEvent(EventType.CUSTOMER_CREATED);

        // Assert
        // File might be created but should be empty or not exist
        if (Files.exists(logFilePath)) {
            List<String> lines = Files.readAllLines(logFilePath);
            assertEquals(0, lines.size(), "Non-sale events should not be logged");
        }
    }

    @Test
    void testOnEventFiltersOutCustomerCreated() throws IOException {
        // Act
        salesLogger.onEvent(EventType.CUSTOMER_CREATED);
        salesLogger.onEvent(EventType.SALE_MADE);

        // Assert
        List<String> lines = Files.readAllLines(logFilePath);
        assertEquals(1, lines.size(), "Only SALE_MADE should be logged");
        assertEquals("Sale Made", lines.get(0));
    }

    @Test
    void testOnEventFiltersOutCustomerUpdated() throws IOException {
        // Act
        salesLogger.onEvent(EventType.CUSTOMER_UPDATED);
        salesLogger.onEvent(EventType.SALE_MADE);

        // Assert
        List<String> lines = Files.readAllLines(logFilePath);
        assertEquals(1, lines.size(), "Only SALE_MADE should be logged");
        assertEquals("Sale Made", lines.get(0));
    }

    @Test
    void testOnEventFiltersOutCustomerDeleted() throws IOException {
        // Act
        salesLogger.onEvent(EventType.CUSTOMER_DELETED);
        salesLogger.onEvent(EventType.SALE_MADE);

        // Assert
        List<String> lines = Files.readAllLines(logFilePath);
        assertEquals(1, lines.size(), "Only SALE_MADE should be logged");
        assertEquals("Sale Made", lines.get(0));
    }

    @Test
    void testOnEventFiltersOutNotificationSent() throws IOException {
        // Act
        salesLogger.onEvent(EventType.NOTIFICATION_SENT);
        salesLogger.onEvent(EventType.SALE_MADE);

        // Assert
        List<String> lines = Files.readAllLines(logFilePath);
        assertEquals(1, lines.size(), "Only SALE_MADE should be logged");
        assertEquals("Sale Made", lines.get(0));
    }

    @Test
    void testOnEventFiltersOutViewHistory() throws IOException {
        // Act
        salesLogger.onEvent(EventType.VIEW_HISTORY);
        salesLogger.onEvent(EventType.SALE_MADE);

        // Assert
        List<String> lines = Files.readAllLines(logFilePath);
        assertEquals(1, lines.size(), "Only SALE_MADE should be logged");
        assertEquals("Sale Made", lines.get(0));
    }

    @Test
    void testOnEventWithCRMEventAlwaysWritesToFile() throws IOException {
        // Note: onEvent(CRMEvent) doesn't filter - it logs everything
        // Arrange
        CRMEvent event = new CRMEvent(EventType.CUSTOMER_CREATED, 123);

        // Act
        salesLogger.onEvent(event);

        // Assert
        assertTrue(Files.exists(logFilePath));
        List<String> lines = Files.readAllLines(logFilePath);
        assertEquals(1, lines.size());
        assertTrue(lines.get(0).contains("Customer Created"));
    }

    @Test
    void testOnEventWithCRMEventSaleWritesToFile() throws IOException {
        // Arrange
        CRMEvent event = new CRMEvent(EventType.SALE_MADE, 456, "Sale of $100");

        // Act
        salesLogger.onEvent(event);

        // Assert
        List<String> lines = Files.readAllLines(logFilePath);
        assertEquals(1, lines.size());
        assertTrue(lines.get(0).contains("Sale Made"));
        assertTrue(lines.get(0).contains("456"));
        assertTrue(lines.get(0).contains("Sale of $100"));
    }

    @Test
    void testMultipleSaleEventsAreLogged() throws IOException {
        // Act
        salesLogger.onEvent(EventType.SALE_MADE);
        salesLogger.onEvent(EventType.SALE_MADE);
        salesLogger.onEvent(EventType.SALE_MADE);

        // Assert
        List<String> lines = Files.readAllLines(logFilePath);
        assertEquals(3, lines.size(), "All sale events should be logged");
        for (String line : lines) {
            assertEquals("Sale Made", line);
        }
    }

    @Test
    void testMixedEventsOnlyLogsSales() throws IOException {
        // Act
        salesLogger.onEvent(EventType.CUSTOMER_CREATED);
        salesLogger.onEvent(EventType.SALE_MADE);
        salesLogger.onEvent(EventType.CUSTOMER_UPDATED);
        salesLogger.onEvent(EventType.SALE_MADE);
        salesLogger.onEvent(EventType.NOTIFICATION_SENT);
        salesLogger.onEvent(EventType.SALE_MADE);

        // Assert
        List<String> lines = Files.readAllLines(logFilePath);
        assertEquals(3, lines.size(), "Only 3 SALE_MADE events should be logged");
        for (String line : lines) {
            assertEquals("Sale Made", line);
        }
    }

    @Test
    void testSalesLoggerAppendsToFile() throws IOException {
        // Arrange - Write initial content
        Files.writeString(logFilePath, "Initial sale log\n");

        // Act
        salesLogger.onEvent(EventType.SALE_MADE);

        // Assert
        List<String> lines = Files.readAllLines(logFilePath);
        assertEquals(2, lines.size());
        assertEquals("Initial sale log", lines.get(0));
        assertEquals("Sale Made", lines.get(1));
    }

    @Test
    void testWriteToFileDirectly() throws IOException {
        // Act
        salesLogger.writeToFile("Custom sale log");

        // Assert
        List<String> lines = Files.readAllLines(logFilePath);
        assertEquals(1, lines.size());
        assertEquals("Custom sale log", lines.get(0));
    }

    @Test
    void testInvalidFilePathHandlesException() {
        // Arrange
        SalesLogger invalidLogger = new SalesLogger("/invalid/path/that/does/not/exist/sales.txt");

        // Act
        invalidLogger.onEvent(EventType.SALE_MADE);

        // Assert - Should print error but not throw
        String output = errContent.toString();
        assertTrue(output.contains("Unable to write to log file"));
    }

    @Test
    void testSalesLoggerImplementsCRMObserver() {
        // Assert
        assertTrue(salesLogger instanceof CRMObserver, "SalesLogger should implement CRMObserver");
    }

    @Test
    void testFilteringLogicWithAllEventTypes() throws IOException {
        // Arrange
        EventType[] allTypes = {
                EventType.CUSTOMER_CREATED,
                EventType.CUSTOMER_UPDATED,
                EventType.CUSTOMER_DELETED,
                EventType.NOTIFICATION_SENT,
                EventType.SALE_MADE,
                EventType.VIEW_HISTORY
        };

        // Act
        for (EventType type : allTypes) {
            salesLogger.onEvent(type);
        }

        // Assert
        List<String> lines = Files.readAllLines(logFilePath);
        assertEquals(1, lines.size(), "Only SALE_MADE should be logged out of all event types");
        assertEquals("Sale Made", lines.get(0));
    }

    @Test
    void testCRMEventBypassesFiltering() throws IOException {
        // The onEvent(CRMEvent) method doesn't check event type - it logs everything
        // Act
        salesLogger.onEvent(new CRMEvent(EventType.CUSTOMER_CREATED));
        salesLogger.onEvent(new CRMEvent(EventType.SALE_MADE));
        salesLogger.onEvent(new CRMEvent(EventType.CUSTOMER_UPDATED));

        // Assert
        List<String> lines = Files.readAllLines(logFilePath);
        assertEquals(3, lines.size(), "All CRMEvents should be logged regardless of type");
    }

    @Test
    void testEmptyFileWhenNoSaleEvents() throws IOException {
        // Act
        salesLogger.onEvent(EventType.CUSTOMER_CREATED);
        salesLogger.onEvent(EventType.CUSTOMER_UPDATED);
        salesLogger.onEvent(EventType.CUSTOMER_DELETED);

        // Assert
        if (Files.exists(logFilePath)) {
            List<String> lines = Files.readAllLines(logFilePath);
            assertEquals(0, lines.size(), "File should be empty when no sale events occur");
        }
    }

    @Test
    void testSaleEventWithDetailsViaCRMEvent() throws IOException {
        // Arrange
        CRMEvent saleEvent = new CRMEvent(EventType.SALE_MADE, 789, "Product: Widget, Amount: $150");

        // Act
        salesLogger.onEvent(saleEvent);

        // Assert
        List<String> lines = Files.readAllLines(logFilePath);
        String logLine = lines.get(0);

        assertTrue(logLine.contains("Sale Made"));
        assertTrue(logLine.contains("789"));
        assertTrue(logLine.contains("Widget"));
        assertTrue(logLine.contains("$150"));
    }

    @Test
    void testNewlineAddedToEachEntry() throws IOException {
        // Act
        salesLogger.onEvent(EventType.SALE_MADE);
        salesLogger.onEvent(EventType.SALE_MADE);

        // Assert
        String content = Files.readString(logFilePath);
        assertTrue(content.contains("Sale Made\n"));
        // Should have two lines with newlines
        assertEquals(2, content.split("\n").length);
    }

    @Test
    void testOnlyFirstOnEventMethodFilters() throws IOException {
        // This test verifies the implementation detail that onEvent(EventType) filters
        // but onEvent(CRMEvent) does not

        // Act - Using EventType (should filter)
        salesLogger.onEvent(EventType.CUSTOMER_CREATED); // Should be filtered out

        // Act - Using CRMEvent (should NOT filter)
        salesLogger.onEvent(new CRMEvent(EventType.CUSTOMER_CREATED)); // Should be logged

        // Assert
        List<String> lines = Files.readAllLines(logFilePath);
        assertEquals(1, lines.size(), "Only the CRMEvent version should be logged");
        assertTrue(lines.get(0).contains("Customer Created"));
    }
}
