package com.dalbit.member.vo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter @Setter
public class MyPageFeedDelVo {
    @NotNull(message = "{\"ko_KR\" : \"공지번호를\"}")
    private Integer feedNo;
    @NotNull(message = "{\"ko_KR\" : \"유저명을\"}")
    private String delChrgrName;

    public MyPageFeedDelVo(Integer feedNo, String delChrgrName) {
        this.feedNo = feedNo;
        this.delChrgrName = delChrgrName;
    }
}
