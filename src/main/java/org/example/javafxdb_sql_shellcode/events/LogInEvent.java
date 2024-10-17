package org.example.javafxdb_sql_shellcode.events;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

public class LogInEvent extends Event {

    public LogInEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }

    public LogInEvent(Object o, EventTarget eventTarget, EventType<? extends Event> eventType) {
        super(o, eventTarget, eventType);
    }
}
