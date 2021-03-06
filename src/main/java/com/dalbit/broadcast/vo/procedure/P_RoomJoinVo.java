package com.dalbit.broadcast.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_RoomJoinVo extends P_ApiVo {

    private int memLogin;                       //회원 로그인 상태(1: 회원, 0: 비회원)
    private String mem_no;                      //참가하려는 회원번호
    private String room_no;                     //참가하려는 방 번호
    private String guest_streamid;              //guest 스트림아이디
    private String guest_publish_tokenid;       //guest 토큰아이디
    private String guest_play_tokenid;          //guest play토큰
    private String bj_streamid;                 //bj 스트림아이디
    private String bj_publish_tokenid;          //bj 토큰아이디
    private String bj_play_tokenid;             //bj play토큰
    private int shadow;

    private int os;
    private String deviceUuid;
    private String ip;
    private String appVersion;

    @Override
    public String toString() {
        return "P_RoomJoinVo{" +
                "memLogin=" + memLogin +
                ", mem_no='" + mem_no + '\'' +
                ", room_no='" + room_no + '\'' +
                ", guest_streamid='" + guest_streamid + '\'' +
                ", guest_publish_tokenid='" + guest_publish_tokenid + '\'' +
                ", guest_play_tokenid='" + guest_play_tokenid + '\'' +
                ", bj_streamid='" + bj_streamid + '\'' +
                ", bj_publish_tokenid='" + bj_publish_tokenid + '\'' +
                ", bj_play_tokenid='" + bj_play_tokenid + '\'' +
                ", shadow=" + shadow +
                ", os=" + os +
                ", deviceUuid='" + deviceUuid + '\'' +
                ", ip='" + ip + '\'' +
                ", appVersion='" + appVersion + '\'' +
                ", deviceToken='" + deviceToken + '\'' +
                ", isHybrid='" + isHybrid + '\'' +
                '}';
    }

    private String deviceToken;
    private String isHybrid;
}
