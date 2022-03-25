package com.dalbit.member.vo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ProfileBoardDetailSelVo {
    @NotNull
    private String memNo;   //상대방 memNo (프로필주인)
    @NotNull
    private Integer fanBoardNo; //팬보드 글번호
    
}
