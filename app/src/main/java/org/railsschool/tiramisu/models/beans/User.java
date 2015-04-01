package org.railsschool.tiramisu.models.beans;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @class User
 * @brief
 */
public class User extends RealmObject {
    @PrimaryKey
    private int     id;
    private String  name;
    private String  email;
    private boolean isTeacher;
    private boolean hideLastName;

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

    public boolean isTeacher() {
        return isTeacher;
    }

    public void setTeacher(boolean value) {
        isTeacher = value;
    }

    public boolean getHideLastName() {
        return hideLastName;
    }

    public void setHideLastName(boolean value) {
        hideLastName = value;
    }

    public String getDisplayName() {
        if (hideLastName) {
            String[] a = name.trim().split(" ");

            if (a.length > 0) {
                return a[0];
            } else {
                return name;
            }
        } else {
            return name;
        }
    }
}
