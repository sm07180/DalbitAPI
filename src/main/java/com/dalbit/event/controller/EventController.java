package com.dalbit.event.controller;

import com.dalbit.common.code.EventCode;
import com.dalbit.common.code.Status;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.event.service.EventService;
import com.dalbit.event.vo.KnowhowEventInputVo;
import com.dalbit.event.vo.PhotoEventInputVo;
import com.dalbit.event.vo.TimeEventVo;
import com.dalbit.event.vo.procedure.*;
import com.dalbit.event.vo.request.*;
import com.dalbit.exception.GlobalException;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;

@RestController
@RequestMapping("/event")
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private GsonUtil gsonUtil;

    @GetMapping("/ranking/term")
    public String event200608Term() {
        return eventService.event200608Term();
    }

    /**
     * 랭킹 이벤트 실시간 순위보기
     */
    @GetMapping("/ranking/live")
    public String rankingLive(@Valid RankingLiveVo rankingLiveVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_RankingLiveInputVo apiData = new P_RankingLiveInputVo();
        apiData.setMem_no(MemberVo.getMyMemNo(request));
        apiData.setSlct_type(rankingLiveVo.getSlctType());

        String result = eventService.callEventRankingLive(apiData);

        return result;
    }

    /**
     * 랭킹 이벤트 결과 보기
     */
    @GetMapping("/ranking/result")
    public String rankingResult(@Valid RankingResultVo rankingResultVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_RankingResultInputVo apiData = new P_RankingResultInputVo();
        apiData.setMem_no(MemberVo.getMyMemNo(request));
        apiData.setSlct_type(rankingResultVo.getSlctType());
        apiData.setRound(rankingResultVo.getRound());

        String result = eventService.callEventRankingResult(apiData);

        return result;
    }


    /**
     * 이벤트 댓글 리스트 조회
     */
    @GetMapping("/reply")
    public String replyList(@Valid ReplyVo replyVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_ReplyListInputVo apiData = new P_ReplyListInputVo();
        apiData.setMem_no(MemberVo.getMyMemNo(request));
        apiData.setEvent_idx(replyVo.getEventIdx());

        String result = eventService.callEventReplyList(apiData);

        return result;
    }


    /**
     * 이벤트 댓글 등록
     */
    @PostMapping("/reply")
    public String replyAdd(@Valid ReplyAddVo replyAddVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {

        //return gsonUtil.toJson(new JsonOutputVo(Status.이벤트_댓글달기실패_이벤트종료));

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_ReplyAddInputVo apiData = new P_ReplyAddInputVo();
        apiData.setMemLogin(DalbitUtil.isLogin(request) ? 1 : 0);
        apiData.setMem_no(MemberVo.getMyMemNo(request));
        apiData.setContents(replyAddVo.getContent());
        apiData.setDepth(replyAddVo.getDepth());
        apiData.setEvent_idx(replyAddVo.getEventIdx());

        String result = eventService.callEventReplyAdd(apiData);

        return result;
    }


    /**
     * 이벤트 댓글 삭제
     */
    @DeleteMapping("/reply")
    public String replyDelete(@Valid ReplyDeleteVo replyDeleteVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_ReplyDeleteInputVo apiData = new P_ReplyDeleteInputVo();
        apiData.setMem_no(MemberVo.getMyMemNo(request));
        apiData.setReply_idx(replyDeleteVo.getReplyIdx());
        apiData.setEvent_idx(replyDeleteVo.getEventIdx());

        String result = eventService.callEventReplyDelete(apiData);

        return result;
    }


    /**
     * 출석체크 이벤트 상태 체크
     */
    @PostMapping("/attendance/check/status")
    public String attendanceCheckStatus(HttpServletRequest request) throws GlobalException {

        String result = eventService.attendanceCheckStatus(request);

        return result;
    }

    /**
     * 출석체크 이벤트 참여
     */
    @PostMapping("/attendance/check/in")
    public String attendanceCheckIn(HttpServletRequest request) throws GlobalException {

        String result = eventService.attendanceCheckIn(request);

        return result;
    }

    /**
     * 출석체크 이벤트 더 줘 받기
     */
    @PostMapping("/attendance/random/gift")
    public String attendanceRandomGift(HttpServletRequest request) throws GlobalException {

        String result = eventService.attendanceRandomGift(request);

        return result;
    }


    /**
     * 라이징 이벤트 실시간 순위보기
     */
    @PostMapping("/rising/live")
    public String risingLive(HttpServletRequest request, P_RisingLiveInputVo pRisingLiveInputVo) throws GlobalException {
        String result = eventService.callRisingLive(request, pRisingLiveInputVo);
        return result;
    }


    /**
     * 라이징 이벤트 결과보기
     */
    @PostMapping("/rising/result")
    public String risingResult(HttpServletRequest request, P_RisingResultInputVo pRisingResultInputVo) throws GlobalException {
        String result = eventService.callRisingResult(request, pRisingResultInputVo);
        return result;
    }


    /**
     * 출석완료 체크
     */
    @GetMapping("/attendance/check")
    public String attendanceCheck(HttpServletRequest request) {
        P_AttendanceCheckVo apiData = new P_AttendanceCheckVo(request);
        String result = eventService.callAttendanceCheck(request, apiData);

        return result;
    }


    /**
     * 당첨자 휴대폰 입력
     */
    @PostMapping("/phone/input")
    public String phoneInput(@Valid PhoneInputVo phoneInputVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_PhoneInputVo apiData = new P_PhoneInputVo(phoneInputVo, request);
        String result = eventService.callPhoneInput(apiData);

        return result;
    }


    /**
     * 기프티콘 당첨자 리스트 조회
     */
    @GetMapping("/gifticon/win/list")
    public String gifticonWinList(HttpServletRequest request, P_GifticonWinListInputVo pGifticonWinListInputVo) {
        pGifticonWinListInputVo.setMem_no(MemberVo.getMyMemNo(request));
        String result = eventService.callGifticonWinList(pGifticonWinListInputVo);

        return result;
    }

    /**
     * 보름달 뜨는 날짜 조회
     */
    @GetMapping("/lunar/date")
    public String getLunarDate() {

        String result = eventService.selectLunarDate();

        return result;
    }

    /**
     * 인증샷 이벤트 참여자 목록 조회
     */
    @PostMapping("/photo/list")
    public String selectPhotoList(HttpServletRequest request, PhotoEventInputVo photoEventInputVo) {

        String result = eventService.selectPhotoList(request, photoEventInputVo);

        return result;
    }

    /**
     * 인증샷 이벤트 참여
     */
    @PostMapping("/photo/insert")
    public String insertPhoto(HttpServletRequest request, @Valid PhotoEventInputVo photoEventInputVo, BindingResult bindingResult) throws GlobalException {

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        String result = eventService.insertPhoto(request, photoEventInputVo);

        return result;
    }

    /**
     * 인증샷 이벤트 내용 수정
     */
    @PostMapping("/photo/update")
    public String updatePhoto(HttpServletRequest request, @Valid PhotoEventInputVo photoEventInputVo, BindingResult bindingResult) throws GlobalException {

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        String result = eventService.updatePhoto(request, photoEventInputVo);

        return result;
    }

    /**
     * 인증샷 이벤트 내용 수정
     */
    @PostMapping("/photo/delete")
    public String deletePhoto(HttpServletRequest request, PhotoEventInputVo photoEventInputVo) {

        photoEventInputVo.setEvent_idx(EventCode.인증샷.getEventIdx());
        String result = eventService.deletePhoto(request, photoEventInputVo);

        return result;
    }

    /**
     * 인증샷 이벤트 참여여부
     */
    @GetMapping("/photo/status")
    public String statusPhoto(HttpServletRequest request, PhotoEventInputVo photoEventInputVo) {

        String result = eventService.statusPhoto(request, photoEventInputVo);

        return result;
    }

    /**
     * 노하우 이벤트 목록
     */
    @PostMapping("/knowhow/list")
    public String selectKnowhowList(HttpServletRequest request, KnowhowEventInputVo knowhowEventInputVo) {

        String result = eventService.selectKnowhowList(request, knowhowEventInputVo);

        return result;
    }

    /**
     * 노하우 이벤트 등록
     */
    @PostMapping("/knowhow/insert")
    public String insertKnowhow(HttpServletRequest request, @Valid KnowhowEventInputVo knowhowEventInputVo, BindingResult bindingResult) throws GlobalException {

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        String result = eventService.insertKnowhow(request, knowhowEventInputVo);

        return result;
    }

    /**
     * 노하우 이벤트 수정
     */
    @PostMapping("/knowhow/update")
    public String updateKnowhow(HttpServletRequest request, @Valid KnowhowEventInputVo knowhowEventInputVo, BindingResult bindingResult) throws GlobalException {

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        String result = eventService.updateKnowhow(request, knowhowEventInputVo);

        return result;
    }

    /**
     * 노하우 이벤트 조회
     */
    @PostMapping("/knowhow/detail")
    public String detailKnowhow(HttpServletRequest request, @Valid KnowhowEventInputVo knowhowEventInputVo, BindingResult bindingResult) throws GlobalException {

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        String result = eventService.detailKnowhow(request, knowhowEventInputVo);

        return result;
    }

    @PostMapping("/knowhow/delete")
    public String deleteKnowhow(HttpServletRequest request, PhotoEventInputVo photoEventInputVo) {

        photoEventInputVo.setEvent_idx(EventCode.노하우.getEventIdx());
        String result = eventService.deletePhoto(request, photoEventInputVo);

        return result;
    }

    /**
     * 이벤트 좋아요
     */
    @PostMapping("/good")
    public String eventGood(HttpServletRequest request, @Valid EventGoodVo eventGoodVo, BindingResult bindingResult) throws GlobalException {

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        String result = eventService.eventGood(request, eventGoodVo);

        return result;
    }

    /**
     * 이벤트 체크
     */
    @GetMapping("/apply")
    public String eventApplyCheck(HttpServletRequest request, @Valid CheckVo checkVo, BindingResult bindingResult) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        HashMap result = eventService.eventCheck(checkVo, request);

        return gsonUtil.toJson(new JsonOutputVo((Status) result.get("status"), result.get("data")));
    }

    /**
     * 이벤트 체크 방송 지원
     */
    @GetMapping("/004/apply")
    public String eventApplyCheck004(HttpServletRequest request, @Valid CheckVo checkVo, BindingResult bindingResult) throws GlobalException {
        //DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        checkVo.setEventIdx(4);
        HashMap result = eventService.eventCheck004(checkVo, request);

        return gsonUtil.toJson(new JsonOutputVo((Status) result.get("status"), result.get("data")));
    }

    /**
     * 이벤트 참여
     */
    @PostMapping("/apply")
    public String eventApply(HttpServletRequest request, @Valid ApplyVo applyVo, BindingResult bindingResult) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        HashMap result = eventService.eventApply(applyVo, request);

        return gsonUtil.toJson(new JsonOutputVo((Status) result.get("status"), result.get("data")));
    }

    /**
     * 이벤트 참여
     */
    @PostMapping("/004/apply")
    public String eventApply004(HttpServletRequest request, @Valid Apply004Vo applyVo, BindingResult bindingResult) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        HashMap result = eventService.eventApply004(applyVo, request);

        return gsonUtil.toJson(new JsonOutputVo((Status) result.get("status"), result.get("data")));
    }

    /**
     * 이벤트 리스트 조회
     */
    @GetMapping("/page/list")
    public String eventPageList(HttpServletRequest request, P_EventPageListInputVo pEventPageListInputVo) {
        pEventPageListInputVo.setMem_no(MemberVo.getMyMemNo(request));
        String result = eventService.callEventPageList(pEventPageListInputVo);

        return result;
    }

    /**
     * 이벤트 당첨자 명단 조회
     */
    @PostMapping("/page/winList")
    public String eventPageWinList(HttpServletRequest request, @Valid EventPageWinVo eventPageWinVo, BindingResult bindingResult) throws GlobalException {

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_EventPageWinListInputVo apiData = new P_EventPageWinListInputVo(eventPageWinVo, request);
        String result = eventService.callEventPageWinList(apiData);

        return result;
    }

    /**
     * 이벤트 당첨 여부 조회
     */
    @PostMapping("/page/winResult")
    public String eventPageWinResult(HttpServletRequest request, @Valid EventPageWinVo eventPageWinVo, BindingResult bindingResult) throws GlobalException {

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_EventPageWinResultInputVo apiData = new P_EventPageWinResultInputVo(eventPageWinVo, request);
        String result = eventService.callEventPageWinResult(apiData);

        return result;
    }

    /**
     * 이벤트 당첨자 경품 수령방법 선택
     */
    @PostMapping("/page/receiveWay")
    public String eventPagePrizeReceiveWay(HttpServletRequest request, @Valid EventPagePrizeReceiveVo eventPagePrizeReceiveVo, BindingResult bindingResult) throws GlobalException {

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_EventPagePrizeReceiveVo apiData = new P_EventPagePrizeReceiveVo(eventPagePrizeReceiveVo, request);
        String result = eventService.callEventPagePrizeReceiveWay(apiData);

        return result;
    }

    /**
     * 이벤트 당첨자 등록정보 조회
     */
    @PostMapping("/page/winnerAddInfo/select")
    public String eventPageWinnerAddInfoSelect(HttpServletRequest request,@Valid EventPageWinnerAddInfoListVo eventPageWinnerAddInfoListVo, BindingResult bindingResult) throws GlobalException {

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_EventPageWinnerAddInfoListInputVo apiData = new P_EventPageWinnerAddInfoListInputVo(eventPageWinnerAddInfoListVo, request);
        String result = eventService.callEventPageWinnerAddInfoSelect(apiData);

        return result;
    }

    /**
     * 이벤트 당첨자 이름/핸드폰 번호 조회
     */
    @GetMapping("/page/winnerAddInfo/infoFormat")
    public String eventPageWinnerInfoFormat(P_EventPageWinnerInfoFormatVo pEventPageWinnerInfoFormatVo){
        String result = eventService.callEventPageWinnerInfoFormat(pEventPageWinnerInfoFormatVo);
        return result;
    }

    /**
     * 이벤트 당첨자 등록정보 등록/수정
     */
    @PostMapping("/page/winnerAddInfo/edit")
    public String eventPageWinnerAddInfoEdit(HttpServletRequest request, @Valid EventPageWinnerAddInfoEditVo eventPageWinnerAddInfoEditVo, BindingResult bindingResult) throws GlobalException {

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_EventPageWinnerAddInfoEditVo apiData = new P_EventPageWinnerAddInfoEditVo(eventPageWinnerAddInfoEditVo, request);
        String result = eventService.callEventPageWinnerAddInfoEdit(apiData, request);

        return result;
    }

    @PostMapping("/timeEvent/info")
    public String timeEventInfo(HttpServletRequest request, TimeEventVo timeEventVo){
        String result = eventService.selectTimeEventInfo(timeEventVo);
        return result;
    }

    /**
     * 오픈 기념 이벤트
     */
    @GetMapping("")
    public String openEvent(@Valid OpenEventVo openEventVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_OpenEventVo apiData = new P_OpenEventVo(openEventVo, request);
        String result = eventService.callOpenEvent(apiData, request);
        return result;
    }

    /**
     * 일간 최고 DJ/FAN 보기
     */
    @GetMapping("/best")
    public String openEventDailyBest(@Valid OpenEventVo openEventVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_OpenEventBestListVo apiData = new P_OpenEventBestListVo(openEventVo, request);
        String result = eventService.callOpenEventDailyBest(apiData);
        return result;
    }

    /**
     * 스페셜 리그 조회
     */
    @GetMapping("/special")
    public String specialLeague(@Valid SpecialLeagueVo specialLeagueVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_SpecialLeagueVo apiData = new P_SpecialLeagueVo(specialLeagueVo, request);
        String result = eventService.callSpecialLeague(apiData);
        return result;
    }

    /**
     * 챔피언십 조회
     */
    @GetMapping("/championship")
    public String championshipSelect(@Valid ChampionshipVo championshipVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_ChampionshipVo apiData = new P_ChampionshipVo(championshipVo, request);
        String result = eventService.callChampionshipSelect(apiData);
        return result;
    }

    /**
     * 챔피언십 승점 현황 조회
     */
    @GetMapping("/championship/point")
    public String championshipPointSelect(@Valid ChampionshipPointVo championshipPointVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_ChampionshipPointVo apiData = new P_ChampionshipPointVo(championshipPointVo, request);
        String result = eventService.callChampionshipPointSelect(apiData);
        return result;
    }

    /**
     * 챔피언십 선물받기(부스터)
     */
    @PostMapping("/championship/gift")
    public String championshipGift(HttpServletRequest request){
        P_ChampionshipGiftVo apiData = new P_ChampionshipGiftVo(request);
        String result = eventService.callChampionshipGift(apiData);
        return result;
    }

}
