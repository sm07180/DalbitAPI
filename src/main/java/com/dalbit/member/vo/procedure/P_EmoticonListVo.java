package com.dalbit.member.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.MemberVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter @Setter
public class P_EmoticonListVo extends P_ApiVo {

    public P_EmoticonListVo(){}
    public P_EmoticonListVo(HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
    }

    /* Input */
    private String mem_no;

    /* Output */
    private int idx;                // 고유인덱스
    private int category_orderNo;   // 카테고리순번
    private int emoticon_orderNo;   // 이모티콘순번
    private int categoryNo;         // 카테고리 번호
    private String categoryNm;      // 카테고리명
    private String emoticon_desc;   // 이모티콘내용
}
