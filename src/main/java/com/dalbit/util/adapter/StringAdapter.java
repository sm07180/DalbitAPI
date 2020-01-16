package com.dalbit.util.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class StringAdapter extends TypeAdapter<String> {

    @Override
    public void write(JsonWriter writer, String value) throws IOException {
        if(value == null){
            writer.value("");
        }else{
            writer.value(value.trim());
        }
    }

    @Override
    public String read(JsonReader in) throws IOException {
        if(in.peek() == JsonToken.NULL){
            in.nextNull();
            return "";
        }
        return in.nextString().trim();
    }
}
