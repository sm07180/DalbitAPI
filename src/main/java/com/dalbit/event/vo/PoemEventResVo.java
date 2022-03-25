package com.dalbit.event.vo;

import com.dalbit.common.vo.ImageVo;
import lombok.Data;

@Data
public class PoemEventResVo {
    private String tail_no; // BIGINT-- 댓글번호
    private String tail_mem_no; // BIGINT-- 회원번호
    private String tail_mem_id; // VARCHAR(50)-- 회원아이디
    private String tail_mem_sex; // CHAR(1)-- 회원성별
    private String tail_mem_ip; // VARCHAR(40)-- 회원아이피
    private String tail_conts; // VARCHAR(300)-- 댓글내용
    private String login_media; // CHAR(1)-- 미디어
    private String ins_date; // DATETIME-- 등록일자
    private String mem_nick; // VARCHAR(20)-- 닉네임
    private String mem_id; // VARCHAR(50)-- 아이디
    private String mem_userid; // VARCHAR(20)-- 추가아이디
    private String image_profile; // VARCHAR(256)-- 프로필이미지정보
    private ImageVo profImg;
}
