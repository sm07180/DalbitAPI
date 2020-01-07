package com.demo.broadcast.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class P_RoomGoodVo {

    @Builder.Default private String mem_no = "11577950643615";                                       //좋아요 누른 회원번호
    @Builder.Default private String room_no = "91578033988651";                                      //좋아요 방 번호
}
