package org.railsschool.tiramisu.models.dao;

public enum TwoHourNotificationPreference {
    ALWAYS(0),
    IF_ATTENDING(1),
    NEVER(2);

    private int _value;

    TwoHourNotificationPreference(int value) {
        this._value = value;
    }

    public int toInt() {
        return _value;
    }

    public static TwoHourNotificationPreference fromInt(int value) {
        switch (value) {
            case 0:
                return ALWAYS;
            case 2:
                return NEVER;
            default:
                return IF_ATTENDING;
        }
    }

    public String toString() {
        return Integer.toString(_value);
    }
}
