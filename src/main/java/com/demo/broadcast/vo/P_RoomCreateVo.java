package com.demo.broadcast.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class P_RoomCreateVo {

    @Builder.Default private String mem_no = "11577690655946";                                      //회원번호
    @Builder.Default private String subjectType = "1";                                              //방송종류
    @Builder.Default private String title = "자유";                                                 //방송제목
    @Builder.Default private String backgroundImage = "http://backgroundImage.backgroundImage";     //배경이미지 경로
    @Builder.Default private String welcomMsg = "환영합니다";                                       //환영메시지
    @Builder.Default private String notice = "공지사항";                                            //공지사항
    @Builder.Default private String entry = "0";                                                    //입장제한
    @Builder.Default private String age = "0";                                                      //나이제한
    @Builder.Default private String os = "1";                                                       //OS구분
    @Builder.Default private String bj_streamid = "bj_streamid00001";                               //bj스트림아이디
    @Builder.Default private String bj_publish_tokenid = "bj_publish_tokenid00001";                 //bj토큰아이디
    @Builder.Default private String bj_play_tokenid = "bj_play_tokenid00001";                       //play토큰

    @Builder.Default private String deviceUuid = "2200DDD1-77A";                                    //디바이스 고유아이디
    @Builder.Default private String deviceToken = "45E3156FDE20E7F11AF";                            //디바이스 토큰
    @Builder.Default private String appVersion = "1.0.0.1";                                         //앱 버전
}
