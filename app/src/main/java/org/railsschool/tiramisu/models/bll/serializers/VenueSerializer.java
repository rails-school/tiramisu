package org.railsschool.tiramisu.models.bll.serializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.railsschool.tiramisu.models.beans.Venue;

import java.lang.reflect.Type;

/**
 * @class VenueSerializer
 * @brief
 */
public class VenueSerializer implements JsonDeserializer<Venue> {
    @Override
    public Venue deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Venue outcome = new Venue();
        JsonObject o = json.getAsJsonObject();

        outcome.setId(o.get("id").getAsInt());
        outcome.setZip(o.get("zip").getAsString());
        outcome.setLatitude(o.get("latitude").getAsFloat());
        outcome.setLongitude(o.get("longitude").getAsFloat());
        outcome.setName(o.get("name").getAsString());
        outcome.setAddress(o.get("address").getAsString());
        outcome.setCity(o.get("city").getAsString());
        outcome.setState(o.get("state").getAsString());
        outcome.setCountry(o.get("country").getAsString());

        return outcome;
    }
}
