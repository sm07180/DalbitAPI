package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;

@Getter @Setter
public class MypageBlackVo {

    @Min(1)
    private Integer page;
    @Min(1)
    private Integer records;
}
