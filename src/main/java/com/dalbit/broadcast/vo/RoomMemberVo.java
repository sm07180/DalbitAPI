package com.dalbit.broadcast.vo;

import com.dalbit.common.vo.ProcedureVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class RoomMemberVo extends ProcedureVo {

    private String mem_no;          //회원번호
    private String nickName;        //닉네임
    private String memSex;          //성별
    private int birthYear;          //생년
    private Object profileImage;    //프로필이미지
    private int auth;               //권한등급(0: 일반, 1: 게스트, 2: 메니저, 3: 방장 )
    private String controlRole;     //매니저인경우 권한구분
    private String joinDate;        //참가시간


}
