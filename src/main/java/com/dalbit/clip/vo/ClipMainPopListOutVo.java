package com.dalbit.clip.vo;

import com.dalbit.clip.vo.procedure.P_ClipMainPopListVo;
import com.dalbit.common.vo.ImageVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ClipMainPopListOutVo {

    private String clipNo;              //클립 번호
    private ImageVo bgImg;              //클립 배경이미지
    private String nickName;
    private String subjectType;
    private String title;
    private String filePlayTime;        //재생시간
    private Boolean isNew;              //신입뱃지(1)
    private Boolean isSpecial;          //스페셜Dj(1)
    private int badgeSpecial;
    private String gender;              //성별
    private int replyCnt;               //클립 댓글 수
    private int goodCnt;                //좋아요 수
    private int playCnt;

    public ClipMainPopListOutVo(P_ClipMainPopListVo target){
        setClipNo(target.getCast_no());
        setBgImg(new ImageVo(target.getImage_background(), DalbitUtil.getProperty("server.photo.url")));
        setNickName(target.getNickName());
        setSubjectType(target.getSubject_type());
        setTitle(target.getTitle());
        setFilePlayTime(target.getFilePlayTime());
        setIsNew(target.getBadge_newdj() == 1);
        setIsSpecial(target.getBadge_special() > 0);
        setBadgeSpecial(target.getBadge_special());
        setGender(target.getMemSex());
        setReplyCnt(target.getReplyCnt());
        setGoodCnt(target.getCount_good());
        setPlayCnt(target.getCount_play());
    }
}
