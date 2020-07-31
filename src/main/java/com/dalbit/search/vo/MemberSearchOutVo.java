package com.dalbit.search.vo;

import com.dalbit.common.vo.ImageVo;
import com.dalbit.search.vo.procedure.P_MemberSearchVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberSearchOutVo {

    private String memNo;
    private String nickNm;
    private String gender;
    private ImageVo profImg;
    private String roomNo;
    private boolean isNew;
    private boolean isSpecial;
    private long fanCnt;

    public MemberSearchOutVo(P_MemberSearchVo target) {
        setMemNo(target.getMem_no());
        setNickNm(target.getMem_nick());
        setProfImg(new ImageVo(target.getImage_profile(), target.getMem_sex(), DalbitUtil.getProperty("server.photo.url")));
        isNew = target.getNewdj_badge() == 1;
        isSpecial = target.getSpecialdj_badge() == 1;
        setFanCnt(target.getFan_count());
        setGender(target.getMem_sex());
    }
}
