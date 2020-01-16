package com.dalbit.util.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class IntegerAdapter extends TypeAdapter<Integer> {

    @Override
    public void write(JsonWriter writer, Integer value) throws IOException {
        if(value == null){
            writer.value(0);
        }else{
            writer.value(value);
        }
    }

    @Override
    public Integer read(JsonReader in) throws IOException {
        if(in.peek() == JsonToken.NULL){
            in.nextNull();
            return 0;
        }
        return in.nextInt();
    }
}
