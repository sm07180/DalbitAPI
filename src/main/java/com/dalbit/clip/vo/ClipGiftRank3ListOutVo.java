package com.dalbit.clip.vo;

import com.dalbit.clip.vo.procedure.P_ClipGiftRankTop3Vo;
import com.dalbit.common.vo.ImageVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ClipGiftRank3ListOutVo {

    private String memNo;
    private String nickName;
    private String gender;
    private int age;
    private ImageVo profImg;

    public ClipGiftRank3ListOutVo(P_ClipGiftRankTop3Vo target){
        setMemNo(target.getMem_no());
        setNickName(target.getNickName());
        setGender(target.getMemSex());
        setAge(target.getAge());
        setProfImg(new ImageVo(target.getProfileImage(), getGender(), DalbitUtil.getProperty("server.photo.url")));
    }
}
