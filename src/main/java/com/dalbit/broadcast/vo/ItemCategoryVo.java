package com.dalbit.broadcast.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemCategoryVo {
    String code;
    String value;
    Boolean isNew;

    public ItemCategoryVo(){}

    public ItemCategoryVo(String code, String value, Boolean isNew){
        this.code = code;
        this.value = value;
        this.isNew = isNew;
    }
}
