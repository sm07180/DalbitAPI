package com.demo.util.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class FloatAdapter extends TypeAdapter<Float> {

    @Override
    public void write(JsonWriter writer, Float value) throws IOException {
        if(value == null){
            writer.value(0.0);
        }else{
            writer.value(value);
        }
    }

    @Override
    public Float read(JsonReader in) throws IOException {
        if(in.peek() == JsonToken.NULL){
            in.nextNull();
            return (float)0.0;
        }
        return Float.valueOf(in.nextString());
    }
}
