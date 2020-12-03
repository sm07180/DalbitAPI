package com.dalbit.common.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class P_KingFanRankListVo extends P_ApiVo {

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
