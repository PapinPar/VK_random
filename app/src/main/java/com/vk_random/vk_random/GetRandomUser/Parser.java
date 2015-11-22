package com.vk_random.vk_random.GetRandomUser;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;

/**
 * Created by Papin on 23.08.2015.
 */
public class Parser  {
    public ArrayList<String> parse(String text)
    {
        JsonElement element = new JsonParser().parse(text);
        JsonObject jsonObject = element.getAsJsonObject();
        jsonObject = jsonObject.getAsJsonObject("response");
        JsonArray jsonArray = jsonObject.getAsJsonArray("profiles");
        ArrayList<String> newText = new ArrayList<>(jsonArray.size());
        for(final JsonElement type : jsonArray)
        {
            jsonObject = type.getAsJsonObject();
            newText.add(jsonObject.get("id").toString()+"\n"
                    +jsonObject.get("first_name".toString())
                    +" "
                    +jsonObject.get("last_name".toString()));
        }

        return newText;
    }
}
