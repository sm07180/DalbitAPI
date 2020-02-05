package com.dalbit.member.vo;

import com.dalbit.common.vo.ImageVo;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
public class MemberFanVo {

    private int rank;
    private String memNo;
    private String nickNm;
    private String gender;
    private int age;
    ImageVo profImg;

}
