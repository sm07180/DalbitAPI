package com.dalbit.broadcast.vo;

import com.dalbit.common.vo.ImageVo;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GuestInfoVo {

    private int mode;
    private String memNo;
    private ImageVo profImg;
    private String nickNm;
    private int proposeCnt=0;   //5:신청, 7:신청 취소일 경우 사용
    private String msg;

    /* WOWZA 정보 */
    private String rtmpOrigin="";
    private String rtmpEdge="";
    private String webRtcUrl="";
    private String webRtcAppName="";
    private String webRtcStreamName="";

}
