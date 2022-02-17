package com.dalbit.event.vo;

import lombok.Data;

@Data
public class InviteVo {

   private String mem_no;   //BIGINT     -- 회원 번호
   private String invitation_code;   //VARCHAR    -- 추천코드
   private Integer invitation_cnt;   //BIGINT     -- 초대회원수
   private Integer invitation_dal_cnt;   //BIGINT     -- 초대 지급달
   private String ins_date;   //DATETIME   -- 등록일자
   private String upd_date;   //DATETIME   -- 수정일자
   private String mem_id;   //VARCHAR    -- 회원 아이디
   private String mem_nick;   //VARCHAR    -- 회원 닉네임
   private String mem_sex;   //CHAR       -- 회원성별
   private String image_profile;   //VARCHAR    -- 프로필
   private Integer mem_level;   //BIGINT     -- 레벨
   private Integer mem_state;   //BIGINT     -- 회원상태(1:정상3:블럭, 4:탈퇴, 5:영구정지...)
   private Integer rankNo;   //BIGINT     -- 순위

   private String send_mem_no         ;   //BIGINT       -- 회원 번호(보낸)
   private String send_mem_id         ;   //RCHAR      -- 회원 아이디(보낸)
   private String send_mem_sex        ;   //AR         -- 회원성별(보낸)
   private String send_mem_age        ;   //GINT       -- 나이(보낸)
   private String send_mem_dal_cnt    ;   //GINT       -- 지급달(보낸)
   private String rcv_mem_no          ;   //GINT       -- 회원 번호(받은)
   private String rcv_mem_id          ;   //RCHAR      -- 회원 아이디(받은)
   private String rcv_mem_sex         ;   //AR         -- 회원성별(받은)
   private String rcv_mem_age         ;   //GINT       -- 나이(받은)
   private String rcv_mem_dal_cnt     ;   //GINT       -- 지급달(받은)
   private String rcv_mem_nick        ;   //RCHAR      -- 회원 닉네임(받은)
   private String rcv_image_profile   ;   //RCHAR      -- 프로필(받은)
   private String rcv_mem_level       ;   //GINT       -- 레벨(받은)
   private String rcv_mem_state       ;   //GINT       -- 회원상태(1:정상3:블럭, 4:탈퇴, 5:영구정지...)  (받은)
   private String rcv_mem_join_date   ;   //TETIME     -- 가입일자(받은)

}
