package com.dalbit.member.vo;

import com.dalbit.common.vo.DeviceVo;
import com.dalbit.common.vo.ImageVo;
import com.dalbit.member.vo.procedure.P_StoryVo;
import com.dalbit.util.DalbitUtil;

public class StoryOutVo {
    private String roomNo;
    private String title;
    private ImageVo bgImg;
    private String startDt;
    private long startTs;
    private long storyCnt;

    public StoryOutVo(){}
    public StoryOutVo(P_StoryVo pStoryVo, String photoSvr, DeviceVo deviceVo){
        this.roomNo = pStoryVo.getRoom_no();
        this.title = pStoryVo.getTitle();
        if(deviceVo != null && pStoryVo.getImage_background().toLowerCase().endsWith(".gif")){
            if(deviceVo.getOs() == 2 && DalbitUtil.versionCompare("14.0", deviceVo.getSdkVersion())){
                this.bgImg = new ImageVo(pStoryVo.getImage_background(), DalbitUtil.getProperty("server.photo.url"));
            }else{ //webp사용
                this.bgImg = new ImageVo(pStoryVo.getImage_background() + ".webp", DalbitUtil.getProperty("server.photo.url"));
            }
        }else{
            this.bgImg = new ImageVo(pStoryVo.getImage_background(), photoSvr);
        }
        this.startDt = DalbitUtil.getUTCFormat(pStoryVo.getStart_date());
        this.startTs = DalbitUtil.getUTCTimeStamp(pStoryVo.getStart_date());
        this.storyCnt = pStoryVo.getStoryCnt();
    }
}
