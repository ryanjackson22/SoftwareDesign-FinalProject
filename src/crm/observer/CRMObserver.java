package crm.observer;

import crm.observer.event.CRMEvent;
import crm.observer.event.EventType;

public interface CRMObserver {
    public void onEvent(EventType eventType);
    public void onEvent(CRMEvent crmEvent);
}
