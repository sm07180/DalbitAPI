package com.dalbit.broadcast.vo.procedure;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class P_RoomStoryListVo {

    /* input */
    private String mem_no;                  // 회원 번호
    private String room_no;                 // 방송방 번호
    private String contents;                // 사연
    private int pageNo;                     //현재 페이지 번호
    private int pageCnt;                    //페이지당 리스트 개수

    /* output */
    private int story_idx;    				//댓글 인덱스번호
    private String writer_mem_no;           //작성자 회원번호
    private String nickName;                //작성자 닉네임
    private String memSex;                  //작성자 성별
    private Object profileImage;            //작성자 프로필이미지
    private Date writeDate;                 //작성일자
}
