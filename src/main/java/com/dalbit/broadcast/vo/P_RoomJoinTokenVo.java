package com.dalbit.broadcast.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class P_RoomJoinTokenVo {

    @Builder.Default private String room_no = "91578888356181";                                     //참가하려는 방 번호

}
