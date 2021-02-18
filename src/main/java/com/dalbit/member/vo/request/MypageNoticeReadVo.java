package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter @Setter
public class MypageNoticeReadVo {
    @NotNull(message = "{\"ko_KR\" : \"공지번호를\"}")
    private Integer noticeIdx;
}
