package org.railsschool.tiramisu.views.events;

/**
 * @class ClassDetailsRequestedEvent
 * @brief
 */
public class ClassDetailsRequestedEvent {
    private int _lessonId;

    public ClassDetailsRequestedEvent(int lessonId) {
        this._lessonId = lessonId;
    }

    public int getLessonId() {
        return _lessonId;
    }
}
