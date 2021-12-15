package com.dalbit.event.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class GganbuRankListVo {
    private String gganbu_no; // 회차번호
    private String mem_no; // 회원번호 (신청자)
    private String mem_id; // 회원아이디 (신청자)
    private String mem_nick; // 회원대화명 (신청자)
    private String image_profile; // 회원사진(신청자)
    private int mem_level; // 회원레벨 (신청자)
    private int mem_state; // 회원상태(1:정상3:블럭, 4:탈퇴, 5:영구정지...) (신청자)
    private String ptr_mem_no; // 회원번호 (대상자)
    private String ptr_mem_id; // 회원아이디 (대상자)
    private String ptr_mem_nick; // 회원대화명 (대상자)
    private String ptr_image_profile; // 회원사진(대상자)
    private int ptr_mem_level; // 회원레벨 (대상자)
    private int ptr_mem_stat; // 회원상태(1:정상3:블럭, 4:탈퇴, 5:영구정지...) (대상자)
    private int marble_pocket_pt; // 구슬주머니 점수
    private Date ins_date; // 등록일자
    private Date upd_date; // 수정일자

    private int my_rank_no; // 나의 랭킹
    private String[] mem_level_color;
    private String[] ptr_mem_level_color;

    private int marble_pt; // 구슬점수
    private int tot_marble_pocket_pt; // 합산 점수
}
