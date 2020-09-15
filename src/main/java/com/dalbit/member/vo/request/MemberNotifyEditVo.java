package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class MemberNotifyEditVo {
    /*@NotNull(message = "{\"ko_KR\" : \"전체 알림을\"}")*/
    private int isAll;

    /*@NotNull(message = "{\"ko_KR\" : \"DJ 알림을\"}")*/
    private int isMyStar;

    /*@NotNull(message = "{\"ko_KR\" : \"선물 알림을\"}")*/
    private int isGift;

    /*@NotNull(message = "{\"ko_KR\" : \"팬 알림을\"}")*/
    private int isFan;

    /*@NotNull(message = "{\"ko_KR\" : \"팬보드 알림을\"}")*/
    private int isComment;

    /*@NotNull(message = "{\"ko_KR\" : \"공지 알림을\"}")*/
    private int isRadio;

    /*@NotNull(message = "{\"ko_KR\" : \"푸쉬 알림을\"}")*/
    private int isPush;

    /*@NotNull(message = "{\"ko_KR\" : \"좋아요 알림을\"}")*/
    private int isLike;

    /*@NotNull(message = "{\"ko_KR\" : \"팬보드 댓글 알림을\"}")*/
    private int isReply;

    /*@NotNull(message = "{\"ko_KR\" : \"마이스타 클립 등록 알림을\"}")*/
    private int isStarClip;

    /*@NotNull(message = "{\"ko_KR\" : \"내클립 알림을\"}")*/
    private int isMyClip;

    @NotBlank(message = "{\"ko_KR\" : \"알림음을\"}")
    @NotNull(message = "{\"ko_KR\" : \"알림음을\"}")
    private String alimType;    //알림음구분(n:무음,s:소리,v:진동)

}
