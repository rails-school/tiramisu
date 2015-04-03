package org.railsschool.tiramisu.views.events;

/**
 * @class ClassDetailsHeaderInitEvent
 * @brief
 */
public class ClassDetailsHeaderInitEvent {
    private String _lessonSlug;

    public ClassDetailsHeaderInitEvent(String lessonSlug) {
        this._lessonSlug = lessonSlug;
    }

    public String getLessonSlug() {
        return _lessonSlug;
    }
}
