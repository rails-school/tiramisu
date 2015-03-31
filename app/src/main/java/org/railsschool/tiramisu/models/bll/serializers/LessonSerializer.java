package org.railsschool.tiramisu.models.bll.serializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.railsschool.tiramisu.models.beans.Lesson;

import java.lang.reflect.Type;

/**
 * @class LessonSerializer
 * @brief
 */
public class LessonSerializer implements JsonDeserializer<Lesson> {
    @Override
    public Lesson deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Lesson outcome = new Lesson();
        JsonObject o = json.getAsJsonObject();

        outcome
            .setId(o.get("id").getAsInt())
            .setTitle(o.get("title").getAsString())
            .setSummary(o.get("summary").getAsString())
            .setDescription(o.get("description").getAsString())
            .setStartTime(o.get("start_time").getAsString())
            .setEndTime(o.get("end_time").getAsString())
            .setTeacherId(o.get("teacher_id").getAsInt());

        return outcome;
    }
}
