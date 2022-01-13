package com.dalbit.member.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.BroadcastSettingEditVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_BroadcastSettingEditVo extends P_ApiVo {

    private String mem_no;
    private Integer giftFanReg;
    private Integer djListenerIn;
    private Integer djListenerOut;
    private Integer listenerIn;
    private Integer listenerOut;
    private Integer liveBadgeView;
    private Integer listenOpen;
    private Integer ttsSound;
    private Integer normalSound;

    private String bjMemNo; // 방송방 - 요청한 유저가 방장 본인인지 확인하기 위한 구분값
    private String roomNo;  // 방송방 여부 체크 (공통으로 쓰는 API)
    
    public P_BroadcastSettingEditVo(){}
    public P_BroadcastSettingEditVo(BroadcastSettingEditVo broadcastSettingEditVo, HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
        setGiftFanReg(!DalbitUtil.isEmpty(broadcastSettingEditVo.getGiftFanReg()) ? (("true".equals(broadcastSettingEditVo.getGiftFanReg().toLowerCase()) || "1".equals(broadcastSettingEditVo.getGiftFanReg())) ? 1 : 0) : null);
        setDjListenerIn(!DalbitUtil.isEmpty(broadcastSettingEditVo.getDjListenerIn()) ? (("true".equals(broadcastSettingEditVo.getDjListenerIn().toLowerCase()) || "1".equals(broadcastSettingEditVo.getDjListenerIn())) ? 1 : 0) : null);
        setDjListenerOut(!DalbitUtil.isEmpty(broadcastSettingEditVo.getDjListenerOut()) ? (("true".equals(broadcastSettingEditVo.getDjListenerOut().toLowerCase()) || "1".equals(broadcastSettingEditVo.getDjListenerOut())) ?  1 : 0) : null);
        setListenerIn(!DalbitUtil.isEmpty(broadcastSettingEditVo.getListenerIn()) ? (("true".equals(broadcastSettingEditVo.getListenerIn().toLowerCase()) || "1".equals(broadcastSettingEditVo.getListenerIn())) ? 1: 0) : null);
        setListenerOut(!DalbitUtil.isEmpty(broadcastSettingEditVo.getListenerOut()) ? (("true".equals(broadcastSettingEditVo.getListenerOut().toLowerCase()) || "1".equals(broadcastSettingEditVo.getListenerOut())) ? 1: 0) : null);
        setLiveBadgeView(!DalbitUtil.isEmpty(broadcastSettingEditVo.getLiveBadgeView()) ? (("true".equals(broadcastSettingEditVo.getLiveBadgeView().toLowerCase()) || "1".equals(broadcastSettingEditVo.getLiveBadgeView())) ? 1: 0) : null);
        setListenOpen(!DalbitUtil.isEmpty(broadcastSettingEditVo.getListenOpen()) ? Integer.parseInt(broadcastSettingEditVo.getListenOpen()) : null);
        setTtsSound(!DalbitUtil.isEmpty(broadcastSettingEditVo.getTtsSound()) ? (("true".equals(broadcastSettingEditVo.getTtsSound().toLowerCase()) || "1".equals(broadcastSettingEditVo.getTtsSound())) ? 1: 0) : null);
        setNormalSound(!DalbitUtil.isEmpty(broadcastSettingEditVo.getNormalSound()) ? (("true".equals(broadcastSettingEditVo.getNormalSound().toLowerCase()) || "1".equals(broadcastSettingEditVo.getNormalSound())) ? 1: 0) : null);
        setRoomNo(!DalbitUtil.isEmpty(broadcastSettingEditVo.getRoomNo()) ? broadcastSettingEditVo.getRoomNo() : "");
        setBjMemNo(!DalbitUtil.isEmpty(broadcastSettingEditVo.getBjMemNo()) ? broadcastSettingEditVo.getBjMemNo() : "");
    }

}
