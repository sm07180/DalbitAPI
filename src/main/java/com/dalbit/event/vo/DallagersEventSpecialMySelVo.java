package com.dalbit.event.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

//Return Vo
public class DallagersEventSpecialMySelVo {
    //p_evt_dalla_collect_member_special_rank_my_sel
    //p_evt_dalla_collect_member_special_rank_list
    private Long rankNo;               //BIGINT         순위
    private Integer dalla_cnt;         //INT            달라 수
    private Integer view_time;         //INT            청취시간
    private Integer play_time;         //INT            방송시간
    private String ins_date;           //DATETIME       등록일자
    private String upd_date;           //DATETIME       수정일자
    private Long mem_no;               //BIGINT         회원 번호
    private String mem_id;             //VARCHAR        회원 아이디
    private String mem_nick;           //VARCHAR        회원 닉네임
    private String mem_sex;            //CHAR           회원성별
    private String image_profile;      //VARCHAR        프로필
    private Long mem_level;            //BIGINT         레벨
    private Long mem_state;            //BIGINT         회원상태(1:정상3:블럭, 4:탈퇴, 5:영구정지...)

    private Integer seq_no;            //INT            회차 번호 (스페셜 랭킹리스트에서만 사용)
}
