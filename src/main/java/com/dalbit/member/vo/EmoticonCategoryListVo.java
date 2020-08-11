package com.dalbit.member.vo;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class EmoticonCategoryListVo {

    private int idx;               // 고유인덱스
    private int categoryOrderNo;   // 카테고리순번
    private String categoryNm;     // 카테고리명

}
