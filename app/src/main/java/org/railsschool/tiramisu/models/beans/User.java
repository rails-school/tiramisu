package org.railsschool.tiramisu.models.beans;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @class User
 * @brief
 */
public class User extends RealmObject {
    @PrimaryKey
    private int     _id;
    private String  _name;
    private boolean _teacher;

    public int getId() {
        return _id;
    }

    public User setId(int value) {
        _id = value;
        return this;
    }

    public String getName() {
        return _name;
    }

    public User setName(String value) {
        _name = value;
        return this;
    }

    public boolean isTeacher() {
        return _teacher;
    }

    public User isTeacher(boolean value) {
        _teacher = value;
        return this;
    }
}
