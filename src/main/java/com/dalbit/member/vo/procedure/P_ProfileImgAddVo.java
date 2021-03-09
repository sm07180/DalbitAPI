package com.dalbit.member.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.ProfileImgAddVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_ProfileImgAddVo extends P_ApiVo {

    public P_ProfileImgAddVo(){}
    public P_ProfileImgAddVo(ProfileImgAddVo profileImgAddVo, HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
        setProfileImage(profileImgAddVo.getProfImg());
    }

    private String mem_no;
    private String profileImage;
}
