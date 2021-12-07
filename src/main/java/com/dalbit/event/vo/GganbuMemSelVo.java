package com.dalbit.event.vo;

import com.dalbit.common.vo.ImageVo;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class GganbuMemSelVo {
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
    private int red_marble; // 보유중인 빨간구슬 개수
    private int yellow_marble; // 보유중인 노랑구슬 개수
    private int blue_marble; // 보유중인 파랑구슬 개수
    private int violet_marble; // 보유중인 보라구슬 개수
    private int tot_red_marble; // 총누적된 빨간구슬 개수
    private int tot_yellow_marble; // 총누적된 노랑구슬 개수
    private int tot_blue_marble; // 총누적된 파랑구슬 개수
    private int tot_violet_marble; // 총누적된 보라구슬 개수
    private int marble_pocket; // 보유중인 구슬주머니
    private long marble_pocket_pt; // 획득한 구슬주머니 점수
    private Date ins_date; // 등록일자
    private Date upd_date; // 수정일자
    private int average_level; // 깐부 평균 레벨 (소수점 버림)

    private String[] mem_level_color;
    private String[] ptr_mem_level_color;
    private ImageVo mem_profile;
    private ImageVo ptr_mem_profile;
    private String mem_sex;
    private String ptr_mem_sex;
}
