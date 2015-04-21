package org.railsschool.tiramisu.views.events;

/**
 * @class InformationEvent
 * @brief
 */
public class InformationEvent {
    private String _message;

    public InformationEvent(String message) { this._message = message; }

    public String getMessage() {
        return _message;
    }
}
