package com.dalbit.member.vo.procedure;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class P_ProfileInfoVo {

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
    @NotNull
    @Size(min = 14, max = 14)
    private String mem_no;                      //회원번호
    @NotNull
    @Size(min = 14, max = 14)
    private String target_mem_no;               //스타회원번호

    private String  nickName;     				//닉네임
    private String  memSex;                     //성별
    private int     age;                        //나이대
    private String  memId;                      //자동생성된아이디8자
    private String  backgroundImage;            //배경이미지
    private String  profileImage;               //프로필이미지
    private String  profileMsg;                 //프로필메세지
    private int     level;                      //레벨
    private int     fanCount;                   //팬으로 등록된 회원수
    private int     starCount;                  //스타로 등록된 회원수
    private int     enableFan;                  //팬등록 가눙여부(1: 가능)
    private int     exp;                        //현재경험치
    private int     expNext;                    //다음 레벨업 필요 경험치
    private String  grade;                      //등급
    private String  ruby;                       //루비
    private String  gold;                       //골드
    private String  fanRank1;                   //팬랭킹1위 정보
    private String  fanRank2;                   //팬랭킹2위 정보
    private String  fanRank3;                   //팬랭킹3위 정보

}
