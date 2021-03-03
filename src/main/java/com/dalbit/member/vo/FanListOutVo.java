package com.dalbit.member.vo;

import com.dalbit.common.vo.ImageVo;
import com.dalbit.member.vo.procedure.P_FanListVo;
import com.dalbit.member.vo.procedure.P_FanRankingVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FanListOutVo {

    private String memNo;
    private String nickNm;
    private ImageVo profImg;
    private Boolean isFan;

    public FanListOutVo(){}
    public FanListOutVo(P_FanListVo target){
        setMemNo(target.getMem_no());
        setNickNm(target.getNickName());
        setProfImg(new ImageVo(target.getProfileImage(), target.getMemSex(), DalbitUtil.getProperty("server.photo.url")));
        setIsFan(target.getEnableFan() == 0);
    }
}
