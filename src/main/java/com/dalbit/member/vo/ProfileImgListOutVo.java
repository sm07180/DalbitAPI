package com.dalbit.member.vo;

import com.dalbit.common.vo.ImageVo;
import com.dalbit.member.vo.procedure.P_ProfileImgListVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProfileImgListOutVo {

    private ImageVo profImg;
    private Boolean isLeader;
    private int idx;

    public ProfileImgListOutVo(P_ProfileImgListVo target){
        setProfImg(new ImageVo(target.getProfileImage(), target.getMemSex(), DalbitUtil.getProperty("server.photo.url")));
        setIsLeader(target.getLeader_yn() == 1);
        setIdx(target.getIdx());
    }

}
