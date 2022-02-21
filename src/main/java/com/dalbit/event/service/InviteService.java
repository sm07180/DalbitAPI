package com.dalbit.event.service;


import com.dalbit.common.code.Status;
import com.dalbit.common.vo.ImageVo;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.event.proc.InviteEvent;
import com.dalbit.event.vo.GganbuMemberSearchVo;
import com.dalbit.event.vo.InviteVo;
import com.dalbit.util.DBUtil;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InviteService {

    private final InviteEvent event;
    private final GsonUtil gsonUtil;

    public String pEvtInvitationMemberIns(String memNo, String invitationCode, String memPhone) {
        Integer result = event.pEvtInvitationMemberIns(memNo, invitationCode, memPhone);

        if(result == 1){
            return gsonUtil.toJson(new JsonOutputVo(Status.초대코드_생성, result));
        }else if(result == -1){
            return gsonUtil.toJson(new JsonOutputVo(Status.초대코드_중복, result));
        }else{
            return gsonUtil.toJson(new JsonOutputVo(Status.비즈니스로직오류, result));
        }
    }

    public String pEvtInvitationRcvMemberChk(String memNo, String memPhone) {
        Integer result = event.pEvtInvitationRcvMemberChk(memNo, memPhone);
        if(result == 1){
            return gsonUtil.toJson(new JsonOutputVo(Status.친구초대_참여대상, result));
        }else if(result == -1){
            return gsonUtil.toJson(new JsonOutputVo(Status.친구초대_참여대상_아님, result));
        }else{
            return gsonUtil.toJson(new JsonOutputVo(Status.비즈니스로직오류, result));
        }
    }

    public String pEvtInvitationRewardIns(String rcvMemNo, String rcvMemIp, String invitationCode, String memPhone) {

        Integer checkAge = event.pEvtInvitationRcvMemberChk(rcvMemNo, memPhone);
        if(checkAge == 1){
            Integer result = event.pEvtInvitationRewardIns(rcvMemNo, rcvMemIp, invitationCode);
            if(result == 1){
                return gsonUtil.toJson(new JsonOutputVo(Status.친구코드_등록_성공, result));
            }else if(result == -1){
                return gsonUtil.toJson(new JsonOutputVo(Status.친구코드_없음, result));
            }else if(result == -2){
                return gsonUtil.toJson(new JsonOutputVo(Status.친구코드_등록_에러, result));
            }else if(result == -3){
                return gsonUtil.toJson(new JsonOutputVo(Status.친구코드_중복_등록, result));
            }else{
                return gsonUtil.toJson(new JsonOutputVo(Status.비즈니스로직오류, result));
            }
        }else{
            return gsonUtil.toJson(new JsonOutputVo(Status.친구초대_참여대상_아님, checkAge));
        }

    }

    public String pEvtInvitationMemberSel(String memNo) {
        InviteVo inviteVo =  event.pEvtInvitationMemberSel(memNo);
        inviteVo.setProfImg(new ImageVo(inviteVo.getSend_image_profile(), inviteVo.getSend_mem_sex(), DalbitUtil.getProperty("server.photo.url")));
        if(inviteVo!=null){
            return gsonUtil.toJson(new JsonOutputVo(Status.조회, inviteVo));
        }else{
            return gsonUtil.toJson(new JsonOutputVo(Status.데이터없음));
        }
    }

    public String pEvtInvitationMemberRankList(Integer pageNo, Integer pagePerCnt) {
        List<Object> object = event.pEvtInvitationMemberRankList(pageNo, pagePerCnt);
        Integer listCnt = DBUtil.getData(object, 0, Integer.class);
        List<InviteVo> list = DBUtil.getList(object, 1, InviteVo.class);
        list.stream().parallel().forEach(item -> {
            item.setProfImg(new ImageVo(item.getImage_profile(), item.getMem_sex(), DalbitUtil.getProperty("server.photo.url")));
        });
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("listCnt", listCnt);
        resultMap.put("list", list);
        return gsonUtil.toJson(new JsonOutputVo(Status.조회, resultMap));
    }

    public String pEvtInvitationMemberRankMySel(String memNo) {
        InviteVo inviteVo = event.pEvtInvitationMemberRankMySel(memNo);
        inviteVo.setProfImg(new ImageVo(inviteVo.getImage_profile(), inviteVo.getMem_sex(), DalbitUtil.getProperty("server.photo.url")));
        if(inviteVo!=null){
            return gsonUtil.toJson(new JsonOutputVo(Status.조회, inviteVo));
        }else{
            return gsonUtil.toJson(new JsonOutputVo(Status.데이터없음));
        }
    }

    public String pEvtInvitationRcvMemberList(String memNo, Integer pageNo, Integer pagePerCnt) {
        List<Object> object = event.pEvtInvitationRcvMemberList(memNo, pageNo, pagePerCnt);
        Integer listCnt = DBUtil.getData(object, 0, Integer.class);
        List<InviteVo> list = DBUtil.getList(object, 1, InviteVo.class);
        list.stream().parallel().forEach(item -> {
            item.setProfImg(new ImageVo(item.getRcv_image_profile(), item.getRcv_mem_sex(), DalbitUtil.getProperty("server.photo.url")));
        });
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("listCnt", listCnt);
        resultMap.put("list", list);
        return gsonUtil.toJson(new JsonOutputVo(Status.조회, resultMap));
    }
}
