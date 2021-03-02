package com.dalbit.common.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.MemberVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_KingFanRankListVo extends P_ApiVo {

    public P_KingFanRankListVo(){}
    public P_KingFanRankListVo(String roomNo, HttpServletRequest request){
        setRoom_no(roomNo);
        setMem_no(MemberVo.getMyMemNo(request));
    }

    /* Input */
    private String room_no;

    private String mem_no;
    private String nickName;
    private String memSex;
    private String age;
    private String profileImage;

}
