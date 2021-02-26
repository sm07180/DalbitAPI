package com.dalbit.clip.vo;

import com.dalbit.clip.vo.procedure.P_ClipListVo;
import com.dalbit.common.vo.ImageVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ClipListOutVo {

    private String clipNo;              //클립 번호
    private String subjectType;         //클립 주제
    private String title;               //클립 제목
    private ImageVo bgImg;              //클립 배경이미지
    private int entryType;              //청취제한
    private int playCnt;                //재생 수
    private int goodCnt;                //좋아요 수
    private int byeolCnt;               //받은별 수
    private double total;               //순위점수
    private String filePlayTime;        //재생시간
    private int os;                     //1: aos, 2: ios, 3: pc
    private Boolean isNew;              //신입뱃지(1)
    private Boolean isSpecial;          //스페셜Dj(1)
    private int badgeSpecial;
    private String memNo;               //클립 등록 회원번호
    private String nickName;            //닉네임
    private String gender;              //성별
    private int birthYear;              //생년
    private ImageVo profImg;            //프로필이미지
    private int replyCnt;               //클립 댓글 수

    public ClipListOutVo(P_ClipListVo target){
        setClipNo(target.getCast_no());
        setSubjectType(target.getSubject_type());
        setTitle(target.getTitle());
        setBgImg(new ImageVo(target.getImage_background(), DalbitUtil.getProperty("server.photo.url")));
        setEntryType(target.getType_entry());
        setPlayCnt(target.getCount_play());
        setGoodCnt(target.getCount_good());
        setByeolCnt(target.getCount_byeol());
        setTotal(target.getTotal());
        setFilePlayTime(target.getFilePlayTime());
        setOs(target.getOs_type());
        setIsNew(target.getBadge_newdj() == 1);
        setIsSpecial(target.getBadge_special() > 0);
        setBadgeSpecial(target.getBadge_special());
        setMemNo(target.getMem_no());
        setNickName(target.getNickName());
        setGender(target.getMemSex());
        setBirthYear(target.getBirthYear());
        setProfImg(new ImageVo(target.getProfileImage(), getGender(), DalbitUtil.getProperty("server.photo.url")));
        setReplyCnt(target.getReplyCnt());
    }
}
