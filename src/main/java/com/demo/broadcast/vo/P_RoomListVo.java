package com.demo.broadcast.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class P_RoomListVo {

    @Builder.Default private String mem_no = "11577950603958";                                       //방리스트 요청 회원번호
    @Builder.Default private String subjectType = "0";                                               //방주제, 리스트 우선순위 설정
    @Builder.Default private String pageNo = "1";                                                    //현재 페이지 번호
    @Builder.Default private String pageCnt = "5";                                                   //페이지당 리스트 개수

}
