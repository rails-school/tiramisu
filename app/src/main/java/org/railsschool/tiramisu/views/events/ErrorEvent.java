package org.railsschool.tiramisu.views.events;

/**
 * @class ErrorEvent
 * @brief
 */
public class ErrorEvent {
    private String _message;

    public ErrorEvent(String message) {
        this._message = message;
    }

    public String getMessage() {
        return _message;
    }
}
