package com.dalbit.broadcast.controller;

import com.dalbit.broadcast.service.ContentService;
import com.dalbit.broadcast.vo.P_RoomNoticeEditVo;
import com.dalbit.broadcast.vo.P_RoomNoticeSelectVo;
import com.dalbit.common.code.Status;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.MessageUtil;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Member;
import java.util.HashMap;

@RestController
@RequestMapping("broad")
public class ContentController {

    @Autowired
    MessageUtil messageUtil;

    @Autowired
    ContentService contentService;

    /**
     * 공지조회
     */
    @GetMapping("/notice")
    public String noticeSelect(HttpServletRequest request){

        P_RoomNoticeSelectVo apiData = new P_RoomNoticeSelectVo();
        apiData.setMem_no(MemberVo.getMyMemNo());
        apiData.setRoom_no(DalbitUtil.convertRequestParamToString(request,"roomNo"));

        String result = contentService.callBroadCastRoomNoticeSelect(apiData);
        return result;
    }

    /**
     * 공지사항 입력/수정
     */
    @PostMapping("/notice")
    public String noticeEdit(HttpServletRequest request){

        P_RoomNoticeEditVo apiData = new P_RoomNoticeEditVo();
        apiData.setMem_no(MemberVo.getMyMemNo());
        apiData.setRoom_no(DalbitUtil.convertRequestParamToString(request, "roomNo"));
        apiData.setNotice(DalbitUtil.convertRequestParamToString(request,"notice"));

        String result = contentService.callBroadCastRoomNoticeEdit(apiData);
        return result;
    }

    /**
     * 공지삭제
     */
    @DeleteMapping("/notice")
    public String noticeDelete(HttpServletRequest request){

        P_RoomNoticeSelectVo apiData = new P_RoomNoticeSelectVo();
        apiData.setMem_no(MemberVo.getMyMemNo());
        apiData.setRoom_no(DalbitUtil.convertRequestParamToString(request,"roomNo"));

        String result = contentService.callBroadCastRoomNoticeDelete(apiData);
        return result;
    }

    /**
     * 사연조회
     */
    @GetMapping("{brodNo}/story")
    public String getStory(@PathVariable String brodNo){

        HashMap map = new HashMap();
        map.put("brodNo", brodNo);

        return new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.조회, map)));
    }

    /**
     * 사연등록
     */
    @PostMapping("{brodNo}/story")
    public String insertStory(@PathVariable String brodNo){

        HashMap map = new HashMap();
        map.put("brodNo", brodNo);

        return new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.생성, map)));
    }
}
