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
    private boolean isTeacher;

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

    public boolean isTeacher() {
        return isTeacher;
    }

    public void setTeacher(boolean value) {
        isTeacher = value;
    }
}
