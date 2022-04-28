package com.dalbit.event.controller;

import com.dalbit.common.code.EventCode;
import com.dalbit.common.code.Status;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.common.vo.ResMessage;
import com.dalbit.common.vo.ResVO;
import com.dalbit.event.service.DrawService;
import com.dalbit.event.service.EventService;
import com.dalbit.event.service.GoodStartService;
import com.dalbit.event.vo.*;
import com.dalbit.event.vo.ItemInsVo;
import com.dalbit.event.vo.KnowhowEventInputVo;
import com.dalbit.event.vo.PhotoEventInputVo;
import com.dalbit.event.vo.TimeEventVo;
import com.dalbit.event.vo.procedure.*;
import com.dalbit.event.vo.request.*;
import com.dalbit.exception.GlobalException;
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
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/event")
@Scope("prototype")
@Slf4j
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private GsonUtil gsonUtil;

    @Autowired
    private DrawService drawService;

    @Autowired GoodStartService goodStartService;

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

    /**
     * 아이템 지급[서비스]
     */
    @PostMapping("/raffle/dj/ins/item")
    public ResVO eventItemIns(@Valid ItemInsVo itemInsVo, HttpServletRequest request) {
        ResVO resVO = new ResVO();
        try {
            String memNo = MemberVo.getMyMemNo(request);
            itemInsVo.setMemNo(memNo);
            int insRes = eventService.eventItemIns(itemInsVo);
            switch (insRes) {
                case 1: resVO.setSuccessResVO(insRes); break;
                case -1: resVO.setResVO(ResMessage.C50001.getCode(), ResMessage.C50001.getCodeNM(), insRes); break;
                case -3: resVO.setResVO(ResMessage.C30003.getCode(), ResMessage.C30003.getCodeNM(), insRes); break;
                default: resVO.setFailResVO();
            }
        } catch (Exception e) {
            log.error("EventController / eventItemIns => {}", e);
            resVO.setFailResVO();
        }

        return resVO;
    }

    /* 깐부 이벤트~~~ */
    /**********************************************************************************************
    * @Method 설명 : 깐부 이벤트 회차번호
    * @작성일   : 2021-12-01
    * @작성자   : 박성민
    * @변경이력  :
    **********************************************************************************************/
    @GetMapping("/gganbu/round/info")
    public ResVO getGganbuRoundInfo() {
        ResVO resVO = new ResVO();
        try {
            resVO.setSuccessResVO(eventService.getGganbuRoundInfo());
        } catch (Exception e) {
            log.error("EventController / gganbuRoundInfo => {}", e);
            resVO.setFailResVO();
        }

        return resVO;
    }

    /**********************************************************************************************
     * @Method 설명 : 깐부 신청
     * @작성일   : 2021-12-01
     * @작성자   : 박성민
     * @변경이력  :
     **********************************************************************************************/
    @PostMapping("/gganbu/relationship/req/ins")
    public ResVO gganbuMemReqIns(@Valid GganbuMemReqInsVo gganbuMemReqInsVo, HttpServletRequest request) {
        ResVO resVO = new ResVO();
        try {
            gganbuMemReqInsVo.setMemNo(MemberVo.getMyMemNo(request));
            int result = eventService.gganbuMemReqIns(gganbuMemReqInsVo);
            switch (result) {
                case -7: resVO.setResVO(ResMessage.C30010.getCode(), ResMessage.C30010.getCodeNM(), result); break;
                case -6: resVO.setResVO(ResMessage.C30011.getCode(), ResMessage.C30011.getCodeNM(), result); break;
                case -5: resVO.setResVO(ResMessage.C30012.getCode(), ResMessage.C30012.getCodeNM(), result); break;
                case -4: resVO.setResVO(ResMessage.C30004.getCode(), ResMessage.C30004.getCodeNM(), result); break;
                case -3: resVO.setResVO(ResMessage.C30005.getCode(), ResMessage.C30005.getCodeNM(), result); break;
                case -2: resVO.setResVO(ResMessage.C30006.getCode(), ResMessage.C30006.getCodeNM(), result); break;
                case -1: resVO.setResVO(ResMessage.C30007.getCode(), ResMessage.C30007.getCodeNM(), result); break;
                case 0: resVO.setResVO(ResMessage.C99997.getCode(), ResMessage.C99997.getCodeNM(), result); break;
                case 1: resVO.setSuccessResVO(result); break;
                default: resVO.setFailResVO();
            }
        } catch (Exception e) {
            log.error("EventController / gganbuRoundInfo => {}", e);
            resVO.setFailResVO();
        }

        return resVO;
    }

    /**********************************************************************************************
     * @Method 설명 : 깐부 신청 취소
     * @작성일   : 2021-12-01
     * @작성자   : 박성민
     * @변경이력  :
     **********************************************************************************************/
    @PostMapping("/gganbu/relationship/req/cancel")
    public ResVO gganbuMemReqCancel(@Valid GganbuMemReqCancelVo gganbuMemReqCancelVo, HttpServletRequest request) {
        ResVO resVO = new ResVO();
        try {
            gganbuMemReqCancelVo.setMemNo(MemberVo.getMyMemNo(request));
            int result = eventService.gganbuMemReqCancel(gganbuMemReqCancelVo);
            switch (result) {
                case -3: resVO.setResVO(ResMessage.C30008.getCode(), ResMessage.C30008.getCodeNM(), result); break;
                case -2: resVO.setResVO(ResMessage.C30009.getCode(), ResMessage.C30009.getCodeNM(), result); break;
                case -1: resVO.setResVO(ResMessage.C30007.getCode(), ResMessage.C30007.getCodeNM(), result); break;
                case 0: resVO.setResVO(ResMessage.C99997.getCode(), ResMessage.C99997.getCodeNM(), result); break;
                case 1: resVO.setSuccessResVO(result); break;
                default: resVO.setFailResVO();
            }
        } catch (Exception e) {
            log.error("EventController / gganbuMemReqCancel => {}", e);
            resVO.setFailResVO();
        }

        return resVO;
    }

    /**********************************************************************************************
     * @Method 설명 : 깐부 신청 리스트
     * @작성일   : 2021-12-01
     * @작성자   : 박성민
     * @변경이력  :
     **********************************************************************************************/
    @PostMapping("/gganbu/relationship/list")
    public ResVO gganbuMemReqList(@Valid GganbuMemReqListInputVo gganbuMemReqListInputVo, HttpServletRequest request) {
        ResVO resVO = new ResVO();
        try {
            gganbuMemReqListInputVo.setMemNo(MemberVo.getMyMemNo(request));
            resVO.setSuccessResVO(eventService.gganbuMemReqList(gganbuMemReqListInputVo));
        } catch (Exception e) {
            log.error("EventController / gganbuMemReqList => {}", e);
            resVO.setFailResVO();
        }

        return resVO;
    }

    /**********************************************************************************************
     * @Method 설명 : 깐부 신청 수락
     * @작성일   : 2021-12-01
     * @작성자   : 박성민
     * @변경이력  :
     **********************************************************************************************/
    @PostMapping("/gganbu/relationship/ins")
    public ResVO gganbuMemIns(@Valid GganbuMemInsVo gganbuMemInsVo, HttpServletRequest request) {
        ResVO resVO = new ResVO();
        try {
            gganbuMemInsVo.setPtrMemNo(MemberVo.getMyMemNo(request));
            int result = eventService.gganbuMemIns(gganbuMemInsVo);
            switch (result) {
                case -5: resVO.setResVO(ResMessage.C30008.getCode(), ResMessage.C30008.getCodeNM(), result); break;
                case -4:
                case -3: resVO.setResVO(ResMessage.C30004.getCode(), ResMessage.C30004.getCodeNM(), result); break;
                case -2: resVO.setResVO(ResMessage.C30006.getCode(), ResMessage.C30006.getCodeNM(), result); break;
                case -1: resVO.setResVO(ResMessage.C30007.getCode(), ResMessage.C30007.getCodeNM(), result); break;
                case 0: resVO.setResVO(ResMessage.C99997.getCode(), ResMessage.C99997.getCodeNM(), result); break;
                case 1: resVO.setSuccessResVO(result); break;
                default: resVO.setFailResVO();
            }
        } catch (Exception e) {
            log.error("EventController / gganbuMemIns => {}", e);
            resVO.setFailResVO();
        }

        return resVO;
    }

    /**********************************************************************************************
     * @Method 설명 : 깐부 여부 체크
     * @작성일   : 2021-12-02
     * @작성자   : 박성민
     * @변경이력  :
     **********************************************************************************************/
    @PostMapping("/gganbu/relationship/chk")
    public ResVO gganbuMemChk(@Valid GganbuMemChkVo gganbuMemChkVo, HttpServletRequest request) {
        ResVO resVO = new ResVO();
        try {
            gganbuMemChkVo.setMemNo(MemberVo.getMyMemNo(request));
            int result = eventService.gganbuMemChk(gganbuMemChkVo);
            switch (result) {
                case -1: resVO.setResVO(ResMessage.C30013.getCode(), ResMessage.C30013.getCodeNM(), result); break;
                case 0: resVO.setResVO(ResMessage.C99997.getCode(), ResMessage.C99997.getCodeNM(), result); break;
                case 1: resVO.setSuccessResVO(result); break;
                default: resVO.setFailResVO();
            }
        } catch (Exception e) {
            log.error("EventController / gganbuMemChk => {}", e);
            resVO.setFailResVO();
        }

        return resVO;
    }

    /**********************************************************************************************
     * @Method 설명 : 깐부 정보
     * @작성일   : 2021-12-02
     * @작성자   : 박성민
     * @변경이력  :
     **********************************************************************************************/
    @PostMapping("/gganbu/relationship/sel")
    public ResVO gganbuMemSel(@Valid GganbuMemSelInputVo gganbuMemSelInputVo, HttpServletRequest request) {
        ResVO resVO = new ResVO();
        try {
            gganbuMemSelInputVo.setMemNo(MemberVo.getMyMemNo(request));
            resVO.setSuccessResVO(eventService.gganbuMemSel(gganbuMemSelInputVo));
        } catch (Exception e) {
            log.error("EventController / gganbuMemSel => {}", e);
            resVO.setFailResVO();
        }

        return resVO;
    }

    /**********************************************************************************************
     * @Method 설명 : 깐부 구슬 획득
     * @작성일   : 2021-12-02
     * @작성자   : 박성민
     * @변경이력  :
     **********************************************************************************************/
    @PostMapping("/gganbu/marble/ins")
    public ResVO gganbuMemMarbleIns(@Valid GganbuMemMarbleInsInputVo gganbuMemMarbleInsInputVo, HttpServletRequest request) {
        ResVO resVO = new ResVO();
        try {
            gganbuMemMarbleInsInputVo.setMemNo(MemberVo.getMyMemNo(request));
            GganbuMemMarbleInsInputVo result = eventService.gganbuMemMarbleIns(gganbuMemMarbleInsInputVo);

            switch (result.getS_return()) {
                case -6: resVO.setResVO(ResMessage.C30018.getCode(), ResMessage.C30018.getCodeNM(), result); break;
                case -5: resVO.setResVO(ResMessage.C30017.getCode(), ResMessage.C30017.getCodeNM(), result); break;
                case -4: resVO.setResVO(ResMessage.C30016.getCode(), ResMessage.C30016.getCodeNM(), result); break;
                case -3: resVO.setResVO(ResMessage.C30014.getCode(), ResMessage.C30014.getCodeNM(), result); break;
                case -2: resVO.setResVO(ResMessage.C30013.getCode(), ResMessage.C30013.getCodeNM(), result); break;
                case -1: resVO.setResVO(ResMessage.C30007.getCode(), ResMessage.C30007.getCodeNM(), result); break;
                case 0: resVO.setResVO(ResMessage.C99997.getCode(), ResMessage.C99997.getCodeNM(), result); break;
                case 1: resVO.setSuccessResVO(result); break;
                default: resVO.setFailResVO();
            }
        } catch (Exception e) {
            log.error("EventController / gganbuMemMarbleIns => {}", e);
            resVO.setFailResVO();
        }

        return resVO;
    }

    /**********************************************************************************************
     * @Method 설명 : 깐부 구슬 리포트
     * @작성일   : 2021-12-01
     * @작성자   : 박성민
     * @변경이력  :
     **********************************************************************************************/
    @PostMapping("/gganbu/report/list")
    public ResVO gganbuMemMarbleLogList(@Valid GganbuMemMarbleLogListInputVo gganbuMemMarbleInsInputVo, HttpServletRequest request) {
        ResVO resVO = new ResVO();
        try {
            gganbuMemMarbleInsInputVo.setMemNo(MemberVo.getMyMemNo(request));
            resVO.setSuccessResVO(eventService.gganbuMemMarbleLogList(gganbuMemMarbleInsInputVo));
        } catch (Exception e) {
            log.error("EventController / gganbuMemMarbleLogList => {}", e);
            resVO.setFailResVO();
        }

        return resVO;
    }

    /**********************************************************************************************
     * @Method 설명 : 깐부 구슬모으기 페이지
     * @작성일   : 2021-12-01
     * @작성자   : 박성민
     * @변경이력  :
     **********************************************************************************************/
    @PostMapping("/gganbu/marble/gather")
    public ResVO gganbuMarbleGather(HttpServletRequest request) {
        ResVO resVO = new ResVO();
        try {
            String memNo = MemberVo.getMyMemNo(request);
            resVO.setSuccessResVO(eventService.gganbuMarbleGather(memNo));
        } catch (Exception e) {
            log.error("EventController / gganbuMarbleGather => {}", e);
            resVO.setFailResVO();
        }

        return resVO;
    }

    /**********************************************************************************************
     * @Method 설명 : 깐부 구슬 베팅 페이지
     * @작성일   : 2021-12-06
     * @작성자   : 박성민
     * @변경이력  :
     **********************************************************************************************/
    @GetMapping("/gganbu/marble/betting")
    public ResVO gganbuMarbleBetting(@Valid GganbuMarbleBettingPageInputVo gganbuMarbleBettingPageInputVo, HttpServletRequest request) {
        ResVO resVO = new ResVO();
        try {
            gganbuMarbleBettingPageInputVo.setMemNo(MemberVo.getMyMemNo(request));
            resVO.setSuccessResVO(eventService.gganbuMarbleBetting(gganbuMarbleBettingPageInputVo));
        } catch (Exception e) {
            log.error("EventController / gganbuMarbleBetting => {}", e);
            resVO.setFailResVO();
        }

        return resVO;
    }

    /**********************************************************************************************
     * @Method 설명 : 구슬 주머니 획득(구슬교환)
     * @작성일   : 2021-12-06
     * @작성자   : 박성민
     * @변경이력  : 자동지급으로 인해 안씀
     **********************************************************************************************/
    @PostMapping("/gganbu/pocket/get")
    public ResVO gganbuMarbleExchange(HttpServletRequest request) {
        ResVO resVO = new ResVO();
        try {
            String memNo = MemberVo.getMyMemNo(request);
            int result = eventService.gganbuMarbleExchange(memNo);
            switch (result) {
                case -3: resVO.setResVO(ResMessage.C30015.getCode(), ResMessage.C30015.getCodeNM(), result); break;
                case -2: resVO.setResVO(ResMessage.C30013.getCode(), ResMessage.C30013.getCodeNM(), result); break;
                case -1: resVO.setResVO(ResMessage.C30007.getCode(), ResMessage.C30007.getCodeNM(), result); break;
                case 0: resVO.setResVO(ResMessage.C99997.getCode(), ResMessage.C99997.getCodeNM(), result); break;
                case 1: resVO.setSuccessResVO(result); break;
                default: resVO.setFailResVO();
            }
        } catch (Exception e) {
            log.error("EventController / gganbuMarbleExchange => {}", e);
            resVO.setFailResVO();
        }

        return resVO;
    }

    /**********************************************************************************************
     * @Method 설명 : 구슬 주머니 오픈
     * @작성일   : 2021-12-06
     * @작성자   : 박성민
     * @변경이력  :
     **********************************************************************************************/
    @PostMapping("/gganbu/pocket/open")
    public ResVO gganbuMarblePocketOpenIns(@Valid GganbuMarblePocketOpenInsVo gganbuMarblePocketOpenInsVo, HttpServletRequest request) {
        ResVO resVO = new ResVO();
        try {
            gganbuMarblePocketOpenInsVo.setMemNo(MemberVo.getMyMemNo(request));
            Integer result = eventService.gganbuMarblePocketOpenIns(gganbuMarblePocketOpenInsVo);
            if(result == null) {
                resVO.setResVO(ResMessage.C30007.getCode(), ResMessage.C30007.getCodeNM(), result);
            }else {
                switch (result) {
                    case -2: resVO.setResVO(ResMessage.C30013.getCode(), ResMessage.C30013.getCodeNM(), result); break;
                    case -1: resVO.setResVO(ResMessage.C30007.getCode(), ResMessage.C30007.getCodeNM(), result); break;
                    case 0: resVO.setResVO(ResMessage.C99997.getCode(), ResMessage.C99997.getCodeNM(), result); break;
                    case 1: resVO.setSuccessResVO(result); break;
                    default: resVO.setFailResVO();
                }
            }
        } catch (Exception e) {
            log.error("EventController / gganbuMarblePocketOpenIns => {}", e);
            resVO.setFailResVO();
        }

        return resVO;
    }

    /**********************************************************************************************
     * @Method 설명 : 깐부 구슬 주머니 리포트
     * @작성일   : 2021-12-06
     * @작성자   : 박성민
     * @변경이력  :
     **********************************************************************************************/
    @GetMapping("/gganbu/pocket/report/list")
    public ResVO gganbuMarblePocketLogList(@Valid GganbuMarblePocketLogListInputVo gganbuMarblePocketLogListInputVo, HttpServletRequest request) {
        ResVO resVO = new ResVO();
        try {
            gganbuMarblePocketLogListInputVo.setMemNo(MemberVo.getMyMemNo(request));
            resVO.setSuccessResVO(eventService.gganbuMarblePocketLogList(gganbuMarblePocketLogListInputVo));
        } catch (Exception e) {
            log.error("EventController / gganbuMarblePocketLogList => {}", e);
            resVO.setFailResVO();
        }

        return resVO;
    }

    /**********************************************************************************************
     * @Method 설명 : 깐부 랭킹 리스트
     * @작성일   : 2021-12-06
     * @작성자   : 박성민
     * @변경이력  :
     **********************************************************************************************/
    @GetMapping("/gganbu/rank/list")
    public ResVO gganbuRankList(@Valid GganbuRankListInputVo gganbuRankListInputVo, HttpServletRequest request) {
        ResVO resVO = new ResVO();
        try {
            resVO.setSuccessResVO(eventService.gganbuRankList(gganbuRankListInputVo));
        } catch (Exception e) {
            log.error("EventController / gganbuRankList => {}", e);
            resVO.setFailResVO();
        }

        return resVO;
    }

    /**********************************************************************************************
     * @Method 설명 : 깐부 베팅 리스트
     * @작성일   : 2021-12-06
     * @작성자   : 박성민
     * @변경이력  :
     **********************************************************************************************/
    @GetMapping("/gganbu/betting/list")
    public ResVO gganbuBettingLogList(@Valid GganbuBettingLogListInputVo bettingLogListInputVo) {
        ResVO resVO = new ResVO();
        try {
            resVO.setSuccessResVO(eventService.gganbuBettingLogList(bettingLogListInputVo));
        } catch (Exception e) {
            log.error("EventController / gganbuBettingLogList => {}", e);
            resVO.setFailResVO();
        }

        return resVO;
    }

    /**********************************************************************************************
     * @Method 설명 : 깐부 나의 베팅 내역
     * @작성일   : 2021-12-06
     * @작성자   : 박성민
     * @변경이력  :
     **********************************************************************************************/
    @GetMapping("/gganbu/my/betting/list")
    public ResVO gganbuMyBettingLogSel(@Valid GganbuBettingLogListInputVo bettingLogListInputVo, HttpServletRequest request) {
        ResVO resVO = new ResVO();
        try {
            bettingLogListInputVo.setMemNo(MemberVo.getMyMemNo(request));
            resVO.setSuccessResVO(eventService.gganbuMyBettingLogSel(bettingLogListInputVo));
        } catch (Exception e) {
            log.error("EventController / gganbuMyBettingLogSel => {}", e);
            resVO.setFailResVO();
        }

        return resVO;
    }

    /**********************************************************************************************
     * @Method 설명 : 깐부 투표자 집계
     * @작성일   : 2021-12-07
     * @작성자   : 박성민
     * @변경이력  :
     **********************************************************************************************/
    @GetMapping("/gganbu/betting/stat/sel")
    public ResVO gganbuBettingStatSel(String gganbuNo) {
        ResVO resVO = new ResVO();
        try {
            resVO.setSuccessResVO(eventService.gganbuBettingStatSel(gganbuNo));
        } catch (Exception e) {
            log.error("EventController / gganbuBettingStatSel => {}", e);
            resVO.setFailResVO();
        }

        return resVO;
    }

    /**********************************************************************************************
     * @Method 설명 : 깐부 배지 초기화
     * @작성일   : 2021-12-07
     * @작성자   : 박성민
     * @변경이력  :
     **********************************************************************************************/
    @PostMapping("/gganbu/badge/upd")
    public ResVO gganbuMemBadgeUpd(@Valid GganbuMemBadgeUpdVo gganbuMemBadgeUpdVo, HttpServletRequest request) {
        ResVO resVO = new ResVO();
        try {
            gganbuMemBadgeUpdVo.setMemNo(MemberVo.getMyMemNo(request));
            int result = eventService.gganbuMemBadgeUpd(gganbuMemBadgeUpdVo);
            switch (result) {
                case -1: resVO.setResVO(ResMessage.C30007.getCode(), ResMessage.C30007.getCodeNM(), result); break;
                case 0: resVO.setResVO(ResMessage.C99997.getCode(), ResMessage.C99997.getCodeNM(), result); break;
                case 1: resVO.setSuccessResVO(result); break;
                default: resVO.setFailResVO();
            }
        } catch (Exception e) {
            log.error("EventController / gganbuMemBadgeUpd => {}", e);
            resVO.setFailResVO();
        }

        return resVO;
    }

    /**********************************************************************************************
     * @Method 설명 : 깐부 찾기
     * @작성일   : 2021-12-07
     * @작성자   : 박성민
     * @변경이력  :
     **********************************************************************************************/
    @GetMapping("/gganbu/member/search")
    public ResVO gganbuMemberSearch(@Valid GganbuMemberSearchInputVo gganbuMemberSearchInputVo, HttpServletRequest request) {
        ResVO resVO = new ResVO();
        try {
            gganbuMemberSearchInputVo.setMemNo(MemberVo.getMyMemNo(request));
            resVO.setSuccessResVO(eventService.gganbuMemberSearch(gganbuMemberSearchInputVo));
        } catch (Exception e) {
            log.error("EventController / gganbuMemberSearch => {}", e);
            resVO.setFailResVO();
        }

        return resVO;
    }

    /**********************************************************************************************
     * @Method 설명 : 구슬 주머니 페이지
     * @작성일   : 2021-12-08
     * @작성자   : 박성민
     * @변경이력  :
     **********************************************************************************************/
    @GetMapping("/gganbu/pocket/page")
    public ResVO gganbuPocketPage(@Valid GganbuPocketPageInputVo gganbuPocketPageInputVo, HttpServletRequest request) {
        ResVO resVO = new ResVO();
        try {
            gganbuPocketPageInputVo.setMemNo(MemberVo.getMyMemNo(request));
            resVO.setSuccessResVO(eventService.gganbuPocketPage(gganbuPocketPageInputVo));
        } catch (Exception e) {
            log.error("EventController / gganbuPocketPage => {}", e);
            resVO.setFailResVO();
        }

        return resVO;
    }

    /**********************************************************************************************
     * @Method 설명 : 깐부 찾기 (나의 팬)
     * @작성일   : 2021-12-08
     * @작성자   : 박성민
     * @변경이력  :
     **********************************************************************************************/
    @GetMapping("/gganbu/search/fan")
    public ResVO gganbuFanList(@Valid GganbuFanListVo gganbuFanListVo, HttpServletRequest request) {
        ResVO resVO = new ResVO();
        try {
            gganbuFanListVo.setMemNo(MemberVo.getMyMemNo(request));
            resVO.setSuccessResVO(eventService.gganbuFanList(gganbuFanListVo));
        } catch (Exception e) {
            log.error("EventController / gganbuFanList => {}", e);
            resVO.setFailResVO();
        }

        return resVO;
    }

    /* 깐부 이벤트 끝 ~~~ */

    /**********************************************************************************************
     * @Method 설명 : 추억의 뽑기(이벤트) 당첨내역 조회
     * @작성일 : 2021-12-06
     * @작성자 : 이정혁
     * @변경이력 :
     **********************************************************************************************/
    @GetMapping("/draw/winningInfo")
    public ResVO getDrawWinningInfo(HttpServletRequest request) {
        ResVO result = new ResVO();

        try {
            String memNo = MemberVo.getMyMemNo(request);

            if (memNo == null) {
                result.setResVO(ResMessage.C10001.getCode(), ResMessage.C10001.getCodeNM(), null);
            } else {
                result = drawService.getDrawWinningInfo(memNo);
            }
        } catch (Exception e) {
            log.error("EventController / getDrawTicketCnt => {}", e);
            result.setFailResVO();
        }

        return result;
    }

    /**********************************************************************************************
     * @Method 설명 : 추억의 뽑기(이벤트) 응모권 개수 조회
     * @작성일 : 2021-12-06
     * @작성자 : 이정혁
     * @변경이력 :
     **********************************************************************************************/
    @GetMapping("/draw/ticketCnt")
    public ResVO getDrawTicketCnt(HttpServletRequest request) {
        ResVO result = new ResVO();

        try {
            String memNo = MemberVo.getMyMemNo(request);

            if (memNo == null) {
                result.setResVO(ResMessage.C10001.getCode(), ResMessage.C10001.getCodeNM(), null);
            } else {
                result = drawService.getDrawTicketCnt(memNo);
            }
        } catch (Exception e) {
            log.error("EventController / getDrawTicketCnt => {}", e);
            result.setFailResVO();
        }

        return result;
    }

    /**********************************************************************************************
     * @Method 설명 : 추억의 뽑기(이벤트) 뽑기 리스트 조회
     * @작성일 : 2021-12-06
     * @작성자 : 이정혁
     * @변경이력 :
     **********************************************************************************************/
    @GetMapping("/draw/listInfo")
    public ResVO getDrawListInfo(HttpServletRequest request) {
        ResVO result = new ResVO();

        try {
            String memNo = MemberVo.getMyMemNo(request);

            if (memNo == null) {
                result.setResVO(ResMessage.C10001.getCode(), ResMessage.C10001.getCodeNM(), null);
            } else {
                result = drawService.getDrawListInfo(memNo);
            }
        } catch (Exception e) {
            log.error("EventController / getDrawListInfo => {}", e);
            result.setFailResVO();
        }

        return result;
    }

    /**********************************************************************************************
     * @Method 설명 : 추억의 뽑기(이벤트) 뽑기 시도
     * @작성일 : 2021-12-06
     * @작성자 : 이정혁
     * @변경이력 :
     **********************************************************************************************/
    @PostMapping("/draw/select")
    public ResVO putDrawSelect(@RequestParam Map<String, Object> param, HttpServletRequest request) {
        ResVO result = new ResVO();

        try {
            String memNo = MemberVo.getMyMemNo(request);
            LocalDateTime today = LocalDateTime.now();
            LocalDateTime endDay = LocalDateTime.parse("2021-12-17T00:00:00.000");

            if (!today.isBefore(endDay)) {
                result.setResVO(ResMessage.C30003.getCode(), ResMessage.C30003.getCodeNM(), null);
                return result;
            }

            if (memNo == null) {
                result.setResVO(ResMessage.C10001.getCode(), ResMessage.C10001.getCodeNM(), null);
            } else if (!param.containsKey("selectList")){
                result.setResVO(ResMessage.C10002.getCode(), ResMessage.C10002.getCodeNM(), null);
            } else {
                result = drawService.putDrawSelect(param, memNo);
            }
        } catch (Exception e) {
            log.error("EventController / getDrawTicketCnt => {}", e);
            result.setFailResVO();
        }

        return result;
    }

    /**********************************************************************************************
    * @Method 설명 : 굿스타트 이벤트 (DJ)
    * @작성일   : 2022-01-08
    * @작성자   : 박성민
    * @변경이력  :
    **********************************************************************************************/
    @GetMapping("/goodStart/dj/page")
    public ResVO goodStartDjPage(@Valid GoodStartInputVo goodStartInputVo, HttpServletRequest request) {
        String memNo = MemberVo.getMyMemNo(request);
        goodStartInputVo.setMemNo(memNo);

        return goodStartService.goodStartDjPage(goodStartInputVo);
    }

    // DJ 랭킹
    @GetMapping("/goodStart/dj/rank")
    public ResVO goodStartDjRank(@Valid GoodStartInputVo goodStartInputVo, HttpServletRequest request) {
        String memNo = MemberVo.getMyMemNo(request);
        goodStartInputVo.setMemNo(memNo);

        return goodStartService.goodStartDjRank(goodStartInputVo);
    }

    // 신입 DJ 랭킹
    @GetMapping("/goodStart/dj/new/rank")
    public ResVO goodStartDjNewRank(@Valid GoodStartInputVo goodStartInputVo, HttpServletRequest request) {
        String memNo = MemberVo.getMyMemNo(request);
        goodStartInputVo.setMemNo(memNo);

        return goodStartService.goodStartDjNewRank(goodStartInputVo);
    }

    /**********************************************************************************************
     * @Method 설명 : 굿스타트 이벤트 (Fan)
     * @작성일   : 2022-01-08
     * @작성자   : 박성민
     * @변경이력  :
     **********************************************************************************************/
    @GetMapping("/goodStart/fan/page")
    public ResVO goodStartFanPage(@Valid GoodStartInputVo goodStartInputVo, HttpServletRequest request) {
        String memNo = MemberVo.getMyMemNo(request);
        goodStartInputVo.setMemNo(memNo);

        return goodStartService.goodStartFanPage(goodStartInputVo);
    }
}
