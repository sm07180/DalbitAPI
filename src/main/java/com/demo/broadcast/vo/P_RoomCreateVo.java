package com.demo.broadcast.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class P_RoomCreateVo {

    @Builder.Default private String mem_no = "11577690655946";                                                  //회원번호
    @Builder.Default private int subjectType = 1;                                                               //방송종류
    @Builder.Default private String title = "테스트000001";                                                     //방송제목
    @Builder.Default private String backgroundImage = "http://backgroundImage.moonlight.co.kr/back00001.jpg";   //배경이미지 경로
    @Builder.Default private int backgroundImageGrade = 3;                                                      //배경이미지 선전성 등급
    @Builder.Default private String welcomMsg = "환영합니다~00001.";                                            //환영메시지
    @Builder.Default private String notice = "공지사항00001";                                                   //공지사항
    @Builder.Default private int entry = 0;                                                                     //입장제한
    @Builder.Default private int age = 0;                                                                       //나이제한
    @Builder.Default private int os = 1;                                                                        //OS구분
    @Builder.Default private String guest_streamid = "";                                                        //guest 스트림아이디
    @Builder.Default private String guest_publish_tokenid = "";                                                 //guest 토큰아이디
    @Builder.Default private String guest_play_tokenid = "";                                                    //guest play토큰
    @Builder.Default private String bj_streamid = "";                                                           //bj 스트림아이디
    @Builder.Default private String bj_publish_tokenid = "";                                                    //bj 토큰아이디
    @Builder.Default private String bj_play_tokenid = "";                                                       //bj play토큰

    @Builder.Default private String deviceUuid = "2200DDD1-77A";                                    //디바이스 고유아이디
    @Builder.Default private String deviceToken = "45E3156FDE20E7F11AF";                            //디바이스 토큰
    @Builder.Default private String appVersion = "1.0.0.1";                                         //앱 버전
}
