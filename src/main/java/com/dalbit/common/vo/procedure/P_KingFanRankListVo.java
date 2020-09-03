package com.dalbit.common.vo.procedure;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class P_KingFanRankListVo {

    public P_KingFanRankListVo(){}
    public P_KingFanRankListVo(String roomNo){
        setRoom_no(roomNo);
    }

    /* Input */
    private String room_no;

    private String mem_no;
    private String nickName;
    private String memSex;
    private String age;
    private String profileImage;

}
