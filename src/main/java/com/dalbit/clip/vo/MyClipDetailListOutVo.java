package com.dalbit.clip.vo;

import com.dalbit.clip.vo.procedure.P_MyClipDetailListVo;
import com.dalbit.common.vo.ImageVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MyClipDetailListOutVo {

    private String clipNo;
    private int openType;
    private String subjectType;
    private String subjectName;
    private ImageVo bgImg;
    private String fileName;
    private String filePlayTime;
    private String gender;
    private String nickName;
    private int countPlay;
    private int countGood;
    private int countByeol;
    private int countReply;
    private ImageVo profImg;
    private String memNo;
    private String title;

    public MyClipDetailListOutVo(P_MyClipDetailListVo target){
        setClipNo(target.getCast_no());
        setOpenType(target.getTypeOpen());
        setSubjectType(target.getSubjectType());
        setSubjectName(target.getSubjectName());
        setBgImg(new ImageVo(target.getBackgroundImage(), DalbitUtil.getProperty("server.photo.url")));
        setFileName(target.getFileName());
        setFilePlayTime(target.getFilePlay());
        setGender(target.getMemSex());
        setNickName(target.getMemNick());
        setCountPlay(target.getCountPlay());
        setCountGood(target.getCountGood());
        setCountByeol(target.getCountByeol());
        setCountReply(target.getCountReply());
        setProfImg(new ImageVo(target.getProfileImage(), getGender(), DalbitUtil.getProperty("server.photo.url")));
        setMemNo(target.getMem_no());
        setTitle(target.getTitle());
    }
}
