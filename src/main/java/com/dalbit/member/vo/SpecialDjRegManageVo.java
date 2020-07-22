package com.dalbit.member.vo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Date;

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
    private String req_start_date;
    private String req_end_date;
    private String condition_start_date;
    private String condition_end_date;
    private String condition_title1;
    private String condition_value1;
    private int condition_code1;
    private int condition_data1;
    private String condition_title2;
    private String condition_value2;
    private int condition_code2;
    private int condition_data2;
    private String condition_title3;
    private String condition_value3;
    private int condition_code3;
    private int condition_data3;
    private String pc_image_url;
    private String mobile_image_url;
    private int is_view;
    private int platform;
    private Date reg_date;
    private String op_name;
    private Date last_upd_date;
    private String last_op_name;

    private String mem_no;
    private int condition_code;
    private int condition_data;

}
