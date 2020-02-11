package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class MemberNotifyEditVo {
    @NotBlank
    private boolean isAll;
    @NotBlank
    private boolean isFanReg;
    @NotBlank
    private boolean isBoard;
    @NotBlank
    private boolean isStarCast;
    @NotBlank
    private boolean isStarNoti;
    @NotBlank
    private boolean isEvtNoti;
    @NotBlank
    private boolean isSearch;
}
