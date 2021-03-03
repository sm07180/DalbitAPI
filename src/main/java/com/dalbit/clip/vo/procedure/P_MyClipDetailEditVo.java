package com.dalbit.clip.vo.procedure;

import com.dalbit.clip.vo.request.MyClipDetailEditVo;
import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.MemberVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_MyClipDetailEditVo extends P_ApiVo {
    public P_MyClipDetailEditVo(){}
    public P_MyClipDetailEditVo(MyClipDetailEditVo myClipDetailEditVo, HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
        setCast_no(myClipDetailEditVo.getClipNo());
        setOpenType(myClipDetailEditVo.getOpenType());
    }

    private String mem_no;
    private String cast_no;
    private int openType; // 클립 공개 여부, 0: 비공개, 1: 공개

}
