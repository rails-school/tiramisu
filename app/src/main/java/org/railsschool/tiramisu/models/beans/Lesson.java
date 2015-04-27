package org.railsschool.tiramisu.models.beans;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @class Lesson
 * @brief
 */
public class Lesson extends RealmObject {
    private int id;

    @PrimaryKey
    private String slug;
    private String title;
    private String summary;
    private String description;
    private String startTime;
    private String endTime;
    private int    teacherId;
    private int    venueId;
    private Date   updateDate;

    public int getId() {
        return id;
    }

    public void setId(int value) {
        id = value;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String value) {
        slug = value;
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

    public int getVenueId() {
        return venueId;
    }

    public void setVenueId(int venueId) {
        this.venueId = venueId;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date value) {
        updateDate = value;
    }
}
