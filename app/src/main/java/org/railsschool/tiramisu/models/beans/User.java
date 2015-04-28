package org.railsschool.tiramisu.models.beans;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @class User
 * @brief Any user of RailsSchool. Could be an admin, teacher or regular user.
 */
public class User extends RealmObject {
    @PrimaryKey
    private int id;

    private String  name;
    private String  email;
    private boolean teacher;

    /**
     * Needed here for hiding last name within app too
     */
    private boolean hideLastName;

    /**
     * Internal purpose only
     */
    private Date updateDate;

    public int getId() {
        return id;
    }

    public void setId(int value) {
        id = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String value) {
        name = value;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String value) {
        email = value;
    }

    public boolean getTeacher() {
        return teacher;
    }

    public void setTeacher(boolean value) {
        teacher = value;
    }

    public boolean getHideLastName() {
        return hideLastName;
    }

    public void setHideLastName(boolean value) {
        hideLastName = value;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date value) {
        updateDate = value;
    }
}
