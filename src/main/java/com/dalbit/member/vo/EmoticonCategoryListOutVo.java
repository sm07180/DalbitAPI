package com.dalbit.member.vo;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class EmoticonCategoryListOutVo {

    private int idx;               // 고유인덱스
    private int categoryOrderNo;   // 카테고리순번
    private String categoryNm;     // 카테고리명

    public EmoticonCategoryListOutVo(){}
    public EmoticonCategoryListOutVo(EmoticonCategoryListVo target){
        setIdx(target.getIdx());
        setCategoryOrderNo(target.getCategoryOrderNo());
        setCategoryNm(target.getCategoryNm());
    }

}
