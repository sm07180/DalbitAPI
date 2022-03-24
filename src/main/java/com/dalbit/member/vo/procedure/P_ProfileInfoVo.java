package com.dalbit.member.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_ProfileInfoVo extends P_ApiVo {

    public P_ProfileInfoVo(){}

    //본인 프로필 조회 시 사용
    public P_ProfileInfoVo(int memLogin, String mem_no){
        setMemLogin(memLogin);
        setMem_no(mem_no);
        setTarget_mem_no(mem_no);
    }

    //상대방 프로필 조회 시 사용
    public P_ProfileInfoVo(int memLogin, String mem_no, String target_mem_no){
        setMemLogin(memLogin);
        setMem_no(mem_no);
        setTarget_mem_no(target_mem_no);
    }

    private int memLogin;                       //회원 로그인 상태(1: 회원, 0: 비회원)
    private String mem_no;                      //회원번호
    private String target_mem_no;               //대상회원번호

    private String  nickName;     				//닉네임
    private String  memSex;                     //성별
    private int     age;                        //나이대
    private String  memId;                      //자동생성된아이디8자
    private String  profileImage;               //프로필이미지
    private String  profileMsg;                 //프로필메세지
    private int     level;                      //레벨
    private int     fanCount;                   //팬으로 등록된 회원수
    private int     starCount;                  //스타로 등록된 회원수
    private int     enableFan;                  //팬등록 가눙여부(1: 가능)
    private int     exp;                        //현재 누적 경험치
    private int     expBegin;                   //시작 경험치
    private int     expNext;                    //다음 레벨업 필요 경험치
    private String  room_no;                    //진행중인 방 번호
    private String  grade;                      //등급
    private String  ruby;                       //루비 -> 달
    private String  gold;                       //골드 -> 별
    private String  fanRank1;                   //팬랭킹1위 정보
    private String  fanRank2;                   //팬랭킹2위 정보
    private String  fanRank3;                   //팬랭킹3위 정보
    private int badge_recomm;                   //추천뱃지
    private int badge_popular;                  //인기뱃지
    private int badge_newdj;                    //신입뱃지
    private long broadcastingTime;              //총 방송시간(초 단위)
    private long listeningTime;                 //총 청취시간(초 단위)
    private int receivedGoodTotal;              //총 좋아요 받은 수
    private int badge_specialdj;
    private int badge_partnerdj;
    private String fanBadgeText;
    private String fanBadgeIcon;
    private String fanBadgeStartColor;
    private String fanBadgeEndColor;
    private String cupidMemNo;
    private String cupidNickNm;
    private String cupidMemSex;
    private String cupidProfileImage;
    private int badge_new;                      // 신입청취자 뱃지
    private String birthYear;           //생 년
    private String birthMonth;          //생 월
    private String birthDay;            //생 일
    private int liveDjRank;             //실시간 DJ 순위
    private int liveFanRank;            //실시간 팬 순위\
    private int specialDjCnt;
    private int memState;
    private String listenRoomNo;
    private int listenOpen = 1;
    private int alertYn;
    private int mailboxOnOff;
    private String memJoinYn; // 21.10.25 이전 가입자 (o, n)
}
