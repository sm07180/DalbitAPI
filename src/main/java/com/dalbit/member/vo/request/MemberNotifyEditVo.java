package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class MemberNotifyEditVo {
    @NotNull
    private int isAll;
    @NotNull
    private int isMyStar;
    @NotNull
    private int isGift;
    @NotNull
    private int isFan;
    @NotNull
    private int isComment;
    @NotNull
    private int isRadio;
    @NotNull
    private int isEvent;
}
