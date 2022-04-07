package com.dalbit.member.vo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter @Setter
public class MyPageFeedReplyDelVo {
    @NotNull(message = "{\"ko_KR\" : \"공지번호를\"}")
    private Integer regNo;
    private Integer tailNo;
    @NotNull(message = "{\"ko_KR\" : \"유저명을\"}")
    private String chrgrName;

    public MyPageFeedReplyDelVo(Integer regNo, Integer tailNo, String chrgrName) {
        this.regNo = regNo;
        this.tailNo = tailNo;
        this.chrgrName = chrgrName;
    }
}
