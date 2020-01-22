package com.dalbit.member.vo;

import com.dalbit.common.vo.ImageVo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_MemberInfoVo {

    private String  mem_no;
    private String  target_mem_no;                 //회원정보 확인 대상
    private String  nickName;                      //닉네임
    private String  memSex;                        //성별
    private int     age;                           //나이대
    private String  memId;                         //자동생성된아이디8자
    private String  backgroundImage;               //배경이미지
    private String  profileImage;                  //프로필이미지
    private String  profileMsg;                    //프로필메세지
    private int     level;                         //레벨
    private int     fanCount;                      //팬으로 등록된 회원수
    private int     starCount;                     //스타로 등록된 회원수
    private int     exp;                           //현재경험치
    private int     expNext;                       //다음 레벨업 필요 경험치
    private String  grade;                         //등급

}
