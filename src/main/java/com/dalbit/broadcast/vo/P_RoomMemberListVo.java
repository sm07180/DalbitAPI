package com.dalbit.broadcast.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class P_RoomMemberListVo {

    /* Input */
    private String mem_no;      //방참여자 리스트 요청 회원번호
    private String room_no;     //방번호
    private int pageNo;         //현재 페이지 번호
    private int pageCnt;        //페이지당 리스트 개수

    /* Output */
    private String nickName;
    private String memSex;
    private int birthYear;
    private Object profileImage;
    private int auth;
    private String controlRole;
    private Date join_date;
}
