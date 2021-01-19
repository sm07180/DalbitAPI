package com.dalbit.member.vo;

import com.dalbit.common.vo.ImageVo;
import com.dalbit.member.vo.procedure.P_AwardHonorListVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AwardHonorOutVo {

    private String memNo;
    private String nickNm;
    private ImageVo profImg;
    private int listenTime;
    private int goodPoint;
    private String fanTitle;
    private String joinDt;
    private long joinTs;

    public AwardHonorOutVo(){}
    public AwardHonorOutVo(P_AwardHonorListVo target){
        setMemNo(target.getMem_no());
        setNickNm(target.getMem_nick());
        setProfImg(new ImageVo(target.getProfileImage(), target.getMemSex(), DalbitUtil.getProperty("server.photo.url")));

        setListenTime(DalbitUtil.isEmpty(target.getListenPoint()) ? 0 : target.getListenPoint() / 60);
        setGoodPoint(DalbitUtil.isEmpty(target.getGoodPoint()) ? 0 : target.getGoodPoint());
        setFanTitle(DalbitUtil.isEmpty(target.getFanTitle()) ? "" : target.getFanTitle());
        setJoinDt(DalbitUtil.isEmpty(target.getMemJoinDate()) ? "" : DalbitUtil.getUTCFormat(target.getMemJoinDate()));
        setJoinTs(DalbitUtil.isEmpty(target.getMemJoinDate()) ? 0 :DalbitUtil.getUTCTimeStamp(target.getMemJoinDate()));
    }


}
