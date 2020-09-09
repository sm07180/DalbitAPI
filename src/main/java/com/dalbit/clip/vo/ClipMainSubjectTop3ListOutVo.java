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

    public ClipMainSubjectTop3ListOutVo(){}
    public ClipMainSubjectTop3ListOutVo(P_ClipMainSubjectTop3ListVo target){
        setRank(target.getRank());
        setClipNo(target.getCast_no());
        setBgImg(new ImageVo(target.getImage_background(), DalbitUtil.getProperty("server.photo.url")));
        setTitle(target.getTitle());
        setNickName(target.getNickName());
        setSubjectType(target.getSubject_type());
    }

}
