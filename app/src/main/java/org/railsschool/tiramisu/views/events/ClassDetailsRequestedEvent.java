package org.railsschool.tiramisu.views.events;

/**
 * @class ClassDetailsRequestedEvent
 * @brief Triggered when user has selected a lesson from landing list
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
