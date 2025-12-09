package crm.observer;

import crm.observer.event.CRMEvent;
import crm.observer.event.EventType;

import java.io.FileWriter;
import java.io.IOException;

public class SalesLogger implements CRMObserver {
    private final String filePath;

    public SalesLogger(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void onEvent(EventType eventType) {
        if (eventType != EventType.SALE_MADE) {
            return;
        }

        writeToFile(eventType.toString());
    }

    @Override
    public void onEvent(CRMEvent crmEvent) {
        writeToFile(crmEvent.toString());
    }

    public void writeToFile(String contents) {
        try {
            FileWriter logFileWriter = new FileWriter(this.filePath, true);
            logFileWriter.write(contents + "\n");
            logFileWriter.close();
        } catch (IOException e) {
            System.out.println("Unable to write to log file: " + this.filePath);
        }
    }
}
