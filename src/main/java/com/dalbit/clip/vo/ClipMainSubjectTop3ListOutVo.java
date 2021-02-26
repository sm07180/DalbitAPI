package com.dalbit.clip.vo;

import com.dalbit.clip.vo.procedure.P_ClipMainSubjectTop3ListVo;
import com.dalbit.common.vo.ImageVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ClipMainSubjectTop3ListOutVo {
    private int rank;
    private String clipNo;
    private ImageVo bgImg;
    private String title;
    private String nickName;
    private String subjectType;
    private String gender;              //성별
    private String filePlayTime;        //재생시간
    private Boolean isNew;              //신입뱃지(1)
    private Boolean isSpecial;          //스페셜Dj(1)
    private int badgeSpecial;
    private int replyCnt;               //클립 댓글 수
    private int goodCnt;                //좋아요 수
    private int playCnt;

    public ClipMainSubjectTop3ListOutVo(){}
    public ClipMainSubjectTop3ListOutVo(P_ClipMainSubjectTop3ListVo target){
        setRank(target.getRank());
        setClipNo(target.getCast_no());
        setBgImg(new ImageVo(target.getImage_background(), DalbitUtil.getProperty("server.photo.url")));
        setTitle(target.getTitle());
        setNickName(target.getNickName());
        setSubjectType(target.getSubject_type());
        setGender(target.getMemSex());
        setFilePlayTime(target.getFilePlayTime());
        setIsNew(target.getBadge_newdj() == 1);
        setIsSpecial(target.getBadge_special() > 0);
        setBadgeSpecial(target.getBadge_special());
        setReplyCnt(target.getReplyCnt());
        setGoodCnt(target.getCount_good());
        setPlayCnt(target.getCount_play());
    }

}
