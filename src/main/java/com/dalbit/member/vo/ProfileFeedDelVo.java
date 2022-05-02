package com.dalbit.member.vo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ProfileFeedDelVo {
    @NotNull(message = "{\"ko_KR\" : \"공지번호를\"}")
    private Long noticeNo;             // 글번호
    @NotNull(message = "{\"ko_KR\" : \"유저명을\"}")
    private String delChrgrName;        // 삭제한 유저명

    public ProfileFeedDelVo(Long noticeNo, String delChrgrName) {
        this.noticeNo = noticeNo;
        this.delChrgrName = delChrgrName;
    }
}
