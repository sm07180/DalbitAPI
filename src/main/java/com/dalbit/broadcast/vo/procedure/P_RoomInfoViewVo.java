package com.dalbit.broadcast.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter @Setter
public class P_RoomInfoViewVo extends P_ApiVo {

    /* Input */
    private int memLogin;       //회원 로그인 상태(1: 회원, 0: 비회원)
    private String mem_no;      //요청 회원 번호
    private String room_no;     //해당 방 번호

    /* Output */
    private String roomNo;                  //방번호
    private String subject_type;            //방주제
    private String title;                   //방제목
    private Object image_background;        //방배경이미지
    private String msg_welcom;              //환영메세지
    private int type_entry;                 //입장제한
    private String type_media;
    private String notice;                  //공지사항
    private int state;                      //방상태
    private String code_link;               //방공유코드
    private int count_entry;                //참여자수
    private int count_good;                 //좋아요수
    private Date start_date;                //방송시작시간
    private String bj_mem_no;               //bj 회원번호
    private String bj_userId;               //bj 유저아이디
    private String bj_nickName;             //bj 닉네임
    private String bj_memSex;               //bj 성별
    private int bj_birthYear;               //bj 생년
    private int bj_age;                     //bj 나이대
    private Object bj_profileImage;         //bj 프로필이미지
    private int bj_level;                   //bj 레벨
    private String guest_mem_no;            //게스트회원번호
    private String guest_userId;            //게스트 유저아이디
    private String guest_nickName;          //게스트닉네임
    private String guest_memSex;            //게스트성별
    private int guest_birthYear;            //게스트생년
    private int guest_age;                  //게스트 레벨
    private Object guest_profileImage;      //게스트프로필이미지
    private int guest_level;                //게스트 나이대
    private String ext;
    private int badge_recomm;               //추천뱃지
    private int badge_popular;              //인기뱃지
    private int badge_newdj;                //신입뱃지
    private int badge_special;              //스페셜뱃지
    private int freezeMsg;                  //채팅상태(0:정상,1: DJ얼리기, 2: 관리자얼리기)
    private int liveDjRank;                 //실시간 DJ 순위
    private String liveBadgeText;
    private String liveBadgeIcon;
    private String liveBadgeStartColor;
    private String liveBadgeEndColor;
    private String liveBadgeImage;
    private String liveBadgeImageSmall;
    private int os;
    private int extendCnt;                  //방송연장 횟수
    private int completeMoon;
    private int imageType;
    private int fullmoon_yn;                //보름달 사용여부

    private int mic_state;                  //마이크 상태
    private int call_state;                 //전화중 상태
    private int server_state;               //와우자 상태
    private int video_state;               //영상 상태
}
