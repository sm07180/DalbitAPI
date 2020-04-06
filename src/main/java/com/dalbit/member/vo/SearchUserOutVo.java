package com.dalbit.member.vo;

import com.dalbit.common.vo.ImageVo;
import com.dalbit.member.vo.procedure.P_SearchUserVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SearchUserOutVo {

    private String memNo;
    private String nickNm;
    private String memId;
    private ImageVo profImg;

    public SearchUserOutVo(){}
    public SearchUserOutVo(P_SearchUserVo target) {
        setMemNo(target.getMem_no());
        setNickNm(target.getMem_nick());
        setMemId(target.getMem_id());
        setProfImg(new ImageVo(target.getProfileImage(), target.getMemSex(), DalbitUtil.getProperty("server.photo.url")));
    }
}
