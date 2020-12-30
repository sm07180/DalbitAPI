package com.dalbit.clip.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_ClipRecommendLeaderListVo extends P_ApiVo {

    public P_ClipRecommendLeaderListVo() {
    }

    private String rec_date;             // 날짜
    private String title_msg;           // 소개제목
    private String thumb_url;           // 썸네일 url
    private String title;               // 클립 제목
    private String mem_no;              // 회원번호
    private String mem_nick;            // 회원닉네임
    private String cast_no;             // 클립 번호

}
