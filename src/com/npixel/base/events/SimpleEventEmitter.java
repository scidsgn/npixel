package com.npixel.base.events;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class SimpleEventEmitter<Ev, T> {
    private static class EventHandler<Ev, T> {
        private final Ev eventType;
        private final Function<T, Void> handler;

        public EventHandler(Ev eventType, Function<T, Void> handler) {
            this.eventType = eventType;
            this.handler = handler;
        }

        public Ev getEventType() {
            return eventType;
        }

        public Function<T, Void> getHandler() {
            return handler;
        }
    }

    private final List<EventHandler<Ev, T>> eventHandlerList;

    public SimpleEventEmitter() {
        eventHandlerList = new ArrayList<>();
    }

    protected void emit(Ev eventType, T value) {
        for (EventHandler<Ev, T> eh : eventHandlerList) {
            if (eh.getEventType().equals(eventType)) {
                eh.getHandler().apply(value);
            }
        }
    }

    public void on(Ev eventType, Function<T, Void> handler) {
        eventHandlerList.add(new EventHandler<>(eventType, handler));
    }

    public void off(Ev eventType, Function<T, Void> handler) {
        eventHandlerList.removeIf(eh -> eh.getEventType().equals(eventType) && handler == eh.getHandler());
    }
}
