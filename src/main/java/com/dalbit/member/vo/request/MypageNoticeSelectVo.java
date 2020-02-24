package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter @Setter
public class MypageNoticeSelectVo {

    @NotBlank
    private String memNo;

    @Min(1)
    private Integer page;
    @Min(1)
    private Integer records;

}
