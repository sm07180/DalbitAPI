package com.dalbit.broadcast.vo.procedure;

import com.dalbit.broadcast.vo.request.RoomListVo;
import com.dalbit.common.vo.DeviceVo;
import com.dalbit.common.vo.ImageVo;
import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Getter
@Setter
public class P_RoomListVo extends P_ApiVo {

    public P_RoomListVo(){
        this.photoSvrUrl = DalbitUtil.getProperty("server.photo.url");
    }
    public P_RoomListVo(RoomListVo roomListVo, HttpServletRequest request){
        this.photoSvrUrl = DalbitUtil.getProperty("server.photo.url");
        int pageNo = DalbitUtil.isEmpty(roomListVo.getPage()) ? 1 : roomListVo.getPage();
        int pageCnt = DalbitUtil.isEmpty(roomListVo.getRecords()) ? 10 : roomListVo.getRecords();

        DeviceVo deviceVo = new DeviceVo(request);
        setMemLogin(DalbitUtil.isLogin(request) ? 1 : 0);
        setMem_no(MemberVo.getMyMemNo(request));
        setSubjectType(roomListVo.getRoomType());
        setSlctType(roomListVo.getSearchType());
        setSearch(roomListVo.getSearch());
        setDjType(DalbitUtil.isEmpty(roomListVo.getDjType()) ? 0 : roomListVo.getDjType());
        if(!DalbitUtil.isEmpty(roomListVo.getGender())){
            if(roomListVo.getGender().equals("d")){
                setDjType(1);
            } else {
                setGender(roomListVo.getGender());
            }
        }
        setPageNo(pageNo);
        setPageCnt(pageCnt);
        setIsWowza(DalbitUtil.isWowza(deviceVo));
        setMediaType(roomListVo.getMediaType());
    }

    private String photoSvrUrl;
    /* Input */
    private int memLogin;                   //회원 로그인 상태(1: 회원, 0: 비회원)
    private String mem_no;                  //방리스트 요청 회원번호
    private Integer slctType;               //검색구분 -1 or 0: null, 1: 추천(=청취자), 2:인기, 3:신입
    private String subjectType;             //방주제, 리스트 우선순위 설정
    private String search;                  //검색어
    private String gender;                  //DJ성별
    private int djType;                     //신입구분
    private int pageNo;                     //현재 페이지 번호
    private int pageCnt;                    //페이지당 리스트 개수
    private int isWowza;                   // 와우자 여
    private String mediaType;

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
    private int count_fan;                  //신규팬수
    private int count_entry;                //참여자수
    private int count_good;                 //좋아요수
    private int count_gold;                 //선물받은 별수
    private int count_boost;                 //선물받은 별수
    private int rank;                 //선물받은 별수
    private Date start_date;                //방송시작시간
    private String bj_mem_no;               //bj 회원번호
    private String bj_nickName;             //bj 닉네임
    private String bj_memSex;               //bj 성별
    private int bj_birthYear;               //bj 생년
    private int bj_age;                     //bj 나이대
    private String bj_profileImage;         //bj 프로필이미지
    private ImageVo bj_profileImageVo;         //bj 프로필이미지
    private String guest_mem_no;            //게스트회원번호
    private String guest_nickName;          //게스트닉네임
    private String guest_memSex;            //게스트성별
    private int guest_birthYear;            //게스트생년
    private int guest_age;                  //게스트 나이대
    private Object guest_profileImage;      //게스트프로필이미지부

    private String bj_streamid;            //bj 스트림아이디
    private String bj_publish_tokenid;     //bj 토큰아이디부
    private String bj_play_tokenid;        //bj play토큰
    private String guest_streamid;         //guest 스트림아이디
    private String guest_publish_tokenid;  //guest 토큰아이디
    private String guest_play_tokenid;     //guest play토큰
    private int is_wowza;                  // 와우자여

    private int badge_partner;
    private int badge_recomm;                   //추천배지
    private int badge_popular;                  //인기배지
    private int badge_newdj;                    //신입배지
    private int badge_special;                  //스페셜배지
    private int os_type;
    private int totalCnt;
    private int isConDj;                    //컨텐츠배지
    private int isContents;                 //컨텐츠 배지(new)

    private int liveDjRank;                 //실시간 DJ 순위
    private String liveBadgeText;
    private String liveBadgeIcon;
    private String liveBadgeStartColor;
    private String liveBadgeEndColor;
    private String liveBadgeImage;
    private String liveBadgeImageSmall;

    private String fanBadgeText;
    private String fanBadgeIcon;
    private String fanBadgeStartColor;
    private String fanBadgeEndColor;
    private String fanBadgeImage;
    private String fanBadgeImageSmall;

    private int type_image;                 //스페셜DJ일 경우 실시간live 이미지 노출선택(1:프로필, 2:배경)
    private int goodMem;                    //좋아요 배지 회원 여부 ( 1이상: 있음, 0:없음)
    private int goodMem2;                    //좋아요 배지 회원 여부 ( 1이상: 있음, 0:없음)
    private int goodMem3;                    //좋아요 배지 회원 여부 ( 1이상: 있음, 0:없음)
    private boolean isShining = false;

    public void setBj_profileImage(String bj_profileImage) {
        if(!StringUtils.isEmpty(this.bj_memSex)) {
            this.bj_profileImageVo = new ImageVo
                (bj_profileImage, this.bj_memSex, this.photoSvrUrl);
        }
        this.bj_profileImage = bj_profileImage;
    }
    public void setBj_memSex(String bj_memSex) {
        if(!StringUtils.isEmpty(this.bj_profileImage)) {
            this.bj_profileImageVo = new ImageVo
                (this.bj_profileImage, bj_memSex, this.photoSvrUrl);
        }
        this.bj_memSex = bj_memSex;
    }
}
