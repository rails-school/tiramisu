package org.railsschool.tiramisu.models.bll.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.railsschool.tiramisu.models.bll.structs.CheckCredentialsRequest;

import java.lang.reflect.Type;

/**
 * @class CheckCredentialsRequestSerializer
 * @brief
 */
public class CheckCredentialsRequestSerializer implements JsonSerializer<CheckCredentialsRequest> {

    @Override
    public JsonElement serialize(CheckCredentialsRequest src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject content = new JsonObject(), wrapper = new JsonObject();

        content.addProperty("email", src.getEmail());
        content.addProperty("password", src.getPassword());
        content.addProperty("remember_me", 1);

        wrapper.add("user", content);

        return wrapper;
    }
}
