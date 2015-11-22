package com.vk_random.vk_random.RepCount;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ParserRepost
{
    public String parse(String text)
    {
        JsonElement element = new JsonParser().parse(text);
        JsonObject jsonObject = element.getAsJsonObject();
        jsonObject = jsonObject.getAsJsonObject("response");
        JsonArray jsonArray = jsonObject.getAsJsonArray("items");
        String newText="";
        for(final JsonElement type : jsonArray)
        {
            jsonObject = type.getAsJsonObject();
            newText = jsonObject.get("reposts").toString();
        }

        return newText;
    }

}
