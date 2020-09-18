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

    public ClipMainPopListOutVo(P_ClipMainPopListVo target){
        setClipNo(target.getCast_no());
        setBgImg(new ImageVo(target.getImage_background(), DalbitUtil.getProperty("server.photo.url")));
        setNickName(target.getNickName());
    }
}
