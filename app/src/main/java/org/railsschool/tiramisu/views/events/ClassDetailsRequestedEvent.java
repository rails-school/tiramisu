package org.railsschool.tiramisu.views.events;

/**
 * @class ClassDetailsRequestedEvent
 * @brief
 */
public class ClassDetailsRequestedEvent {
    private String _lessonSlug;

    public ClassDetailsRequestedEvent(String lessonSlug) {
        this._lessonSlug = lessonSlug;
    }

    public String getLessonSlug() {
        return _lessonSlug;
    }
}
