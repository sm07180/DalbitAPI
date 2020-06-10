package com.dalbit.common.vo.procedure;

import com.dalbit.common.vo.BaseVo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_pushInsertVo extends BaseVo {
    private String push_idx;                //푸시번호
    private String platform;        //플랫폼
    private String status;             //방송상태
    private String send_title;      //메시지 제목
    private String send_cont;       //메시지 내용
    private String etc_contents;       //팝업 메시지 내용
    private String send_url;        //이미지URL
    private String is_all;             //수신대상전체여부
    private String mem_nos;         //회원번호s
    private String msg_type;           //메시지 구분
    private String slct_push;           //푸시 구분
    private String is_direct;          //즉시발송여부
    private String send_cnt;          //발송건수 (전체 :-1, 지정: 지정건수)
    private String send_datetime;     //발송시작일시
    private String reg_date;          //등록일
    private String opName;         //등록자
    private String link_url;         //이동 링크

    private String room_no;				// DJ방생성시 방송방 번호
    private String target_mem_no;		// 타겟회원의 마이페이지 이동할 경우
    private String board_idx;			// 해당 게시판 이동할 경우
    private String target_info;
    private String image_type;
}

