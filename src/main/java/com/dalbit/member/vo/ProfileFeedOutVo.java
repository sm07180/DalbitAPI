package com.dalbit.member.vo;

import com.dalbit.common.vo.ImageVo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProfileFeedOutVo {
    private Long noticeIdx;              //         BIGINT		-- 번호
    private String mem_no;                 //         BIGINT		-- 회원번호
    private String nickName;             //         VARCHAR	--닉네임
    private String memSex;               //         VARCHAR	-- 성별
    private String image_profile;        //         VARCHAR	-- 프로필
    private String title;                //         VARCHAR	-- 제목
    private String contents;             //         VARCHAR	-- 내용
    private String imagePath;            //         VARCHAR	-- 대표사진
    private Long topFix;                 //         BIGINT		-- 고정여부[0:미고정 ,1:고정]
    private String writeDate;            //         DATETIME	-- 수정일자
    private Long readCnt;                //         BIGINT		-- 읽은수
    private Long replyCnt;               //         BIGINT		-- 댓글수
    private Long rcv_like_cnt;           //         BIGINT		-- 좋아요수
    private Long rcv_like_cancel_cnt;    //         BIGINT		-- 취소 좋아요수

    private List<ProfileFeedPhotoOutVo> photoInfoList;
    private ImageVo profImg;
}
