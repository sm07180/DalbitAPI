package com.dalbit.common.vo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter @Setter
public class SelfAuthOutVo {

    private String tr_cert;         //요청정보(암호화)
    private String tr_url;          //결과수신URL
    private String tr_add = "N";    //IFrame사용여부
}
