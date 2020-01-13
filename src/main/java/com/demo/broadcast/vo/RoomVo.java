package com.demo.broadcast.vo;

import com.demo.common.vo.BaseVo;
import com.demo.common.vo.ProcedureVo;
import lombok.*;

@Getter @Setter @ToString
public class RoomVo extends ProcedureVo {

    private String roomNo;                 //방번호
    private String subjectType;            //방주제
    private String title;                  //방제목
    private String imageBackground;        //방배경이미지
    private String msgWelcom;              //환영메세지
    private String restrictEntry;          //입장제한
    private String restrictAge;            //나이제한
    private String notice;                 //공지사항
    private String state;                  //방상태
    private String codeLink;               //방공유코드
    private String countEntry;             //참여자수
    private String countGood;              //좋아요수
    private String startDate;              //방송시작시간
    private String airtime;                //방송시간
    private String bjMemNo;                //bj 회원번호
    private String bjNickName;             //bj 닉네임
    private String bjMemSex;               //bj 성별
    private String bjBirthYear;            //bj 생년
    private String bjProfileImage;         //bj 프로필이미지
    private String bj_streamid;            //bj 스트림아이디
    private String bj_publish_tokenid;     //bj 토큰아이디
    private String bj_play_tokenid;        //bj play토큰
    private String guest_streamid;         //guest 스트림아이디
    private String guest_publish_tokenid;  //guest 토큰아이디
    private String guest_play_tokenid;     //guest play토큰

}
