/*
 * 	Author: Ryan Jackson & Ryan Buck
 * 	Created: 12/10/2025
 * 	Final Project - Console Customer Relationship Management Tool
 */

package crm.observer;

import crm.observer.event.CRMEvent;
import crm.observer.event.EventType;

public interface CRMObserver {
    public void onEvent(EventType eventType);
    public void onEvent(CRMEvent crmEvent);
}
