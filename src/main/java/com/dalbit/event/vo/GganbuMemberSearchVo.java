package com.dalbit.event.vo;

import com.dalbit.common.vo.ImageVo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GganbuMemberSearchVo {
    private String mem_no; // 회원번호
    private String mem_id; // 회원아이디
    private String mem_nick; // 회원대화명
    private String image_profile; // 회원사진
    private String rcvYn; // 내가 신청여부
    private String sendYn; // 검색된 회원이 신청여부
    private String mem_sex;
    private ImageVo mem_profile;
    private int average_level;
    private int mem_level;
}
