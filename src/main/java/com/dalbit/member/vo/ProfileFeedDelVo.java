package com.dalbit.member.vo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ProfileFeedDelVo {
    @NotNull(message = "{\"ko_KR\" : \"공지번호를\"}")
    private Long noticeIdx;             // 글번호
    @NotNull(message = "{\"ko_KR\" : \"유저명을\"}")
    private String delChrgrName;        // 삭제한 유저명

    public ProfileFeedDelVo(Long noticeIdx, String delChrgrName) {
        this.noticeIdx = noticeIdx;
        this.delChrgrName = delChrgrName;
    }
}
