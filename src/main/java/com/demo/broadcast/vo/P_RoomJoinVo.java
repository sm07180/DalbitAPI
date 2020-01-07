package com.demo.broadcast.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class P_RoomJoinVo {

    @Builder.Default private String mem_no = "11577950701317";                                       //참가하려는 회원번호
    @Builder.Default private String room_no = "91578033988651";                                      //참가하려는 방 번호
}
