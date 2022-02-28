package com.dalbit.event.vo.request;

import lombok.Data;

@Data
public class PoemEventReqVo {

    private String tailNo; // BIGINT UNSIGNED-- 댓글번호
    private String tailMemNo; // BIGINT UNSIGNED-- 댓글 등록 회원번호
    private String tailMemId; // VARCHAR(50)-- 댓글 등록 회원아이디
    private String tailMemSex; // CHAR(1)-- 댓글 등록 회원성별
    private String tailMemIp; // VARCHAR(40)-- 댓글 등록 회원아이피
    private String tailConts; // VARCHAR(300)-- 댓글 등록 내용
    private String tailLoginMedia; // CHAR(1)-- 댓글 등록 미디어
}
