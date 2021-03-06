package org.railsschool.tiramisu.models.dao;

public enum DayNotificationPreference {
    ALWAYS(0),
    IF_ATTENDING(1),
    NEVER(2);

    private int _value;

    DayNotificationPreference(int value) {
        this._value = value;
    }

    public int toInt() {
        return _value;
    }

    public static DayNotificationPreference fromInt(int value) {
        switch (value) {
            case 1:
                return IF_ATTENDING;
            case 2:
                return NEVER;
            default:
                return ALWAYS;
        }
    }

    public String toString() {
        return Integer.toString(_value);
    }
}
