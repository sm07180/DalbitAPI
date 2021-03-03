package com.dalbit.member.vo;

import com.dalbit.common.vo.ImageVo;
import com.dalbit.member.vo.procedure.P_DjRecommendListVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class DjRecommendListOutVo {
    private String memNo;
    private String nickNm;
    private String gender;
    private ImageVo profImg;
    private String roomNo;
    private int ageType;
    private String ageDesc;
    private String title;
    private String desc;
    private Boolean isFan;

    public DjRecommendListOutVo(P_DjRecommendListVo target){
        setMemNo(target.getMem_no());
        setNickNm(target.getMem_nick());
        setGender(target.getMem_sex());

        if(DalbitUtil.isEmpty(target.getProfileImage()) || !(target.getProfileImage().startsWith("http://") || target.getProfileImage().startsWith("https://"))){
            setProfImg(new ImageVo(target.getProfileImage(), target.getMem_sex(), DalbitUtil.getProperty("server.photo.url")));
        }else{
            setProfImg(new ImageVo(target.getProfileImage()));
        }

        setRoomNo(DalbitUtil.isEmpty(target.getRoomNo()) ? "" : target.getRoomNo());
        setAgeType(target.getAge_type());
        setAgeDesc(target.getAge_desc());
        setTitle(target.getTitle());
        setDesc(target.getDesc());
        setIsFan(target.getEnableFan() == 0);
    }

}
