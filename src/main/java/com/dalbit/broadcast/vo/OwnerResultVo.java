package com.dalbit.broadcast.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OwnerResultVo {
    private String roomNo;           // 방번호
    private String memNo;            // 방장회원번호
    private String memNick;          // 방장닉네임
}
