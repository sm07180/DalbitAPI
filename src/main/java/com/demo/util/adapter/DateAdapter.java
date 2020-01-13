package com.demo.util.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateAdapter extends TypeAdapter<Date> {
    SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void write(JsonWriter writer, Date value) throws IOException {
        if(value == null){
            writer.value("");
        }else{
            writer.value(transFormat.format(value));
        }
    }

    @Override
    public Date read(JsonReader in) throws IOException {
        if(in.peek() == JsonToken.NULL){
            in.nextNull();
            return null;
        }
        Date result = null;
        try{
            result = transFormat.parse(in.nextString());
        }catch(ParseException e){}

        return result;
    }
}
