package com.dalbit.event.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MoonLandMissionSelVO {
    private Integer moon_no = 0;//			INT		-- 회차번호
    private Long room_no = 0L;//			BIGINT		-- 방번호
    private Long star_mem_no = 0L;//		BIGINT		-- 별똥별완료 회원번호
    private Long lanterns_mem_no = 0L;//		BIGINT		-- 풍등완료 회원번호
    private Long rocket_mem_no = 0L;//		BIGINT		-- 로켓완료 회원번호
    private Long balloon_mem_no = 0L;//		BIGINT		-- 열기구완료 회원번호
    private String lanterns_item_yn = "n";//	CHAR(1)		-- 풍등아이템 완료여부
    private String balloon_item_yn = "n";//		CHAR(1)		-- 열기구아이템 완료여부
    private String rocket_item_yn = "n";//		CHAR(1)		-- 로켓아이템 완료여부
    private String star_item_yn = "n";//		CHAR(1)		-- 별똥별아이템 완료여부
    private String rcv_yn = "n";//			CHAR(1)		-- 전체아이템 완료여부
    private String ins_date = "";//		DATETIME	-- 등록일자
    private String upd_date = "";//		DATETIME	-- 수정일자
}
