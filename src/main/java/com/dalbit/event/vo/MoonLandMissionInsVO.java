package com.dalbit.event.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MoonLandMissionInsVO {
    private Integer s_Return;		    //INT	 -3: 이미선물한 아이템, -2:방송방 미션 완료, -1:이벤트 기간아님, 0:에러, 1:정상
    private String s_rRocketItemYn;		//CHAR(1)	-- 로켓아이템 완료여부
    private String s_rBalloonItemYn;	    //CHAR(1)	-- 열기구아이템 완료여부
    private String s_rLanternsItemYn;	//CHAR(1)	-- 풍등아이템 완료여부
    private String s_rStarItemYn;		//CHAR(1)	-- 별똥별아이템 완료여부
    private String s_rcvYn;			    //CHAR(1)	-- 전체아이템 완료여부 : "y", "n"
}
