package com.dalbit.broadcast.controller;

import com.dalbit.broadcast.service.ContentService;
import com.dalbit.broadcast.vo.BroadcastNoticeUpdVo;
import com.dalbit.broadcast.vo.procedure.*;
import com.dalbit.broadcast.vo.request.*;
import com.dalbit.common.code.Status;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.exception.GlobalException;
import com.dalbit.member.vo.BroadcastNoticeSelVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/broad")
@Scope("prototype")
public class ContentController {

    @Autowired
    ContentService contentService;
    @Autowired
    GsonUtil gsonUtil;

    /**
     * 방송방 공지조회
     */
//    @GetMapping("/notice")
//    public String noticeSelect(@Valid NoticeViewVo noticeViewVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
//
//        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
//
//        P_RoomNoticeVo apiData = new P_RoomNoticeVo();
//        apiData.setMem_no(MemberVo.getMyMemNo(request));
//        apiData.setRoom_no(noticeViewVo.getRoomNo());
//
//        String result = contentService.callBroadCastRoomNoticeSelect(apiData);
//
//        return result;
//    }

    @GetMapping("/notice")
    public String noticeSelect(@Valid BroadcastNoticeSelVo noticeSelVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        try {
            return contentService.mobileBroadcastNoticeSelect(noticeSelVo, request);
        } catch (Exception e) {
            log.error("noticeSelect Error : {}", e);
            return gsonUtil.toJson(new JsonOutputVo(Status.공지조회_실패));
        }

    }

    /**
     * 공지사항 입력/수정
     */
//    @PostMapping("/notice")
//    public String noticeEdit(@Valid NoticeEditVo noticeEditVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
//
//        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
//
//        P_RoomNoticeEditVo apiData = new P_RoomNoticeEditVo();
//        apiData.setMem_no(MemberVo.getMyMemNo(request));
//        apiData.setRoom_no(noticeEditVo.getRoomNo());
//        apiData.setNotice(noticeEditVo.getNotice());
//
//        String result = contentService.callBroadCastRoomNoticeEdit(apiData, request);
//
//        return result;
//    }

    @PostMapping("/notice")
    public String noticeEdit(@Valid BroadcastNoticeUpdVo param, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        try {
            return contentService.mobileBroadcastNoticeUpd(param, request);
        } catch (Exception e) {
            log.error("noticeEdit Error : {}", e);
            return gsonUtil.toJson(new JsonOutputVo(Status.공지등록_실패));
        }
    }


    /**
     * 공지삭제
     */
    @DeleteMapping("/notice")
    public String noticeDelete(@Valid NoticeDelVo noticeDelVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_RoomNoticeVo apiData = new P_RoomNoticeVo();
        apiData.setMem_no(MemberVo.getMyMemNo(request));
        apiData.setRoom_no(noticeDelVo.getRoomNo());

        String result = contentService.callBroadCastRoomNoticeDelete(apiData, request);

        return result;
    }


    /**
     * 방송방 사연 등록
     */
    @PostMapping("/story")
    public String insertStory(@Valid StoryAddVo storyAddVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_RoomStoryAddVo apiData = new P_RoomStoryAddVo();
        apiData.setMem_no(MemberVo.getMyMemNo(request));
        apiData.setRoom_no(storyAddVo.getRoomNo());
        apiData.setContents(storyAddVo.getContents());

        String result = contentService.callInsertStory(apiData, request);

        return result;
    }


    /**
     * 방송방 사연 조회
     */
    @GetMapping("/story")
    public String getStory(@Valid StoryViewVo storyViewVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        int pageNo = DalbitUtil.isEmpty(storyViewVo.getPage()) ? 1 : storyViewVo.getPage();
        int pageCnt = DalbitUtil.isEmpty(storyViewVo.getRecords()) ? 10 : storyViewVo.getRecords();

        P_RoomStoryListVo apiData = new P_RoomStoryListVo();
        apiData.setMem_no(MemberVo.getMyMemNo(request));
        apiData.setRoom_no(storyViewVo.getRoomNo());
        apiData.setPageNo(pageNo);
        apiData.setPageCnt(pageCnt);

        String result = contentService.callGetStory(apiData, request);

        return result;
    }


    /**
     * 방송방 사연 삭제
     */
    @DeleteMapping("/story")
    public String deleteStory(@Valid StoryDelVo storyDelVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        int page = DalbitUtil.isEmpty(storyDelVo.getPage()) ? 1 : storyDelVo.getPage();
        int records = DalbitUtil.isEmpty(storyDelVo.getRecords()) ? 10 : storyDelVo.getRecords();

        P_RoomStoryDeleteVo apiData = new P_RoomStoryDeleteVo();
        apiData.setMem_no(MemberVo.getMyMemNo(request));
        apiData.setRoom_no(storyDelVo.getRoomNo());
        apiData.setStory_idx(storyDelVo.getStoryIdx());
        apiData.setPage(page);
        apiData.setRecords(records);

        String result = contentService.callDeleteStory(apiData);

        return result;
    }
}
