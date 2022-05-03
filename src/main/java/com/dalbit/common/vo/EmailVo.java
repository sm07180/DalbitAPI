package com.dalbit.common.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.UnsupportedEncodingException;

@Getter
@Setter
@ToString
public class EmailVo {

    public EmailVo(String title, String rcvMail, String msgCont){
        this.title = title;
        this.rcvMemId = rcvMail;
        this.rcvMail = rcvMail;
        this.msgCont = msgCont;
    }

    private String title;       //제목
    private String rcvMemId;    //수신자 아이디
    private String rcvMail;     //수신자 메일주소
    private String sendMail = "help@dallalive.com";    //발신자
    private String msgCont;     //내용
    private String sendType = "1";    //발송구분 [코드표참고]
}
