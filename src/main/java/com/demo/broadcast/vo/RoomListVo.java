package com.demo.broadcast.vo;

import com.demo.common.vo.BaseVo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RoomListVo extends BaseVo {

    private String room_no;                 //방번호
    private String subject_type;            //방주제
    private String title;                   //방제목
    private String image_background;        //방배경이미지
    private String msg_welcom;              //환영메세지
    private String restrict_entry;          //입장제한
    private String restrict_age;            //나이제한
    private String notice;                  //공지사항
    private String state;                   //방상태
    private String code_link;               //방공유코드
    private String count_entry;             //참여자수
    private String count_good;              //좋아요수
    private String start_date;              //방송시작시간
    private String bj_mem_no;               //bj회원번호
    private String bj_nickName;             //bj닉네임
    private String bj_memSex;               //bj성별
    private String bj_birthYear;            //bj생년
    private String bj_profileImage;         //bj프로필이미지

}
