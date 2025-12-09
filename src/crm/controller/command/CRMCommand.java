package crm.controller.command;

import crm.observer.event.CRMEvent;
import crm.observer.event.EventType;

public interface CRMCommand {
    public CRMEvent execute();
    public void undo();
    public String getName();
    public EventType getEventType();
}
