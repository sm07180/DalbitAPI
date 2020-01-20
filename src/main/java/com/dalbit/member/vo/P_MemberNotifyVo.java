package com.dalbit.member.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class P_MemberNotifyVo {

    @Builder.Default private String mem_no = "11577690655946";      //ID
    @Builder.Default private int all_ok = 1;                        //전체알림
    @Builder.Default private int fan_reg = 1;                       //팬등록알림
    @Builder.Default private int fan_board = 1;                     //팬보드알림
    @Builder.Default private int star_broadcast = 1;                //스타방송알림
    @Builder.Default private int star_notice = 1;                   //스타공지사항알림
    @Builder.Default private int event_notice = 1;                  //이벤트공지알림
    @Builder.Default private int search = 1;                        //검색허용
}
