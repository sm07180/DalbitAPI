package com.dalbit.broadcast.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class P_RoomExitVo {

    @Builder.Default private String mem_no = "11577950701317";                                       //나가려는 회원번호
    @Builder.Default private String room_no = "91578033988651";                                      //나가려는 방 번호
}
