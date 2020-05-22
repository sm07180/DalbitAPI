package com.dalbit.member.vo.procedure;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter @Setter
public class P_SpecialDjReq {

    private String mem_no;
    @NotBlank @Size(min = 1, max = 20)
    private String name;
    @NotBlank @Size(min = 1, max = 20)
    private String phone;
    @Size(max = 200)
    private String title;
    @Size(max = 1024)
    private String contents;
}
