package com.dalbit.clip.vo;

import com.dalbit.clip.vo.procedure.P_ClipMainLatestListVo;
import com.dalbit.common.vo.ImageVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ClipMainLatestListOutVo {
    private String clipNo;              //클립 번호
    private ImageVo bgImg;              //클립 배경이미지
    private String title;
    private String nickName;
    private String subjectType;
    private String filePlayTime;
    private String gender;
    private Boolean isNew;
    private Boolean isSpecial;
    private int playCnt;
    private int goodCnt;
    private int replyCnt;

    public ClipMainLatestListOutVo(P_ClipMainLatestListVo target){
        setClipNo(target.getCast_no());
        setBgImg(new ImageVo(target.getImage_background(), DalbitUtil.getProperty("server.photo.url")));
        setTitle(target.getTitle());
        setNickName(target.getNickName());
        setSubjectType(target.getSubject_type());
        setFilePlayTime(target.getFilePlayTime());
        setGender(target.getMemSex());
        setIsNew(target.getBadge_newdj() == 1 ? true : false);
        setIsSpecial(target.getBadge_special() == 1 ? true : false);
        setPlayCnt(target.getCount_play());
        setGoodCnt(target.getCount_good());
        setReplyCnt(target.getReplyCnt());
    }
}
