package com.dalbit.member.vo.procedure;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_MemberNotifyEditVo {

    private String mem_no;                     //ID
    private int all_ok;                        //전체알림
    private int fan_reg;                       //팬등록알림
    private int fan_board;                     //팬보드알림
    private int star_broadcast;                //스타방송알림
    private int star_notice;                   //스타공지사항알림
    private int event_notice;                  //이벤트공지알림
    private int search;                        //검색허용
}
