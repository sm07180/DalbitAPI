package com.demo.broadcast.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class P_RoomJoinVo {

    @Builder.Default private String mem_no = "11578531441954";                          //참가하려는 회원번호
    @Builder.Default private String room_no = "91578033988651";                                     //참가하려는 방 번호
    @Builder.Default private String guest_streamid = "guest_streamid00001";                         //bj스트림아이디
    @Builder.Default private String guest_publish_tokenid = "guest_publish_tokenid00001";           //bj토큰아이디
    @Builder.Default private String guest_play_tokenid = "guest_play_tokenid00001";                 //play토큰
    @Builder.Default private String bj_streamid = "bj_streamid00001";                               //bj스트림아이디
    @Builder.Default private String bj_publish_tokenid = "bj_publish_tokenid00001";                 //bj토큰아이디
    @Builder.Default private String bj_play_tokenid = "bj_play_tokenid00001";                       //play토큰

}
