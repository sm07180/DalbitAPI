package com.demo.broadcast.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class P_RoomMemberListVo {

    @Builder.Default private String mem_no = "11577950701317";                                       //방참여자 리스트 요청 회원번호
    @Builder.Default private String room_no = "91578033988651";                                      //방번호
    @Builder.Default private String pageNo = "1";                                                    //현재 페이지 번호
    @Builder.Default private String pageCnt = "10";                                                  //페이지당 리스트 개수
}
