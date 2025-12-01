package crm.observer;

import crm.observer.event.EventType;

public class SalesLogger implements CRMObserver {
    private String filePath;

    @Override
    public void onEvent(EventType eventType) {
        // stub
    }

    public void writeToFile(String filePath) {
        // stub
    }
}
