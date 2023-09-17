package com.example.whatsopwebandorid.room;

import androidx.room.TypeConverter;

import com.example.whatsopwebandorid.Message;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class MessageListConverter {
    @TypeConverter
    public static List<Message> fromString(String value) {
        Type listType = new TypeToken<List<Message>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String toString(List<Message> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }
}
