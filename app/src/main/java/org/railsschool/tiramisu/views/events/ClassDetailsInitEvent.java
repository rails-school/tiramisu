package org.railsschool.tiramisu.views.events;

/**
 * @class ClassDetailsInitEvent
 * @brief
 */
public class ClassDetailsInitEvent {
    private String _lessonSlug;

    public ClassDetailsInitEvent(String lessonSlug) {
        this._lessonSlug = lessonSlug;
    }

    public String getLessonSlug() {
        return _lessonSlug;
    }
}
