package org.railsschool.tiramisu.views.events;

/**
 * @class ClassDetailsInitEvent
 * @brief
 */
public class ClassDetailsInitEvent {
    private int _lessonId;

    public ClassDetailsInitEvent(int lessonId) {
        this._lessonId = lessonId;
    }

    public int getLessonId() {
        return _lessonId;
    }
}
