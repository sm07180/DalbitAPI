package com.dalbit.member.vo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.util.List;

@Getter @Setter
public class SpecialDjRegManageVo {

    private int idx;
    private String title;
    @NotBlank(message = "{\"ko_KR\" : \"년\"}")
    @NotNull(message = "{\"ko_KR\" : \"년\"}")
    private String select_year;
    @NotBlank(message = "{\"ko_KR\" : \"월\"}")
    @NotNull(message = "{\"ko_KR\" : \"월\"}")
    private String select_month;
    private int notice_idx;
    private String req_start_date;
    private String req_end_date;
    private String condition_start_date;
    private String condition_end_date;
    private int condition_code1;
    private int condition_data1;
    private int condition_code2;
    private int condition_data2;
    private int condition_code3;
    private int condition_data3;
    private int condition_code4;
    private int condition_data4;
    private int best_code1;
    private int best_data1;
    private int best_code2;
    private int best_data2;
    private int best_code3;
    private int best_data3;
    private int is_view;
    private int platform;

    private List<SpecialDjContentVo> contentList;
}
