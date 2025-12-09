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

class FileLoggerTest {

    @TempDir
    Path tempDir;

    private FileLogger fileLogger;
    private Path logFilePath;
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalErr = System.out;

    @BeforeEach
    void setUp() throws IOException {
        logFilePath = tempDir.resolve("test-log.txt");
        fileLogger = new FileLogger(logFilePath.toString());
        System.setOut(new PrintStream(errContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalErr);
    }

    @Test
    void testOnEventWithEventTypeWritesToFile() throws IOException {
        // Act
        fileLogger.onEvent(EventType.CUSTOMER_CREATED);

        // Assert
        assertTrue(Files.exists(logFilePath), "Log file should be created");

        List<String> lines = Files.readAllLines(logFilePath);
        assertEquals(1, lines.size(), "Should have one line");
        assertEquals("Customer Created", lines.get(0));
    }

    @Test
    void testOnEventWithCRMEventWritesToFile() throws IOException {
        // Arrange
        CRMEvent event = new CRMEvent(EventType.SALE_MADE, 123, "Sale completed");

        // Act
        fileLogger.onEvent(event);

        // Assert
        assertTrue(Files.exists(logFilePath), "Log file should be created");

        List<String> lines = Files.readAllLines(logFilePath);
        assertEquals(1, lines.size(), "Should have one line");
        assertTrue(lines.get(0).contains("Sale Made"));
        assertTrue(lines.get(0).contains("123"));
        assertTrue(lines.get(0).contains("Sale completed"));
    }

    @Test
    void testMultipleEventsAppendToFile() throws IOException {
        // Act
        fileLogger.onEvent(EventType.CUSTOMER_CREATED);
        fileLogger.onEvent(EventType.CUSTOMER_UPDATED);
        fileLogger.onEvent(EventType.SALE_MADE);

        // Assert
        List<String> lines = Files.readAllLines(logFilePath);
        assertEquals(3, lines.size(), "Should have three lines");
        assertEquals("Customer Created", lines.get(0));
        assertEquals("Customer Updated", lines.get(1));
        assertEquals("Sale Made", lines.get(2));
    }

    @Test
    void testFileLoggerAppendsNotOverwrites() throws IOException {
        // Arrange - Write initial content
        Files.writeString(logFilePath, "Initial content\n");

        // Act
        fileLogger.onEvent(EventType.CUSTOMER_CREATED);

        // Assert
        List<String> lines = Files.readAllLines(logFilePath);
        assertEquals(2, lines.size(), "Should have two lines");
        assertEquals("Initial content", lines.get(0));
        assertEquals("Customer Created", lines.get(1));
    }

    @Test
    void testAllEventTypesCanBeLogged() throws IOException {
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
            fileLogger.onEvent(type);
        }

        // Assert
        List<String> lines = Files.readAllLines(logFilePath);
        assertEquals(6, lines.size(), "Should have six lines");

        for (int i = 0; i < allTypes.length; i++) {
            assertEquals(allTypes[i].toString(), lines.get(i));
        }
    }

    @Test
    void testCRMEventWithAllFieldsLogged() throws IOException {
        // Arrange
        CRMEvent event = new CRMEvent(EventType.NOTIFICATION_SENT, 456, "Email sent to customer");

        // Act
        fileLogger.onEvent(event);

        // Assert
        List<String> lines = Files.readAllLines(logFilePath);
        String loggedLine = lines.get(0);

        assertTrue(loggedLine.contains("Notification Sent"));
        assertTrue(loggedLine.contains("456"));
        assertTrue(loggedLine.contains("Email sent to customer"));
    }

    @Test
    void testCRMEventWithMinimalFieldsLogged() throws IOException {
        // Arrange
        CRMEvent event = new CRMEvent(EventType.CUSTOMER_CREATED);

        // Act
        fileLogger.onEvent(event);

        // Assert
        List<String> lines = Files.readAllLines(logFilePath);
        String loggedLine = lines.get(0);

        assertTrue(loggedLine.contains("Customer Created"));
        assertTrue(loggedLine.contains("Time:"));
    }

    @Test
    void testWriteToFileDirectly() throws IOException {
        // Act
        fileLogger.writeToFile("Custom log message");

        // Assert
        List<String> lines = Files.readAllLines(logFilePath);
        assertEquals(1, lines.size());
        assertEquals("Custom log message", lines.get(0));
    }

    @Test
    void testMultipleWritesToFileAppend() throws IOException {
        // Act
        fileLogger.writeToFile("First message");
        fileLogger.writeToFile("Second message");
        fileLogger.writeToFile("Third message");

        // Assert
        List<String> lines = Files.readAllLines(logFilePath);
        assertEquals(3, lines.size());
        assertEquals("First message", lines.get(0));
        assertEquals("Second message", lines.get(1));
        assertEquals("Third message", lines.get(2));
    }

    @Test
    void testInvalidFilePathHandlesException() {
        // Arrange - Create logger with invalid path
        FileLogger invalidLogger = new FileLogger("/invalid/path/that/does/not/exist/log.txt");

        // Act
        invalidLogger.onEvent(EventType.CUSTOMER_CREATED);

        // Assert - Should print error message but not throw exception
        String output = errContent.toString();
        assertTrue(output.contains("Unable to write to log file"));
    }

    @Test
    void testEmptyStringCanBeLogged() throws IOException {
        // Act
        fileLogger.writeToFile("");

        // Assert
        List<String> lines = Files.readAllLines(logFilePath);
        assertEquals(1, lines.size());
        assertEquals("", lines.get(0));
    }

    @Test
    void testLongMessageCanBeLogged() throws IOException {
        // Arrange
        String longMessage = "This is a very long message that contains a lot of information ".repeat(10);

        // Act
        fileLogger.writeToFile(longMessage);

        // Assert
        List<String> lines = Files.readAllLines(logFilePath);
        assertEquals(1, lines.size());
        assertEquals(longMessage, lines.get(0));
    }

    @Test
    void testSpecialCharactersAreLogged() throws IOException {
        // Arrange
        String specialChars = "Special: !@#$%^&*()_+-=[]{}|;':\",./<>?";

        // Act
        fileLogger.writeToFile(specialChars);

        // Assert
        List<String> lines = Files.readAllLines(logFilePath);
        assertEquals(1, lines.size());
        assertEquals(specialChars, lines.get(0));
    }

    @Test
    void testMultipleFileLoggersToSameFile() throws IOException {
        // Arrange
        FileLogger logger1 = new FileLogger(logFilePath.toString());
        FileLogger logger2 = new FileLogger(logFilePath.toString());

        // Act
        logger1.onEvent(EventType.CUSTOMER_CREATED);
        logger2.onEvent(EventType.CUSTOMER_UPDATED);

        // Assert
        List<String> lines = Files.readAllLines(logFilePath);
        assertEquals(2, lines.size());
        assertEquals("Customer Created", lines.get(0));
        assertEquals("Customer Updated", lines.get(1));
    }

    @Test
    void testFileLoggerImplementsCRMObserver() {
        // Assert
        assertTrue(fileLogger instanceof CRMObserver, "FileLogger should implement CRMObserver");
    }

    @Test
    void testNewlineIsAddedToEachEntry() throws IOException {
        // Act
        fileLogger.writeToFile("Line 1");
        fileLogger.writeToFile("Line 2");

        // Assert
        String content = Files.readString(logFilePath);
        // Each writeToFile adds a newline, so we should have proper line separation
        assertTrue(content.contains("Line 1\n"));
        assertTrue(content.contains("Line 2\n"));
    }

    @Test
    void testMixedEventTypesAndCRMEvents() throws IOException {
        // Act
        fileLogger.onEvent(EventType.CUSTOMER_CREATED);
        fileLogger.onEvent(new CRMEvent(EventType.SALE_MADE, 100, "Sale info"));
        fileLogger.onEvent(EventType.CUSTOMER_UPDATED);
        fileLogger.onEvent(new CRMEvent(EventType.NOTIFICATION_SENT, 200));

        // Assert
        List<String> lines = Files.readAllLines(logFilePath);
        assertEquals(4, lines.size());
        assertEquals("Customer Created", lines.get(0));
        assertTrue(lines.get(1).contains("Sale Made"));
        assertEquals("Customer Updated", lines.get(2));
        assertTrue(lines.get(3).contains("Notification Sent"));
    }
}
