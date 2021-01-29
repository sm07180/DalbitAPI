package com.dalbit.event.service;

import com.dalbit.common.code.EventCode;
import com.dalbit.common.code.Status;
import com.dalbit.common.vo.DeviceVo;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.event.dao.CamDao;
import com.dalbit.event.dao.EventDao;
import com.dalbit.event.vo.EventMemberVo;
import com.dalbit.event.vo.PhotoEventInputVo;
import com.dalbit.event.vo.procedure.P_CamCheckVo;
import com.dalbit.event.vo.request.CamApplyVo;
import com.dalbit.member.dao.MypageDao;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.SpecialDjConditionSearchVo;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@Slf4j
@Service
public class CamService {

    @Autowired
    EventService eventService;

    @Autowired
    EventDao eventDao;

    @Autowired
    CamDao camDao;

    @Autowired
    MypageDao mypageDao;

    @Autowired
    GsonUtil gsonUtil;

    /**
     * 이벤트 페이지 로드 시
     */
    public String camStatus(P_CamCheckVo pCamCheckVo){

        EventCode eventCode = EventCode.웹캠;
        int eventCheck = eventService.eventDateCheck(eventCode.getEventIdx());
        if(eventCheck == 0){
            return gsonUtil.toJson(new JsonOutputVo(Status.이벤트_참여날짜아님));
        }

        if(!DalbitUtil.isLogin(pCamCheckVo.getMem_no())){
            return gsonUtil.toJson(new JsonOutputVo(Status.로그인필요));
        }

        var specialDjConditionSearchVo = new SpecialDjConditionSearchVo();
        specialDjConditionSearchVo.setMem_no(pCamCheckVo.getMem_no());
        HashMap map = getBroadcastAirTime(specialDjConditionSearchVo);

        return gsonUtil.toJson(new JsonOutputVo(Status.조회, map));
    }

    public HashMap<String, Object> getBroadcastAirTime(SpecialDjConditionSearchVo specialDjConditionSearchVo){
        specialDjConditionSearchVo.setSlct_type(2);
        specialDjConditionSearchVo.setCondition_start_date("2020-01-01");
        specialDjConditionSearchVo.setCondition_end_date("2022-01-01");
        long second = mypageDao.selectSpecialDjBroadcastTime(specialDjConditionSearchVo);


        long minute = second / 60;
        long hour = minute / 60;

        minute = minute % 60;

        var map = new HashMap<String, Object>();
        map.put("airTime", hour);
        map.put("airTimeStr",  hour+"시간 " + minute +"분");

        return map;
    }

    /**
     * 지원 신청 체크
     */
    public String callCamCheck(P_CamCheckVo pCamCheckVo) {

        EventCode eventCode = EventCode.웹캠;

        int eventCheck = eventService.eventDateCheck(eventCode.getEventIdx());
        if(eventCheck == 0){
            return gsonUtil.toJson(new JsonOutputVo(Status.이벤트_참여날짜아님));
        }

        if(!DalbitUtil.isLogin(pCamCheckVo.getMem_no())){
            return gsonUtil.toJson(new JsonOutputVo(Status.로그인필요));
        }

        if(!eventCode.isMulti()){
            var checkDuplJoin = new PhotoEventInputVo();
            checkDuplJoin.setEvent_idx(eventCode.getEventIdx());
            checkDuplJoin.setSlct_type(1);
            checkDuplJoin.setMem_no(pCamCheckVo.getMem_no());
            eventDao.selectPhotoList(checkDuplJoin);
            int totCnt = checkDuplJoin.getTotalCnt();
            if(0 < totCnt){
                return gsonUtil.toJson(new JsonOutputVo(Status.이벤트_이미참여));
            }
        }

        var specialDjConditionSearchVo = new SpecialDjConditionSearchVo();
        specialDjConditionSearchVo.setMem_no(pCamCheckVo.getMem_no());

        HashMap map = getBroadcastAirTime(specialDjConditionSearchVo);

        int isOk = 10 <= DalbitUtil.getIntMap(map, "airTime") ? 1 : 0;
        map.put("isOk", isOk);

        return gsonUtil.toJson(new JsonOutputVo(Status.조회, map));
    }


    /**
     * 신청서 작성
     */
    public String callCamApply(HttpServletRequest request, CamApplyVo camApplyVo) {

        EventCode eventCode = EventCode.웹캠;
        String mem_no = MemberVo.getMyMemNo(request);

        int eventCheck = eventService.eventDateCheck(eventCode.getEventIdx());
        if(eventCheck == 0){
            return gsonUtil.toJson(new JsonOutputVo(Status.이벤트_참여날짜아님));
        }

        if(!DalbitUtil.isLogin(mem_no)){
            return gsonUtil.toJson(new JsonOutputVo(Status.로그인필요));
        }

        var specialDjConditionSearchVo = new SpecialDjConditionSearchVo();
        specialDjConditionSearchVo.setMem_no(mem_no);
        HashMap map = getBroadcastAirTime(specialDjConditionSearchVo);
        if(DalbitUtil.getIntMap(map, "airTime") < 10){
            return gsonUtil.toJson(new JsonOutputVo(Status.이벤트_체크_자격안됨, map));
        }

        if(!eventCode.isMulti()){
            var checkDuplJoin = new PhotoEventInputVo();
            checkDuplJoin.setEvent_idx(eventCode.getEventIdx());
            checkDuplJoin.setSlct_type(1);
            checkDuplJoin.setMem_no(mem_no);
            eventDao.selectPhotoList(checkDuplJoin);
            int totCnt = checkDuplJoin.getTotalCnt();
            if(0 < totCnt){
                return gsonUtil.toJson(new JsonOutputVo(Status.이벤트_이미참여));
            }
        }

        var photoEventInputVo = new PhotoEventInputVo();
        var deviceVo = new DeviceVo(request);
        var eventMemberVo = new EventMemberVo();

        eventMemberVo.setMem_no(mem_no);
        eventMemberVo.setEvent_idx(eventCode.getEventIdx());
        eventMemberVo.setPlatform(deviceVo.getOs());
        eventDao.insertEventMember(eventMemberVo);

        photoEventInputVo.setEvent_idx(eventCode.getEventIdx());
        photoEventInputVo.setEvent_member_idx(eventMemberVo.getEvent_member_idx());
        photoEventInputVo.setMem_no(mem_no);
        photoEventInputVo.setMem_name(camApplyVo.getName());
        photoEventInputVo.setMem_phone(camApplyVo.getPhone());
        photoEventInputVo.setImage_url(camApplyVo.getPostCode());
        photoEventInputVo.setImage_url2(camApplyVo.getAddress_1());
        photoEventInputVo.setImage_url3(camApplyVo.getAddress_2());
        photoEventInputVo.setDevice1(camApplyVo.getDevice());
        photoEventInputVo.setContents(camApplyVo.getContents());

        eventDao.insertPhoto(photoEventInputVo);

        String result = gsonUtil.toJson(new JsonOutputVo(Status.이벤트_참여));
        return result;
    }
}
