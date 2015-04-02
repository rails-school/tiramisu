package org.railsschool.tiramisu.models.bll.serializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import org.railsschool.tiramisu.models.beans.Lesson;
import org.railsschool.tiramisu.models.beans.User;
import org.railsschool.tiramisu.models.bll.structs.SchoolClass;

import java.lang.reflect.Type;
import java.util.List;

/**
 * @class SchoolClassSerializer
 * @brief
 */
public class SchoolClassSerializer implements JsonDeserializer<SchoolClass> {
    @Override
    public SchoolClass deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        SchoolClass outcome;

        outcome = new SchoolClass();
        outcome
            .setLesson(context.deserialize(json, Lesson.class))
            .setStudents(
                context.deserialize(
                    json.getAsJsonObject().get("students"),
                    new TypeToken<List<User>>() {
                    }.getType()
                )
            );

        return outcome;
    }
}
