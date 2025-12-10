/*
 * 	Author: Ryan Jackson & Ryan Buck
 * 	Created: 12/10/2025
 * 	Final Project - Console Customer Relationship Management Tool
 */

package crm.controller.command;

import crm.observer.event.CRMEvent;
import crm.observer.event.EventType;

public interface CRMCommand {
    public CRMEvent execute();
    public void undo();
    public String getName();
    public EventType getEventType();
}
