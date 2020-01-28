package com.dalbit.broadcast.controller;

import com.dalbit.broadcast.service.ContentService;
import com.dalbit.broadcast.vo.*;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

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

        P_RoomNoticeVo apiData = new P_RoomNoticeVo();
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

        P_RoomNoticeVo apiData = new P_RoomNoticeVo();
        apiData.setMem_no(MemberVo.getMyMemNo());
        apiData.setRoom_no(DalbitUtil.convertRequestParamToString(request,"roomNo"));

        String result = contentService.callBroadCastRoomNoticeDelete(apiData);
        return result;
    }

    /**
     * 방송방 사연 등록
     */
    @PostMapping("/story")
    public String insertStory(HttpServletRequest request){
        P_RoomStoryAddVo apiData = new P_RoomStoryAddVo();
        apiData.setMem_no(MemberVo.getMyMemNo());
        apiData.setRoom_no(DalbitUtil.convertRequestParamToString(request, "roomNo"));
        apiData.setContents(DalbitUtil.convertRequestParamToString(request, "contents"));

        String result = contentService.callInsertStory(apiData);

        return result;
    }

    /**
     * 방송방 사연 조회
     */
    @GetMapping("/story")
    public String getStory(HttpServletRequest request){
        int pageNo = (DalbitUtil.convertRequestParamToInteger(request, "page")) == -1 ? 1 : DalbitUtil.convertRequestParamToInteger(request, "page");
        int pageCnt = (DalbitUtil.convertRequestParamToInteger(request, "records")) == -1 ? 5 : DalbitUtil.convertRequestParamToInteger(request, "records");

        P_RoomStoryListVo apiData = new P_RoomStoryListVo();
        apiData.setMem_no(MemberVo.getMyMemNo());
        apiData.setRoom_no(DalbitUtil.convertRequestParamToString(request, "roomNo"));
        apiData.setPageNo(pageNo);
        apiData.setPageCnt(pageCnt);

        String result = contentService.callGetStory(apiData);

        return result;
    }

    /**
     * 방송방 사연 삭제
     */
    @DeleteMapping("/story")
    public String deleteStory(HttpServletRequest request){
        P_RoomStoryDeleteVo apiData = new P_RoomStoryDeleteVo();
        apiData.setMem_no(MemberVo.getMyMemNo());
        apiData.setRoom_no(DalbitUtil.convertRequestParamToString(request, "roomNo"));
        apiData.setStory_idx(DalbitUtil.convertRequestParamToInteger(request, "storyIdx"));

        String result = contentService.callDeleteStory(apiData);

        return result;
    }
}
