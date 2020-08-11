package com.dalbit.member.vo;

import com.dalbit.member.vo.procedure.P_EmoticonListVo;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class EmoticonListOutVo {

    private int idx;               // 고유인덱스
    private int categoryOrderNo;   // 카테고리순번
    private int emoticonOrderNo;   // 이모티콘순번
    private int categoryNo;        // 카테고리 번호
    private String categoryNm;     // 카테고리명
    private String emoticonDesc;   // 이모티콘내용

    public EmoticonListOutVo(){}
    public EmoticonListOutVo(P_EmoticonListVo target){
        setIdx(target.getIdx());
        setCategoryOrderNo(target.getCategory_orderNo());
        setEmoticonOrderNo(target.getEmoticon_orderNo());
        setCategoryNo(target.getCategoryNo());
        setCategoryNm(target.getCategoryNm());
        setEmoticonDesc(target.getEmoticon_desc());
    }
}
