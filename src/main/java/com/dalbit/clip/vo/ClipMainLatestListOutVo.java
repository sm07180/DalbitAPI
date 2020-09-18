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

    public ClipMainLatestListOutVo(P_ClipMainLatestListVo target){
        setClipNo(target.getCast_no());
        setBgImg(new ImageVo(target.getImage_background(), DalbitUtil.getProperty("server.photo.url")));
        setTitle(target.getTitle());
        setNickName(target.getNickName());
    }
}
