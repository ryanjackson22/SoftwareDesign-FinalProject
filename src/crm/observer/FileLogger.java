package crm.observer;

import crm.observer.event.EventType;

import java.io.FileWriter;
import java.io.IOException;

public class FileLogger implements CRMObserver {
    private String filePath;

    @Override
    public void onEvent(EventType eventType) {
        writeToFile(eventType.toString());
    }

    public void writeToFile(String contents) {
        try {
            FileWriter logFileWriter = new FileWriter(this.filePath);
            logFileWriter.write(contents);
            logFileWriter.close();
        } catch (IOException e) {
            System.out.println("Unable to write to log file: " + this.filePath);
        }
    }
}
