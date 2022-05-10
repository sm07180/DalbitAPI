package com.dalbit.broadcast.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoryResultVo {
    private Long idx;                   //BIGINT    -- 자동등록 번호
    private Long dj_mem_no;             //BIGINT    -- 회원 번호(방장)
    private Long room_no;               //BIGINT    -- 방 번호
    private String contents;            //VARCHAR   -- 내용
    private String plus_yn;             //CHAR      -- 플러스[y,n]
    private Long status;                //BIGINT    -- 상태 0 정상,1삭제
    private Long writer_no;             //BIGINT    -- 회원 번호(보낸이)
    private String writer_mem_id;       //VARCHAR   -- 회원 아이디(보낸이)
    private String writer_mem_nick;     //VARCHAR   -- 회원 닉네임(보낸이)
    private String writer_mem_sex;      //CHAR      -- 회원성별(보낸이)
    private String writer_mem_profile;  //VARCHAR   -- 프로필(보낸이)
    private String write_date;          //DATETIME  -- 등록일자
}
