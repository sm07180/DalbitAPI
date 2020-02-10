package com.dalbit.sample.vo;

import com.dalbit.common.vo.BaseVo;
import com.dalbit.validator.annotation.Sample;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SampleVo extends BaseVo {
    private String id;
    @Sample
    private String name;
    private int age;

    private String idx;
    private String sp_name;
    private String data;
    private String upd_date;

    private String mem_id;
    private String mem_nick;
    private String id_info;
    private int ruby;

}
