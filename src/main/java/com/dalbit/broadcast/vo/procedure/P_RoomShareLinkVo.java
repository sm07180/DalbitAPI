package com.dalbit.broadcast.vo.procedure;


import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter @Setter
public class P_RoomShareLinkVo {

    /* Input */
    private int memLogin;       //회원 로그인 상태(1: 회원, 0: 비회원)
    private String mem_no;      //요청 회원 번호
    private String linkCode;    //공유하기 주소에서 뽑아낸 방의 링크코드

    /* Output */
    private String roomNo;
    private int subject_type;
    private String title;
    private Object image_background;
    private String msg_welcom;
    private int type_entry;
    private String notice;
    private int state;
    private String code_link;
    private int count_entry;
    private int count_good;
    private Date start_date;
    private String bj_mem_no;
    private String bj_nickName;
    private String bj_memSex;
    private int bj_birthYear;
    private Object bj_profileImage;
    private String guest_mem_no;
    private String guest_nickName;
    private String guest_memSex;
    private int guest_birthYear;
    private Object guest_profileImage;

}
