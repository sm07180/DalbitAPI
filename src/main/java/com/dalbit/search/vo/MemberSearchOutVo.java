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
    private String memId;
    /*private String gender;*/
    private ImageVo profImg;
    private String roomNo;

    public MemberSearchOutVo(P_MemberSearchVo target) {
        setMemNo(target.getMem_no());
        setNickNm(target.getMem_nick());
        setMemId(target.getMem_id());
        /*setGender(target.getMem_sex());*/
        setProfImg(new ImageVo(target.getImage_profile(), target.getMem_sex(), DalbitUtil.getProperty("server.photo.url")));
        setRoomNo(target.getRoom_no());
    }

}
