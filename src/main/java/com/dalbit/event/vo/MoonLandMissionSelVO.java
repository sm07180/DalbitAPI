package com.dalbit.event.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MoonLandMissionSelVO {
    private Integer s_moonNo = 0;//			INT		-- 회차번호
    private Long s_roomNo = 0L;//			BIGINT		-- 방번호
    private Long s_starMemNo = 0L;//		BIGINT		-- 별똥별완료 회원번호
    private Long s_lanternsMemNo = 0L;//		BIGINT		-- 풍등완료 회원번호
    private Long s_rocketMemNo = 0L;//		BIGINT		-- 로켓완료 회원번호
    private Long s_balloonMemNo = 0L;//		BIGINT		-- 열기구완료 회원번호
    private String s_lanternsItemYn = "n";//	CHAR(1)		-- 풍등아이템 완료여부
    private String s_balloonItemYn = "n";//		CHAR(1)		-- 열기구아이템 완료여부
    private String s_rocketItemYn = "n";//		CHAR(1)		-- 로켓아이템 완료여부
    private String s_starItemYn = "n";//		CHAR(1)		-- 별똥별아이템 완료여부
    private String s_rcvYn = "n";//			CHAR(1)		-- 전체아이템 완료여부
}
