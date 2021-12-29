package com.dalbit.event.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MoonLandMyRankVO {
   private Integer moon_no = 1;				    //INT		-- 회차번호
   private Long mem_no = 0L;				        //BIGINT		-- 회원번호
   private String mem_id = "";				    //VARCHAR(20)	-- 아이디
   private String mem_nick = "";			        //VARCHAR(20)	-- 대화명
   private String image_profile = "";			//VARCHAR(256)	-- 대표이미지
   private Integer mem_level = 0;			    //INT		-- 회원레벨
   private Integer mem_state = 0;			    //INT		-- 회원상태
   private Integer mem_basic_item_score = 0;	//INT		-- 아이템선물 점수(일반)
   private Integer mem_gold_item_score = 0;		//INT		-- 아이템선물 점수(보너스)
   private Integer mem_gold_like_score = 0;		//INT		-- 좋아요 점수(보너스)
   private Integer mem_gold_booster_score = 0;	//INT		-- 부스터 점수(보너스)
   private Integer mem_gold_mission_score = 0;	//INT		-- 아이템 미션 점수(보너스)
   private Integer mem_cha_booster_score = 0;	//INT		-- 부스터 점수(캐릭터)
   private Integer mem_cha_mission_score = 0;	//INT		-- 아이템 미션 점수(캐릭터)
   private Integer rank_pt = 0;				    //INT		-- 점수합
   private Integer rank_step = 0;			    //INT		-- 랭킹 단계
   private Integer send_like_cnt = 0;			//INT		-- 좋아요 합계
   private Integer view_cnt = 0;			    //INT		-- 청취시간 합계
   private Integer my_rank_no = 0;			    //INT		-- 나의 랭킹순위

   private Integer tot_score =0;
   private Integer next_reward =0;
}
