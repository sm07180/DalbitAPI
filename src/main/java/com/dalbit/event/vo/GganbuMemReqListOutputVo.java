package com.dalbit.event.vo;

import com.dalbit.common.vo.ImageVo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GganbuMemReqListOutputVo {
    private String gganbu_no; // 회차번호
    private String mem_no; // 회원번호 (신청자)
    private String mem_id; // 회원아이디 (신청자)
    private String mem_nick; // 회원대화명 (신청자)
    private String image_profile; // 회원사진(신청자)
    private int mem_level; // 회원레벨 (신청자)
    private String mem_state; // 회원상태(1:정상3:블럭, 4:탈퇴, 5:영구정지...) (신청자)
    private String ptr_mem_no; // 회원번호 (대상자)
    private String ptr_mem_id; // 회원아이디 (대상자)
    private String ptr_mem_nick; // 회원대화명 (대상자)
    private String ptr_image_profile; // 회원사진(대상자)
    private int ptr_mem_level; // 회원레벨 (대상자)
    private String ptr_mem_stat; // 회원상태(1:정상3:블럭, 4:탈퇴, 5:영구정지...) (대상자)
    private String req_date; // 신청일자
    private int average_level; // 평균 레벨

    private String mem_sex;
    private String ptr_mem_sex;
    private ImageVo mem_profile;
    private ImageVo ptr_mem_profile;
}
