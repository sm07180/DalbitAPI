package com.dalbit.broadcast.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_RoomGuestAddVo {

    private String mem_no;                      //요청 회원번호
    private String room_no;                     //참가하려는 방 번호
    private String guest_mem_no;                //게스트 지정할 회원번호
    private String guest_streamid;              //guest 스트림아이디
    private String guest_publish_tokenid;       //guest 토큰아이디
    private String guest_play_tokenid;          //guest play토큰
    private String bj_streamid;                 //bj 스트림아이디
    private String bj_publish_tokenid;          //bj 토큰아이디
    private String bj_play_tokenid;             //bj play토큰

}
