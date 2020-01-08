package com.demo.broadcast.vo;

import com.demo.common.vo.ProcedureVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class RoomMemberVo extends ProcedureVo {

    private String memNo;                //회원번호
    private String nickName;             //닉네임
    private String memSex;               //성별
    private String birthYear;            //생년
    private String profileImage;         //프로필이미지
    private String auth;                 //권한등급(0: 일반, 1: 게스트, 2: 메니저, 3: 방장 )
    private String controlRole;          //매니저인경우 권한구분
    private String joinDate;             //참가시간


}
