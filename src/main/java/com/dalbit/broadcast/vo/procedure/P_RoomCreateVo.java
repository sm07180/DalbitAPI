package com.dalbit.broadcast.vo.procedure;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_RoomCreateVo {

    private String mem_no;                  //회원번호
    private String subjectType;             //방송종류
    private String title;                   //방송제목
    private String backgroundImage;         //배경이미지 경로
    private int backgroundImageGrade;   //배경이미지 선전성 등급
    private String welcomMsg;               //환영메시지
    private String notice;                  //공지사항
    private Integer entryType;              //입장제한(0: 전체, 1: 팬만, 2: 20세이상)
    private int os;                         //OS구분
    private String guest_streamid;          //guest 스트림아이디
    private String guest_publish_tokenid;   //guest 토큰아이디
    private String guest_play_tokenid;      //guest play토큰
    private String bj_streamid;             //bj 스트림아이디
    private String bj_publish_tokenid;      //bj 토큰아이디
    private String bj_play_tokenid;         //bj play토큰

    private String deviceUuid;              //디바이스 고유아이디
    private String deviceToken;             //디바이스 토큰
    private String appVersion;              //앱 버전

    private int isWowza;                    // 와우자 여부
}
