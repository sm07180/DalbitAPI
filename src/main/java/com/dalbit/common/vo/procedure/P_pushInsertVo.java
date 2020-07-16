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

    /*
       push_slct

       11 : 방송 시작
       12 : DJ 레벨 업(사용안함)
       13 : 캐스트 등록, 좋아요
       14 : 방송 시작 10분 후
       15 : 회원 선물 수신
       16 : 팬 등록
       17 : 팬 보드 등록
       18 : 방송공지 등록
       19 : 생일 축하

       51 : 미접속 대상 알림
       52 : 이벤트 알림
       53 : 긴급공지
       54 : 사용자 경고
       55 : 경고 해제
       56 : 이용정지 해제
       57 : 사용자 정보 초기화
       58 : 캐스트 정보 초기화
       59 : 스페셜 DJ 선정
       60 : 1:1 문의 답변
       61 : 운영자 선물 수신
       62 : 환전 완료
       63 : 환전 불가

       92 : 운영자(회원 + 비회원)
       93 : 운영자(회원)
       94 : 운영자 (비회원)
       95 : 운영자 (테스트계정)
       96 : 운영자 (지정회원)
   */
    private String push_slct;
}

