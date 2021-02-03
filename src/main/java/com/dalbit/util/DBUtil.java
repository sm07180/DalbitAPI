package com.dalbit.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DBUtil {

    public static <T> T getData(List<?> list, int idx, Class<T> obj) {
        for(int i = 0; i < list.size() ; i++) {
            Object data = list.get(i);
            if( data.getClass().isAssignableFrom(ArrayList.class) ) {
                List<Object> _list = (ArrayList)data;

                if( _list.size() > 0 && !obj.isAssignableFrom( Collections.class) && _list.get(0) != null && obj.isAssignableFrom( _list.get(0).getClass() ) ){
                    if(idx == i) {
                        return (T) _list.get(0);
                    }
                }
            }
        }
        try {
            return obj.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> List<T> getList(List<?> list, Integer idx, Class<T> obj) {

        for(int i = 0; i < list.size() ; i++) {
            Object data = list.get(i);
            if( data.getClass().isAssignableFrom(ArrayList.class) ) {
                List<Object> _list = (ArrayList)data;
                if( _list.size() > 0 && _list.get(0) != null && obj.isAssignableFrom( _list.get(0).getClass() ) ) {
                    if( idx == i )
                        return (List<T>)_list;
                }
            }
        }

        return new ArrayList<>();
    }
}
