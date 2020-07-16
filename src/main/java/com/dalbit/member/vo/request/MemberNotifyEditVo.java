package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class MemberNotifyEditVo {
    @NotNull(message = "{\"ko_KR\" : \"전체 알림을\"}")
    @NotBlank(message = "{\"ko_KR\" : \"전체 알림을\"}")
    private int isAll;

    @NotNull(message = "{\"ko_KR\" : \"DJ 알림을\"}")
    @NotBlank(message = "{\"ko_KR\" : \"DJ 알림을\"}")
    private int isMyStar;

    @NotNull(message = "{\"ko_KR\" : \"선물 알림을\"}")
    @NotBlank(message = "{\"ko_KR\" : \"선물 알림을\"}")
    private int isGift;

    @NotNull(message = "{\"ko_KR\" : \"팬 알림을\"}")
    @NotBlank(message = "{\"ko_KR\" : \"팬 알림을\"}")
    private int isFan;

    @NotNull(message = "{\"ko_KR\" : \"댓글 알림을\"}")
    @NotBlank(message = "{\"ko_KR\" : \"댓글 알림을\"}")
    private int isComment;

    @NotNull(message = "{\"ko_KR\" : \"공지 알림을\"}")
    @NotBlank(message = "{\"ko_KR\" : \"공지 알림을\"}")
    private int isRadio;

    @NotNull(message = "{\"ko_KR\" : \"푸쉬 알림을\"}")
    @NotBlank(message = "{\"ko_KR\" : \"푸쉬 알림을\"}")
    private int isPush;

    @NotNull(message = "{\"ko_KR\" : \"좋아요 알림을\"}")
    @NotBlank(message = "{\"ko_KR\" : \"좋아요 알림을\"}")
    private int isLike;
}
