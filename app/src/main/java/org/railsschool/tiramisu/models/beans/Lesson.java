package org.railsschool.tiramisu.models.beans;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @class Lesson
 * @brief
 */
public class Lesson extends RealmObject {
    @PrimaryKey
    private int    _id;
    private String _title;
    private String _summary;
    private String _description;
    private String _startTime;
    private String _endTime;
    private int    _teacherId;

    public int getId() {
        return _id;
    }

    public Lesson setId(int value) {
        _id = value;
        return this;
    }

    public String getTitle() {
        return _title;
    }

    public Lesson setTitle(String value) {
        _title = value;
        return this;
    }

    public String getSummary() {
        return _summary;
    }

    public Lesson setSummary(String value) {
        _summary = value;
        return this;
    }

    public String getDescription() {
        return _description;
    }

    public Lesson setDescription(String value) {
        _description = value;
        return this;
    }

    public String getStartTime() {
        return _startTime;
    }

    public Lesson setStartTime(String value) {
        _startTime = value;
        return this;
    }

    public String getEndTime() {
        return _endTime;
    }

    public Lesson setEndTime(String value) {
        _endTime = value;
        return this;
    }

    public int getTeacherId() {
        return _teacherId;
    }

    public Lesson setTeacherId(int value) {
        _teacherId = value;
        return this;
    }
}
