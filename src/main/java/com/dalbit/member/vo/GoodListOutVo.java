package com.dalbit.member.vo;

import com.dalbit.common.vo.ImageVo;
import com.dalbit.member.vo.procedure.P_GoodListVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

@Getter  @Setter
public class GoodListOutVo {
    private String memNo;
    private int good;
    private String nickNm;
    private ImageVo profImg;
    private boolean isFan;

    public GoodListOutVo(P_GoodListVo target){
        setMemNo(target.getMem_no());
        setNickNm(target.getMem_nick());
        setProfImg(new ImageVo(target.getImage_profile(), target.getMem_sex(), DalbitUtil.getProperty("server.photo.url")));
        this.isFan = target.isFan();
        setGood(target.getGood());
    }
}
