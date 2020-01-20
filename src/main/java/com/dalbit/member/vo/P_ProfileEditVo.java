package com.dalbit.member.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class P_ProfileEditVo {

    @Builder.Default private String mem_no = "11577931027280";
    @Builder.Default private String memSex = "m";                                                   //성별
    @Builder.Default private String nickName = "미스터홍";                                          //닉네임
    @Builder.Default private String name = "홍길동";                                                //이름
    @Builder.Default private int birthYear = 1990;                                                  //생년
    @Builder.Default private int birthMonth = 9;                                                    //생월
    @Builder.Default private int birthDay = 11;                                                     //생일
    @Builder.Default private String profileImage = "http://profileImage.profileImage";				//프로필이미지패스
    @Builder.Default private String backgroundImage = "http://backgroundImage.backgroundImage";		//배경이미지패스
    @Builder.Default private String profileMsg = "안녕하세요~ test 입니다.";						//메시지

}
