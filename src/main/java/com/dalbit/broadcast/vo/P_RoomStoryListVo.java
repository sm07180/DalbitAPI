package com.dalbit.broadcast.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_RoomStoryListVo {

    /* input */
    private String roomNo;      // 방송방 번호
    private String contents;    // 사연

    /* output */
    private int story_idx;      // 댓글 인덱스 번호
    private String writer_mem_no;   //작성자 회원 번호
    private String nickName;    // 작성자 닉네임
    private String memSex;      // 작성자 성별
    private String profImg;     // 작성자 프로필 이미지

    private String writeDate;  // 작성일자 timestamp

}
