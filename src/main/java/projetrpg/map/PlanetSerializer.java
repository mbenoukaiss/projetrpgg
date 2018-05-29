package projetrpg.map;

import com.google.gson.*;

import java.lang.reflect.Type;

public class PlanetSerializer implements JsonSerializer<Planet>, JsonDeserializer<Planet> {

    @Override
    public JsonElement serialize(Planet planet, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject regionPart = jsonSerializationContext.serialize(planet, Region.class).getAsJsonObject();

        regionPart.addProperty("type", "planet");
        regionPart.addProperty("landingRegion", planet.getLandingRegion().id);

        return regionPart;
    }

    @Override
    public Planet deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        Region regionPart = jsonDeserializationContext.deserialize(jsonElement, Region.class);
        Planet p = new Planet(regionPart);
        MapSerializer.addPlanet(p, jsonElement.getAsJsonObject().get("landingRegion").getAsInt());
        return p;
    }

}
