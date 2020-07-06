package com.dalbit.common.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter @Setter
public class PushClickVo {

    @NotBlank
    private String pushIdx;
    private String mem_no;

}
