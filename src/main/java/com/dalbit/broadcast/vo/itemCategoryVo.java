package com.dalbit.broadcast.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class itemCategoryVo {
    String code;
    String value;
    Boolean isNew;

    public itemCategoryVo(){}

    public itemCategoryVo(String code, String value, Boolean isNew){
        this.code = code;
        this.value = value;
        this.isNew = isNew;
    }
}
