package com.dalbit.member.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.ProfileImgDeleteVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_ProfileImgDeleteVo extends P_ApiVo {

    public P_ProfileImgDeleteVo(){}
    public P_ProfileImgDeleteVo(ProfileImgDeleteVo profileImgDeleteVo, HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
        setIdx(profileImgDeleteVo.getIdx());
    }

    private String mem_no;
    private int idx;
}
