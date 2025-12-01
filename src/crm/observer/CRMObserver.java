package crm.observer;

import crm.observer.event.EventType;

public interface CRMObserver {
    public void onEvent(EventType eventType);
}
