package com.dalbit.clip.controller;

import com.dalbit.clip.service.ClipService;
import com.dalbit.clip.vo.procedure.*;
import com.dalbit.clip.vo.request.*;
import com.dalbit.common.code.Status;
import com.dalbit.common.service.CommonService;
import com.dalbit.common.vo.DeviceVo;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.exception.GlobalException;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/clip")
public class ClipController {

    @Autowired
    ClipService clipService;
    @Autowired
    CommonService commonService;
    @Autowired
    GsonUtil gsonUtil;


    /**
     * 클립 등록
     */
    @PostMapping("/add")
    public String clipAdd(@Valid ClipAddVo clipAddVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_ClipAddVo apiData = new P_ClipAddVo(clipAddVo, request);
        String result = clipService.clipAdd(apiData, request);
        return result;
    }


    /**
     * 클립 수정
     */
    @PostMapping("/edit")
    public String clipEdit(@Valid ClipEditVo clipEditVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_ClipEditVo apiData = new P_ClipEditVo(clipEditVo, request);
        String result = clipService.clipEdit(apiData, request);
        return result;
    }


    /**
     * 클립 삭제
     */
    @PostMapping("/delete")
    public String clipDelete(@Valid ClipDeleteVo clipDeletetVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_ClipDeleteVo apiData = new P_ClipDeleteVo(clipDeletetVo, request);
        String result = clipService.clipDelete(apiData, request);
        return result;
    }


    /**
     * 클립 리스트
     */
    @GetMapping("/list")
    public String clipList(@Valid ClipListVo clipListVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_ClipListVo apiData = new P_ClipListVo(clipListVo, request);
        String result = clipService.clipList(apiData);
        return result;

    }


    /**
     * 클립 플레이
     */
    @PostMapping("/play")
    public String clipPlay(@Valid ClipPlayVo clipPlayVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_ClipPlayVo apiData = new P_ClipPlayVo(clipPlayVo, request);
        String result = clipService.clipPlay(apiData, "play", request);
        return result;
    }


    /**
     * 클립 재생목록
     */
    @GetMapping("/play/list")
    public String clipPlayList(@Valid ClipPlayListVo clipPlayListVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_ClipPlayListVo apiData = new P_ClipPlayListVo(clipPlayListVo, request);
        String result = clipService.clipPlayList(apiData);
        return result;

    }


    /**
     * 클립 선물하기
     */
    @PostMapping("/gift")
    public String clipGift(@Valid ClipGiftVo clipGiftVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getClassName());

        P_ClipGiftVo apiData = new P_ClipGiftVo(clipGiftVo, request);
        String result = clipService.clipGift(apiData);
        return result;
    }


    /**
     * 클립 좋아요
     */
    @PostMapping("/good")
    public String clipGood(@Valid ClipGoodVo clipGoodVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getClassName());

        P_ClipGoodVo apiData = new P_ClipGoodVo(clipGoodVo, request);
        String result = clipService.clipGood(apiData);
        return result;
    }


    /**
     * 클립 선물랭킹 TOP3
     */
    @GetMapping("/gift/rank/top3")
    public String clipRankTop3(@Valid ClipGiftRankTop3Vo clipGiftRankTop3Vo, BindingResult bindingResult) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getClassName());

        P_ClipGiftRankTop3Vo apiData = new P_ClipGiftRankTop3Vo(clipGiftRankTop3Vo);
        String result = clipService.clipGiftRankTop3(apiData);
        return result;
    }


    /**
     * 클립 선물랭킹 리스트
     */
    @GetMapping("/gift/rank/list")
    public String clipRankList(@Valid ClipGiftRankListVo clipGiftRankListVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getClassName());

        P_ClipGiftRankListVo apiData = new P_ClipGiftRankListVo(clipGiftRankListVo, request);
        String result = clipService.clipRankList(apiData, request);
        return result;
    }


    /**
     * 클립 받은선물내역
     */
    @GetMapping("/gift/list")
    public String clipGiftList(@Valid ClipGiftListVo clipGiftListVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getClassName());

        P_ClipGiftListVo apiData = new P_ClipGiftListVo(clipGiftListVo, request);
        String result = clipService.clipGiftList(apiData, request);
        return result;
    }


    /**
     * 클립 댓글목록 조회
     */
    @GetMapping("/reply/list")
    public String clipReplyList(@Valid ClipReplyListVo clipReplyListVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getClassName());

        P_ClipReplyListVo apiData = new P_ClipReplyListVo(clipReplyListVo, request);
        String result = clipService.clipReplyList(apiData);
        return result;
    }


    /**
     * 클립 댓글 등록
     */
    @PostMapping("/reply/add")
    public String clipReplyAdd(@Valid ClipReplyAddVo clipReplyAddVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getClassName());

        P_ClipReplyAddVo apiData = new P_ClipReplyAddVo(clipReplyAddVo, request);
        String result = clipService.clipReplyAdd(apiData, request);
        return result;
    }


    /**
     * 클립 댓글 수정
     */
    @PostMapping("/reply/edit")
    public String clipReplyEdit(@Valid ClipReplyEditVo clipReplyEditVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getClassName());

        P_ClipReplyEditVo apiData = new P_ClipReplyEditVo(clipReplyEditVo, request);
        String result = clipService.clipReplyEdit(apiData, request);
        return result;
    }


    /**
     * 클립 댓글 삭제
     */
    @PostMapping("/reply/delete")
    public String clipReplyDelete(@Valid ClipReplyDeleteVo clipReplyDeleteVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getClassName());

        P_ClipReplyDeleteVo apiData = new P_ClipReplyDeleteVo(clipReplyDeleteVo, request);
        String result = clipService.clipReplyDelete(apiData, request);
        return result;
    }


    /**
     * 마이페이지 클립 업로드 리스트
     */
    @GetMapping("/upload/list")
    public String clipUploadList(@Valid ClipUploadListVo clipUploadListVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_ClipUploadListVo apiData = new P_ClipUploadListVo(clipUploadListVo, request);
        String result = clipService.clipUploadList(apiData);
        return result;
    }


    /**
     * 마이페이지 클립 청취내역
     */
    @GetMapping("/listen/list")
    public String clipListenList(@Valid ClipListenListVo clipListenListVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_ClipListenListVo apiData = new P_ClipListenListVo(clipListenListVo, request);
        String result = clipService.clipListenList(apiData);
        return result;
    }


    /**
     * 클립 재생목록 편집
     */
    @PostMapping("/play/list/edit")
    public String clipPlayListEdit(@Valid ClipPlayListEditVo clipPlayListEditVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getClassName());

        P_ClipPlayListEditVo apiData = new P_ClipPlayListEditVo(clipPlayListEditVo, request);
        String result = clipService.clipPlayListEdit(apiData);
        return result;
    }


    /**
     * 클립 메인 인기리스트
     */
    @GetMapping("/main/pop/list")
    public String clipMainPopList(HttpServletRequest request) {
        P_ClipMainPopListVo apiData = new P_ClipMainPopListVo(request);
        String result = clipService.clipMainPopList(apiData, request);
        return result;
    }


    /**
     * 클립 메인 최신리스트
     */
    @GetMapping("/main/latest/list")
    public String clipMainLatestList(HttpServletRequest request) {
        P_ClipMainLatestListVo apiData = new P_ClipMainLatestListVo(request);
        String result = clipService.clipMainLatestList(apiData);
        return result;
    }


    /**
     * 클립 메인 주제별 TOP3 리스트
     */
    @GetMapping("/main/top3/list")
    public String clipMainSubjectTop3List(HttpServletRequest request) {
        String result = clipService.clipMainSubjectTop3List(request);
        return result;
    }


    /**
     * 클립 신고하기
     */
    @PostMapping("/declar")
    public String clipDeclar(@Valid ClipDeclarVo clipDeclarVo, BindingResult bindingResult, HttpServletRequest request)throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_ClipDeclarVo apiData = new P_ClipDeclarVo(clipDeclarVo, new DeviceVo(request), request);
        String result = clipService.callClipDeclar(apiData);
        return result;
    }


    /**
     * 클립 공유하기
     */
    @GetMapping("/share")
    public String clipShare(@Valid ClipPlayVo clipPlayVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_ClipPlayVo apiData = new P_ClipPlayVo(clipPlayVo, request);
        HashMap result = clipService.clipShare(apiData, request);

        return gsonUtil.toJson(new JsonOutputVo((Status)result.get("status"), result.get("data")));
    }


    /**
     * 내 클립 현황
     */
    @GetMapping("/myclip")
    public String myClip(HttpServletRequest request){
        P_MyClipVo apiData = new P_MyClipVo(request);
        String result = clipService.callMyClip(apiData);
        return result;
    }


}
