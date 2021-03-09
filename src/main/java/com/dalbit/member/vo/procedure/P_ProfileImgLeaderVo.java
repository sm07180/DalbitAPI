package com.dalbit.member.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.ProfileImgLeaderVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_ProfileImgLeaderVo extends P_ApiVo {
    public P_ProfileImgLeaderVo(){}
    public P_ProfileImgLeaderVo(ProfileImgLeaderVo profileImgLeaderVo, HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
        setIdx(profileImgLeaderVo.getIdx());
    }

    private String mem_no;
    private int idx;
}
