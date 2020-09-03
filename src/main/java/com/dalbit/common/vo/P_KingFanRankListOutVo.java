package com.dalbit.common.vo;

import com.dalbit.common.vo.procedure.P_KingFanRankListVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class P_KingFanRankListOutVo {

    private int rank;
    private String memNo;
    private String nickNm;
    private String gender;
    private String age;
    private ImageVo profImg;

    public P_KingFanRankListOutVo(){}
    public P_KingFanRankListOutVo(P_KingFanRankListVo target, int i){
        setRank(i);
        setMemNo(target.getMem_no());
        setNickNm(target.getNickName());
        setGender(target.getMemSex());
        setAge(target.getAge());
        setProfImg(new ImageVo(target.getProfileImage(), target.getMemSex(), DalbitUtil.getProperty("server.photo.url")));
    }

}
