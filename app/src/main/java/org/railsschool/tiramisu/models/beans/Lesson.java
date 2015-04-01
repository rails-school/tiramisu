package org.railsschool.tiramisu.models.beans;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @class Lesson
 * @brief
 */
public class Lesson extends RealmObject {
    @PrimaryKey
    private int    id;
    private String title;
    private String summary;
    private String description;
    private String startTime;
    private String endTime;
    private int    teacherId;

    public int getId() {
        return id;
    }

    public void setId(int value) {
        id = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String value) {
        title = value;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String value) {
        summary = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String value) {
        description = value;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String value) {
        startTime = value;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String value) {
        endTime = value;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int value) {
        teacherId = value;
    }
}
