package com.dalbit.clip.vo;

import com.dalbit.clip.vo.procedure.P_ClipRankListVo;
import com.dalbit.common.vo.FanBadgeVo;
import com.dalbit.common.vo.ImageVo;
import com.dalbit.main.vo.procedure.P_MainRankingPageVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class ClipRankListOutVo {

    private int rank;
    private String clipNo;
    private String memNo;
    private String gender;
    private String nickName;
    private String upDown;
    private String subjectType;
    private String subjectName;
    private String title;
    private ImageVo bgImg;
    private String fileName;
    private String filePlayTime;
    private String fileSize;
    private ImageVo profImg;
    private int clipPoint;
    private int giftPoint;
    private int goodPoint;
    private int listenPoint;

    public ClipRankListOutVo(P_ClipRankListVo target) {
        setRank(target.getRank());
        setClipNo(target.getCast_no());
        setMemNo(target.getMem_no());
        setGender(target.getMemSex());
        setNickName(target.getNickName());
        setUpDown(target.getUp_down());
        setSubjectType(target.getSubjectType());
        setSubjectName(target.getSubjectName());
        setTitle(target.getTitle());
        setBgImg(new ImageVo(target.getBackgroundImage(), DalbitUtil.getProperty("server.photo.url")));
        setFileName(target.getFileName());
        setFilePlayTime(target.getFilePlay());
        setFileSize(target.getFileSize());
        setProfImg(new ImageVo(target.getProfileImage(), target.getMemSex(), DalbitUtil.getProperty("server.photo.url")));
        setClipPoint(target.getClipPoint());
        setGiftPoint(target.getGiftPoint());
        setGoodPoint(target.getGoodPoint());
        setListenPoint(target.getListenPoint());
    }
}
