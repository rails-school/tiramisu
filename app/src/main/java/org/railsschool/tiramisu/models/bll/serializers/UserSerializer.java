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

        outcome.setId(o.get("id").getAsInt());
        outcome.setSchoolId(o.get("school_id").getAsInt());

        // Optional fields
        outcome.setName(o.has("name") ? o.get("name").getAsString() : "");
        outcome.setEmail(o.has("email") ? o.get("email").getAsString() : "");
        outcome.setHideLastName(
            o.has("hide_last_name") ? o.get("hide_last_name").getAsBoolean() : true
        );

        return outcome;
    }
}
