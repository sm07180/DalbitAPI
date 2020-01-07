package com.demo.broadcast.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class P_RoomEditVo {

    @Builder.Default private String mem_no = "11577950701317";                                      //방 수정하는 회원번호
    @Builder.Default private String room_no = "91578033988651";                                     //방 번호
    @Builder.Default private String subjectType = "1";                                              //방송종류
    @Builder.Default private String title = "자유";                                                 //방송제목
    @Builder.Default private String backgroundImage = "http://backgroundImage.backgroundImage";     //배경이미지 경로
    @Builder.Default private String welcomMsg = "환영합니다";                                       //환영메시지
    @Builder.Default private String entry = "0";                                                    //입장제한
    @Builder.Default private String age = "0";                                                      //나이제한
}
