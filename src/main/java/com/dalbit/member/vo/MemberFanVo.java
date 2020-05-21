package com.dalbit.member.vo;

import com.dalbit.common.vo.ImageVo;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Getter
@Setter
@Slf4j
public class MemberFanVo {

    private int rank;
    //private BigDecimal memNo;
    private String memNo;
    private String nickNm;
    private String gender;
    private int age;
    ImageVo profImg;

}
