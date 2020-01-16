package com.dalbit.broadcast.vo;

import com.dalbit.common.vo.ProcedureVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RoomVo extends ProcedureVo {

    private String roomNo;                  //방번호
    private int subject_type;               //방주제
    private String title;                   //방제목
    private Object image_background;        //방배경이미지
    private String msg_welcom;              //환영메세지
    private int restrict_entry;             //입장제한
    private int restrict_age;               //나이제한
    private String notice;                  //공지사항
    private int state;                      //방상태
    private String code_link;               //방공유코드
    private int count_entry;                //참여자수
    private int count_good;                 //좋아요수
    private String start_date;              //방송시작시간
    private String bj_mem_no;               //bj 회원번호
    private String bj_nickName;             //bj 닉네임
    private String bj_memSex;               //bj 성별
    private int bj_birthYear;               //bj 생년
    private int bj_age;                     //bj 나이대
    private Object bj_profileImage;         //bj 프로필이미지
    private String guest_mem_no;            //게스트회원번호
    private String guest_nickName;          //게스트닉네임
    private String guest_memSex;            //게스트성별
    private int guest_birthYear;            //게스트생년
    private int guest_age;                  //게스트 나이대
    private Object guest_profileImage;      //게스트프로필이미지

    private String bj_streamid;            //bj 스트림아이디
    private String bj_publish_tokenid;     //bj 토큰아이디
    private String bj_play_tokenid;        //bj play토큰
    private String guest_streamid;         //guest 스트림아이디
    private String guest_publish_tokenid;  //guest 토큰아이디
    private String guest_play_tokenid;     //guest play토큰
}
