package com.dalbit.rank.vo;

import lombok.Data;

@Data
public class StarDjPointVO {
    private String mem_no;
    private String select_year;
    private String select_month;
    private Integer play_cnt;
    private Integer like_score_cnt;
    private Integer byeol_cnt;
    private Integer view_cnt;
}
