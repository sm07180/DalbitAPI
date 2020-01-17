package com.dalbit.broadcast.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class P_RoomGuestDeleteVo {

    @Builder.Default private String mem_no = "11578531329289";                                      //요청 회원번호
    @Builder.Default private String room_no = "91578898402918";                                     //참가하려는 방 번호
    @Builder.Default private String guest_mem_no = "11577950643615";                                //게스트 지정할 회원번호
    @Builder.Default private String guest_streamid = "guest_streamid00001";                         //guest 스트림아이디
    @Builder.Default private String guest_publish_tokenid = "guest_publish_tokenid00001";           //guest 토큰아이디
    @Builder.Default private String guest_play_tokenid = "guest_play_tokenid00001";                 //guest play토큰
    @Builder.Default private String bj_streamid = "bj_streamid00001";                               //bj 스트림아이디
    @Builder.Default private String bj_publish_tokenid = "bj_publish_tokenid00001";                 //bj 토큰아이디
    @Builder.Default private String bj_play_tokenid = "bj_play_tokenid00001";                       //bj play토큰

}