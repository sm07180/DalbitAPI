package com.dalbit.broadcast.vo;

import com.dalbit.broadcast.vo.procedure.P_GuestListVo;
import com.dalbit.common.vo.ImageVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RoomGuestListOutVo {

    private String memNo;
    private ImageVo profImg;
    private String nickNm;
    /* WOWZA 정보 */
    private String rtmpOrigin;
    private String rtmpEdge;
    private String webRtcUrl;
    private String webRtcAppName;
    private String webRtcStreamName;

    public RoomGuestListOutVo(){}
    public RoomGuestListOutVo(P_GuestListVo pGuestListVo, String nowMemNo, String wowza_prefix){
        setMemNo(pGuestListVo.getMem_no());
        setProfImg(new ImageVo(pGuestListVo.getImage_profile(), pGuestListVo.getMem_sex(), DalbitUtil.getProperty("server.photo.url")));
        setNickNm(pGuestListVo.getMem_nick());
        setWebRtcStreamName(wowza_prefix + pGuestListVo.getRoom_no() + "_" + this.memNo);
        setRtmpOrigin(DalbitUtil.getProperty("wowza.rtmp.origin") + "/" + this.webRtcStreamName);// + "_aac";
        setRtmpEdge(DalbitUtil.getProperty("wowza.rtmp.edge") + "/" + this.webRtcStreamName + "_aac");
        setWebRtcUrl(DalbitUtil.getProperty("wowza.wss.url"));
        if(this.memNo.equals(nowMemNo)){
            setWebRtcAppName("origin");
        }else{
            setWebRtcAppName("edge");
            setWebRtcStreamName(wowza_prefix + pGuestListVo.getRoom_no() + "_" + this.memNo+"_opus");
        }

    }

}
