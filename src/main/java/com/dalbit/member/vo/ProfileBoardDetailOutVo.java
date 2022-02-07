package com.dalbit.member.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileBoardDetailOutVo {
    private Long board_idx;                  //BIGINT		-- 번호
    private Long writer_mem_no;              //BIGINT		-- 회원번호(작성자)
    private String nickName;                 //VARCHAR	--닉네임(작성자)
    private String userId;                   //VARCHAR	--아이디(작성자)
    private String memSex;                   //VARCHAR	-- 성별(작성자)
    private String profileImage;             //VARCHAR	-- 프로필(작성자)
    private Long STATUS;                     //BIGINT		-- 상태
    private Long viewOn;                     //BIGINT		-- 1:공개 0:비공개
    private String writeDate;                //DATETIME	-- 수정일자
    private String ins_date;                 //DATETIME	-- 등록일자
    private Long rcv_like_cnt;               //BIGINT		-- 좋아요수
    private Long rcv_like_cancel_cnt;        //BIGINT		-- 취소 좋아요수
    private String like_yn;                  //CHAR		-- 좋아요 확인[y,n]
}
