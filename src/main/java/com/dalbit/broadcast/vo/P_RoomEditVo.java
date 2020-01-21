package com.dalbit.broadcast.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_RoomEditVo {

    private String mem_no;                  //방 수정하는 회원번호
    private String room_no;                 //방 번호
    private int subjectType;                //방송종류
    private String title;                   //방송제목
    private String backgroundImage;         //배경이미지 경로
    private int backgroundImageGrade;       //배경이미지 선정성
    private String welcomMsg;               //환영메시지
    private int entry;                      //입장제한
    private int age;                        //나이제한
}
