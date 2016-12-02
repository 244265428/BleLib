package com.czjk.blelib.model;

/**
 * to accecpt evnetbus event base class
 */
public class BaseEvent {
    private EventType mEventType;
    public EventType getEventType() {
        return mEventType;
    }

    public BaseEvent(EventType mEventType) {
        this.mEventType = mEventType;
    }

    public enum EventType {
        STATE_CONNECTED,
        STATE_DISCONNECTED,
        STATE_CONNECT_FAILURE,

        SET_TIME_SUCCESS,
        UNBIND_SUCCESS,
        UNBIND_FAILURE,
        ENTER_OTA_SUCCESS,
        ENTER_OTA_FAILURE


    }
}
