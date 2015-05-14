package org.railsschool.tiramisu.views.events;

/**
 * @class ConfirmationEvent
 * @brief
 */
public class ConfirmationEvent {
    private String _message;

    public ConfirmationEvent(String message) {
        this._message = message;
    }

    public String getMessage() {
        return _message;
    }
}
