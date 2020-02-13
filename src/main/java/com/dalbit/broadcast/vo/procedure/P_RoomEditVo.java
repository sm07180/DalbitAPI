package com.dalbit.broadcast.vo.procedure;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_RoomEditVo {

    private String mem_no;                  //방 수정하는 회원번호
    private String room_no;                 //방 번호
    private String subjectType;             //방송종류
    private String title;                   //방송제목
    private String backgroundImage;         //배경이미지 경로
    private String backgroundImageDelete;   //삭제할 배경이미지 경로
    private int backgroundImageGrade;       //배경이미지 선정성
    private String welcomMsg;               //환영메시지
    private int os;                         //OS구분
    private String deviceUuid;              //디바이스 고유아이디
    private String deviceToken;             //디바이스 토큰
    private String appVersion;              //앱 버전

}
