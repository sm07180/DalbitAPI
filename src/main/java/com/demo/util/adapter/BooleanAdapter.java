package com.demo.util.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class BooleanAdapter extends TypeAdapter<Boolean> {

    @Override
    public void write(JsonWriter writer, Boolean value) throws IOException {
        if(value == null){
            writer.value(false);
        }else{
            writer.value(value);
        }
    }

    @Override
    public Boolean read(JsonReader in) throws IOException {
        if(in.peek() == JsonToken.NULL){
            in.nextNull();
            return false;
        }
        return Boolean.valueOf(in.nextString());
    }
}
