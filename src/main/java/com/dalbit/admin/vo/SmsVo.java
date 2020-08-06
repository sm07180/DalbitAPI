package com.dalbit.admin.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class SmsVo {

    public SmsVo(String msgBody, String phoneNo){
        setMsgBody(msgBody);
        setPhoneNo(phoneNo);
    }

    public SmsVo(String msgBody, String phoneNo, String vxmlFile){
        setMsgBody(msgBody);
        setPhoneNo(phoneNo);
        setVxmlFile(vxmlFile);
    }

    public SmsVo(String subject, String msgBody, String phoneNo, String vxmlFile){
        setSubject(subject);
        setMsgBody(msgBody);
        setPhoneNo(phoneNo);
        setVxmlFile(vxmlFile);
    }

    private String phoneNo;         //문자받을번호
    private String msgBody;         //메시지
    private String subject = "";    //제목
    private String vxmlFile;        //문자발송구분 (0: 회원가입, 1: 비밀번호변경, 2: 환전, 7: 운영자 발송) Admin은 비밀번호 초기화 '1' 하드코딩
    private String send_name = "";
    private String send_time;
    private String mem_no;
    private String cinfo;              // 운영자 발송 시 그룹 코드
    //0(단문)으로 하드코딩.
    private String msgType;         //데이터 타입 (SMS 0 / WAP 1 / FAX 2 / PHONE 3 / SMS_INBOUND 4 / MMS 5)

    //property에서 세팅
    private String sendPhoneNo; //보낸사람번호
    private String umId;

    //Insert 후 자동생성
    private int CMID;


}
