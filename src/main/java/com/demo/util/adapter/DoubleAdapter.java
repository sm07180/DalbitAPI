package com.demo.util.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class DoubleAdapter extends TypeAdapter<Double> {

    @Override
    public void write(JsonWriter writer, Double value) throws IOException {
        if(value == null){
            writer.value(0.0);
        }else{
            writer.value(value);
        }
    }

    @Override
    public Double read(JsonReader in) throws IOException {
        if(in.peek() == JsonToken.NULL){
            in.nextNull();
            return 0.0;
        }
        return Double.valueOf(in.nextString());
    }
}
