package org.railsschool.tiramisu.models.bll.serializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.railsschool.tiramisu.models.beans.User;

import java.lang.reflect.Type;

/**
 * @class UserSerializer
 * @brief
 */
public class UserSerializer implements JsonDeserializer<User> {
    @Override
    public User deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        User outcome = new User();
        JsonObject o = json.getAsJsonObject();

        outcome
            .setId(o.get("id").getAsInt())
            .setName(o.get("name").getAsString())
            .isTeacher(o.get("teacher").getAsBoolean());

        return outcome;
    }
}
