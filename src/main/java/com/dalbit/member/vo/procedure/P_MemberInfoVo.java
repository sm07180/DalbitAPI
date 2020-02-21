package com.dalbit.member.vo.procedure;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_MemberInfoVo {

    private String mem_no;
    private String nickName;            //닉네임
    private String memSex;              //성별
    private int age;                    //나이대
    private String birthYear;           //생 년
    private String birthMonth;          //생 월
    private String birthDay;            //생 일
    private String memId;               //자동생성된아이디8자
    private String profileImage;        //프로필이미지
    private String profileMsg;          //프로필메세지
    private String ruby;               //보유 루비수(본인)
    private String gold;               //보유 골드수(본인)

}
