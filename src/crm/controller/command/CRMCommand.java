package crm.controller.command;

import crm.observer.event.EventType;

public interface CRMCommand {
    public void execute();
    public void undo();
    public String getName();
    public EventType getEventType();
}
