package com.dalbit.event.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@ToString
public class KeyboardRewardVO {

    private String theDate; // DATE	-- 조회일자

    @NotBlank
    private String theSeq; // TINYINT-- 조회회차

    @NotBlank
    private String memNo; // INT-- 회원번호

    @NotBlank
    private String preCode; // CHAR(3)-- 보상코드

    @NotBlank
    private String preSlct; // CHAR(1)-- 보상구분[r:캐럿, k:현물]

}
