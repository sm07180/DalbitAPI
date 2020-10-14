package com.dalbit.member.vo;

import com.dalbit.common.vo.ImageVo;
import com.dalbit.member.vo.procedure.P_FanRankVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FanRankVo {

    private int rank;
    private String memNo;
    private String nickNm;
    private String gender;
    private int age;
    private ImageVo profImg;

    public FanRankVo(P_FanRankVo target, int rank) {
        setRank(rank);
        setMemNo(target.getMem_no());
        setNickNm(target.getNickName());
        setGender(target.getMemSex());
        setAge(target.getAge());
        setProfImg(new ImageVo(target.getProfileImage(), target.getMemSex(), DalbitUtil.getProperty("server.photo.url")));
    }

}


