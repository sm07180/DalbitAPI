package com.dalbit.broadcast.vo.procedure;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_RoomStreamTokenVo {

    /* INPUT */
    private int memLogin;                       //회원 로그인 상태(1: 회원, 0: 비회원)
    private String mem_no;                      //참가하려는 회원번호
    private String room_no;                     //참가하려는 방 번호

    private String guest_publish_tokenid;       //guest 토큰아이디
    private String guest_play_tokenid;          //guest play토큰
    private String bj_publish_tokenid;          //bj 토큰아이디
    private String bj_play_tokenid;             //bj play토큰
}