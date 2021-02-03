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
        //setWebRtcStreamName(wowza_prefix + pGuestListVo.getRoom_no() + "_" + this.memNo);
        setWebRtcStreamName(wowza_prefix + pGuestListVo.getRoom_no() + this.memNo);
        if("a".equals(pGuestListVo.getType_media())){
            setRtmpOrigin(DalbitUtil.getProperty("wowza.audio.rtmp.origin") + "/" + this.webRtcStreamName);// + "_aac";
            setRtmpEdge(DalbitUtil.getProperty("wowza.audio.rtmp.edge") + "/" + this.webRtcStreamName + DalbitUtil.getProperty("wowza.aac"));
            setWebRtcUrl(DalbitUtil.getProperty("wowza.audio.wss.url"));
        }else{
            setRtmpOrigin(DalbitUtil.getProperty("wowza.video.rtmp.origin") + "/" + this.webRtcStreamName);// + "_aac";
            setRtmpEdge(DalbitUtil.getProperty("wowza.video.rtmp.edge") + "/" + this.webRtcStreamName + DalbitUtil.getProperty("wowza.aac"));
            setWebRtcUrl(this.memNo.equals(nowMemNo) ? DalbitUtil.getProperty("wowza.video.wss.url.origin") : DalbitUtil.getProperty("wowza.video.wss.url.edge"));
        }
        if(this.memNo.equals(nowMemNo)){
            setWebRtcAppName("origin");
        }else{
            setWebRtcAppName("edge");
            //setWebRtcStreamName(wowza_prefix + pGuestListVo.getRoom_no() + "_" + this.memNo+"_opus");
            setWebRtcStreamName(wowza_prefix + pGuestListVo.getRoom_no() + this.memNo + DalbitUtil.getProperty("wowza.opus"));
        }

    }

}
