package com.dalbit.broadcast.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter @Setter
public class ManagerAddVo {

    @NotBlank
    private String memNo;
    @NotBlank
    private String roomNo;
    
    private String role;
}
