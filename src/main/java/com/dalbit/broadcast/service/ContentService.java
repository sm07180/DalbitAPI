package com.dalbit.broadcast.service;

import com.dalbit.broadcast.dao.ContentDao;
import com.dalbit.broadcast.dao.RoomDao;
import com.dalbit.broadcast.vo.BroadcastNoticeAddVo;
import com.dalbit.broadcast.vo.BroadcastNoticeUpdVo;
import com.dalbit.broadcast.vo.RoomStoryListOutVo;
import com.dalbit.broadcast.vo.procedure.*;
import com.dalbit.common.code.Status;
import com.dalbit.common.service.CommonService;
import com.dalbit.common.vo.*;
import com.dalbit.member.dao.MypageDao;
import com.dalbit.member.vo.*;
import com.dalbit.socket.service.SocketService;
import com.dalbit.socket.vo.SocketVo;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jose4j.lang.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Slf4j
@Service
public class ContentService {

    @Autowired
    ContentDao contentDao;
    @Autowired
    GsonUtil gsonUtil;
    @Autowired
    SocketService socketService;
    @Autowired
    CommonService commonService;
    @Autowired
    MypageDao mypageDao;
    @Autowired
    RoomDao roomDao;

    /**
     *  방송방 공지사항 조회(입장시)
     */
//    public String callBroadCastRoomNoticeSelect(P_RoomNoticeVo pRoomNoticeSelectVo) {
//        ProcedureVo procedureVo = new ProcedureVo(pRoomNoticeSelectVo);
//        contentDao.callBroadCastRoomNoticeSelect(procedureVo);
//
//        HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
//        String notice = DalbitUtil.getStringMap(resultMap, "notice");
//
//        log.info("프로시저 응답 코드: {}", procedureVo.getRet());
//        log.info("프로시저 응답 데이타: {}", procedureVo.getExt());
//
//        HashMap returnMap = new HashMap();
//        //사이트+방송방 금지어 조회 공지사항 마스킹 처리
//        BanWordVo banWordVo = new BanWordVo();
//        String systemBanWord = commonService.banWordSelect();
//        banWordVo.setRoomNo(pRoomNoticeSelectVo.getRoom_no());
//        String banWord = commonService.broadcastBanWordSelect(banWordVo);
//        if(!DalbitUtil.isEmpty(banWord)){
//            notice = DalbitUtil.replaceMaskString(systemBanWord+"|"+banWord, notice);
//        }else if(!DalbitUtil.isEmpty(systemBanWord)){
//            notice = DalbitUtil.replaceMaskString(systemBanWord, notice);
//        }
//        returnMap.put("notice", notice);
//        procedureVo.setData(returnMap);
//
//        String result ="";
//        if(procedureVo.getRet().equals(Status.공지가져오기성공.getMessageCode())) {
//            result = gsonUtil.toJson(new JsonOutputVo(Status.공지가져오기성공, procedureVo.getData()));
//        } else if(procedureVo.getRet().equals(Status.공지가져오기실패_정상회원이아님.getMessageCode())) {
//            result = gsonUtil.toJson(new JsonOutputVo(Status.공지가져오기실패_정상회원이아님));
//        } else if(procedureVo.getRet().equals(Status.공지가져오기실패_해당방이없음.getMessageCode())) {
//            result = gsonUtil.toJson(new JsonOutputVo(Status.공지가져오기실패_정상회원이아님));
//        } else if(procedureVo.getRet().equals(Status.공지가져오기실패_방참가자가아님.getMessageCode())) {
//            result = gsonUtil.toJson(new JsonOutputVo(Status.공지가져오기실패_방참가자가아님));
//        } else {
//            result = gsonUtil.toJson(new JsonOutputVo(Status.공지가져오기실패_조회에러));
//        }
//         return result;
//    }

    public String mobileBroadcastNoticeSelect(BroadcastNoticeSelVo noticeSelVo, HttpServletRequest request) {
        HashMap paramMap = new HashMap();
        List<BroadcastNoticeListOutVo> noticeRow = null;
        Long memNo = Long.parseLong(MemberVo.getMyMemNo(request));

        int cnt = 0;

        paramMap.put("memNo", memNo);
        paramMap.put("roomNo", noticeSelVo.getRoomNo());
        noticeRow = mypageDao.pMemberBroadcastNoticeList(paramMap);

        HashMap resultMap = new HashMap();
        if(DalbitUtil.isEmpty(noticeRow)) {
            if(DalbitUtil.isEmpty(noticeRow)) {
                resultMap.put("notice", "");
                resultMap.put("paging", new PagingVo(cnt, DalbitUtil.getIntMap(paramMap, "pageNo"), DalbitUtil.getIntMap(paramMap, "pageCnt")));
                return gsonUtil.toJson(new JsonOutputVo(Status.공지조회_없음, resultMap));
            }
        }
        BanWordVo banWordVo = new BanWordVo();
        banWordVo.setMemNo(noticeSelVo.getMemNo());
        String systemBanWord = commonService.banWordSelect();
        String banWord = commonService.broadcastBanWordSelect(banWordVo);

        for(int i = 0; i < noticeRow.size(); i++) {
            if(!DalbitUtil.isEmpty(banWord)) {
                noticeRow.get(i).setConts(DalbitUtil.replaceMaskString(systemBanWord + "|" + banWord, noticeRow.get(i).getConts()));
            } else if(!DalbitUtil.isEmpty(systemBanWord)) {
                noticeRow.get(i).setConts(DalbitUtil.replaceMaskString(systemBanWord, noticeRow.get(i).getConts()));
            }
        }

        String notice = noticeRow.get(0).getConts();

        resultMap.put("notice", notice);
        return gsonUtil.toJson(new JsonOutputVo(Status.공지조회_성공, resultMap));
    }


    /**
     *  방송방 공지사항 입력/수정
     */
//    public String callBroadCastRoomNoticeEdit(P_RoomNoticeEditVo pRoomNoticeEditVo, HttpServletRequest request) {
//        ProcedureVo procedureVo = new ProcedureVo(pRoomNoticeEditVo);
//        contentDao.callBroadCastRoomNoticeEdit(procedureVo);
//
//        String result;
//        if(procedureVo.getRet().equals(Status.공지입력수정성공.getMessageCode())) {
//            try{
//                SocketVo vo = socketService.getSocketVo(pRoomNoticeEditVo.getRoom_no(), MemberVo.getMyMemNo(request), DalbitUtil.isLogin(request));
//                socketService.sendNotice(pRoomNoticeEditVo.getRoom_no(), MemberVo.getMyMemNo(request), pRoomNoticeEditVo.getNotice(), DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request), vo);
//                vo.resetData();
//            }catch(Exception e){
//                log.info("Socket Service sendNotice Exception {}", e);
//            }
//            result = gsonUtil.toJson(new JsonOutputVo(Status.공지입력수정성공));
//        } else if(procedureVo.getRet().equals(Status.공지입력수정실패_정상회원이아님.getMessageCode())) {
//            result = gsonUtil.toJson(new JsonOutputVo(Status.공지입력수정실패_정상회원이아님));
//        } else if(procedureVo.getRet().equals(Status.공지입력수정실패_해당방이없음.getMessageCode())) {
//            result = gsonUtil.toJson(new JsonOutputVo(Status.공지입력수정실패_해당방이없음));
//        } else if(procedureVo.getRet().equals(Status.공지입력수정실패_방참가자가아님.getMessageCode())) {
//            result = gsonUtil.toJson(new JsonOutputVo(Status.공지입력수정실패_방참가자가아님));
//        } else if(procedureVo.getRet().equals(Status.공지입력수정실패_공지권한없음.getMessageCode())){
//            result = gsonUtil.toJson(new JsonOutputVo(Status.공지입력수정실패_공지권한없음));
//        } else {
//            result = gsonUtil.toJson(new JsonOutputVo(Status.공지입력수정실패_입력수정에러));
//        }
//
//        return result;
//    }

    public String mobileBroadcastNoticeUpd(BroadcastNoticeUpdVo noticeUpdVo, HttpServletRequest request) {
        HashMap paramMap = new HashMap();
        List<BroadcastNoticeListOutVo> noticeRow = null;
        Long memNo = Long.parseLong(MemberVo.getMyMemNo(request));
        paramMap.put("memNo", memNo);
        noticeRow = mypageDao.pMemberBroadcastNoticeList(paramMap);

        if(noticeRow.size() != 0) {
            HashMap updMap = new HashMap();
            updMap.put("roomNoticeNo", noticeRow.get(0).getAuto_no());
            updMap.put("memNo", memNo);
            updMap.put("roomNo", noticeUpdVo.getRoomNo());
            updMap.put("notice", StringUtils.equals(noticeUpdVo.getNotice(), "") ? "default" : noticeUpdVo.getNotice());
            Integer result = contentDao.pMemberBroadcastNoticeUpd(updMap);
            if(result > 0) {
                try{
                    SocketVo vo = socketService.getSocketVo(noticeUpdVo.getRoomNo(), MemberVo.getMyMemNo(request), DalbitUtil.isLogin(request));
                    socketService.sendNotice(noticeUpdVo.getRoomNo(), MemberVo.getMyMemNo(request), noticeUpdVo.getNotice(), DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request), vo);
                    vo.resetData();
                } catch (Exception e) {
                    log.info("Socket Service sendNotice Exception {}", e);
                }
                return gsonUtil.toJson(new JsonOutputVo(Status.공지수정_성공));
            } else {
                return gsonUtil.toJson(new JsonOutputVo(Status.공지등록_실패));
            }
        } else {
            HashMap addMap = new HashMap();
            addMap.put("memNo", memNo);
            addMap.put("roomNo", noticeUpdVo.getRoomNo());
            addMap.put("notice", StringUtils.equals(noticeUpdVo.getNotice(), "") ? "default" : noticeUpdVo.getNotice());
            Integer result = contentDao.pMemberBroadcastNoticeIns(addMap);
            if(result > 0) {
                try{
                    SocketVo vo = socketService.getSocketVo(noticeUpdVo.getRoomNo(), MemberVo.getMyMemNo(request), DalbitUtil.isLogin(request));
                    socketService.sendNotice(noticeUpdVo.getRoomNo(), MemberVo.getMyMemNo(request), noticeUpdVo.getNotice(), DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request), vo);
                    vo.resetData();
                } catch (Exception e) {
                    log.info("Socket Service sendNotice Exception {}", e);
                }
                return gsonUtil.toJson(new JsonOutputVo(Status.공지등록_성공));
            } else {
                return gsonUtil.toJson(new JsonOutputVo(Status.공지등록_실패));
            }
        }
    }


    /**
     *  방송방 공지사항 삭제
     */
    public String callBroadCastRoomNoticeDelete(P_RoomNoticeVo pRoomNoticeSelectVo, HttpServletRequest request) {
        ProcedureVo procedureVo = new ProcedureVo(pRoomNoticeSelectVo);
        contentDao.callBroadCastRoomNoticeDelete(procedureVo);

        String result;
        if (procedureVo.getRet().equals(Status.공지삭제하기성공.getMessageCode())) {
            try{
                SocketVo vo = socketService.getSocketVo(pRoomNoticeSelectVo.getRoom_no(), MemberVo.getMyMemNo(request), DalbitUtil.isLogin(request));
                socketService.sendNotice(pRoomNoticeSelectVo.getRoom_no(), MemberVo.getMyMemNo(request), "", DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request), vo);
                vo.resetData();
            }catch(Exception e){
                log.info("Socket Service sendNotice Exception {}", e);
            }
            result = gsonUtil.toJson(new JsonOutputVo(Status.공지삭제하기성공));
        } else if (procedureVo.getRet().equals(Status.공지삭제하기실패_정상회원이아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.공지삭제하기실패_정상회원이아님));
        } else if (procedureVo.getRet().equals(Status.공지삭제하기실패_해당방이없음.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.공지삭제하기실패_해당방이없음));
        } else if (procedureVo.getRet().equals(Status.공지삭제하기실패_방참가자가아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.공지삭제하기실패_방참가자가아님));
        } else if (procedureVo.getRet().equals(Status.공지삭제하기실패_공지삭제권한없음)) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.공지삭제하기실패_공지삭제권한없음));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.공지삭제하기실패_삭제에러));
        }

        return result;

    }


    /**
     * 방송방 사연 등록
     */
    public String callInsertStory(P_RoomStoryAddVo pRoomStoryAddVo, HttpServletRequest request, boolean isOldVersion) {
        // -----------------------------사연 플러스 일감 -----------------------------
        /* 네이티브 업데이트 안한 경우 => plusYn, djMemNo 값 세팅 */
        if(isOldVersion) {
            HashMap returnMap = new HashMap();
            returnMap.put("passTime", 0);
            
            /* 방장 memNo 알아내기 */
            P_RoomInfoViewVo roomInfoVo = getRoomInfo(1, pRoomStoryAddVo.getMem_no(), pRoomStoryAddVo.getRoom_no());

            /* Fail or Exception */
            if(roomInfoVo.equals(null)){
                return gsonUtil.toJson(new JsonOutputVo(Status.방송방사연등록오류, returnMap));
            } else if (StringUtils.equals(roomInfoVo.getBj_mem_no(), "") || StringUtils.equals(roomInfoVo.getBj_mem_no(), null)) {
                log.error("ContentService.java / callInsertStory => roomInfoVo : {}", gsonUtil.toJson(roomInfoVo));
                return gsonUtil.toJson(new JsonOutputVo(Status.방송방사연등록오류, returnMap));
            }

            pRoomStoryAddVo.setDj_mem_no(roomInfoVo.getBj_mem_no());
        }

        /* param check */
        if (!(StringUtils.equals(pRoomStoryAddVo.getPlus_yn(), "n") || StringUtils.equals(pRoomStoryAddVo.getPlus_yn(), "y"))
            || StringUtils.equals(pRoomStoryAddVo.getDj_mem_no(), "")) {
            HashMap returnMap = new HashMap();
            returnMap.put("passTime", 0);
            return gsonUtil.toJson(new JsonOutputVo(Status.방송방사연등록_파라미터_에러, returnMap));
        }
        // -------------------------------------------------------------------------
        
        ProcedureVo procedureVo = new ProcedureVo(pRoomStoryAddVo);
        contentDao.callInsertStory(procedureVo);

        HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
        int passTime = DalbitUtil.getIntMap(resultMap, "passTime");
        log.info("프로시저 응답 코드: {}", procedureVo.getRet());
        log.info("프로시저 응답 데이타: {}", resultMap);
        log.info("passTime 추출: {}", passTime);
        log.info(" ### 프로시저 호출결과 ###");

        HashMap returnMap = new HashMap();
        returnMap.put("passTime", passTime);

        String result;
        if(Status.방송방사연등록성공.getMessageCode().equals(procedureVo.getRet())) {
            try{
                HashMap socketMap = new HashMap();
                socketMap.put("storyIdx", DalbitUtil.getIntMap(resultMap, "story_idx"));
                socketMap.put("writerNo", DalbitUtil.getStringMap(resultMap, "writer_mem_no"));
                socketMap.put("nickNm", DalbitUtil.getStringMap(resultMap, "nickName"));
                socketMap.put("profImg", new ImageVo(DalbitUtil.getStringMap(resultMap, "profileImage"), DalbitUtil.getStringMap(resultMap, "memSex"), DalbitUtil.getProperty("server.photo.url")));
                socketMap.put("contents", DalbitUtil.getStringMap(resultMap, "contents"));
                socketMap.put("writeDt", DalbitUtil.getUTCFormat((String)resultMap.get("writeDate")));
                socketMap.put("writeTs", DalbitUtil.getUTCTimeStamp((String)resultMap.get("writeDate")));
                SocketVo vo = socketService.getSocketVo(pRoomStoryAddVo.getRoom_no(), MemberVo.getMyMemNo(request), DalbitUtil.isLogin(request));
                socketService.sendStory(pRoomStoryAddVo.getRoom_no(), MemberVo.getMyMemNo(request), socketMap, DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request), vo);
                vo.resetData();
            }catch(Exception e){
                log.info("Socket Service sendStory Exception {}", e);
            }
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송방사연등록성공, returnMap));
        }else if(Status.방송방사연등록_회원아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송방사연등록_회원아님, returnMap));
        }else if(Status.방송방사연등록_해당방이없음.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송방사연등록_해당방이없음, returnMap));
        }else if(Status.방송방사연등록_방참가자가아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송방사연등록_방참가자가아님, returnMap));
        }else if(Status.방송방사연등록_1분에한번등록가능.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송방사연등록_1분에한번등록가능, returnMap));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송방사연등록오류, returnMap));
        }

        return result;
    }


    /**
     * 방송방 사연 조회
     */
    public String callGetStory(P_RoomStoryListVo pRoomStoryListVo, HttpServletRequest request) {

        ProcedureVo procedureVo = new ProcedureVo(pRoomStoryListVo);
        List<P_RoomStoryListVo> storyVoList = contentDao.callGetStory(procedureVo);

        HashMap storyList = new HashMap();
        if(DalbitUtil.isEmpty(storyVoList)){
            ProcedureOutputVo procedureOutputVo = new ProcedureOutputVo(procedureVo);
            storyList.put("list", new ArrayList<>());
            DeviceVo deviceVo = new DeviceVo(request);
            if(deviceVo.getOs() == 2){
                storyList.put("paging", new PagingVo(Integer.valueOf(procedureOutputVo.getRet()), pRoomStoryListVo.getPageNo(), pRoomStoryListVo.getPageCnt()));
            }else{
                storyList.put("paging", new PagingVo(0, pRoomStoryListVo.getPageNo(), pRoomStoryListVo.getPageCnt()));
            }
            return gsonUtil.toJson(new JsonOutputVo(Status.방송방사연조회_등록된사연없음, storyList));
        }

        List<RoomStoryListOutVo> outVoList = new ArrayList<>();
        String systemBanWord = commonService.banWordSelect();
        BanWordVo banWordVo = new BanWordVo();
        banWordVo.setRoomNo(pRoomStoryListVo.getRoom_no());
        String banWord = commonService.broadcastBanWordSelect(banWordVo);
        for (int i=0; i<storyVoList.size(); i++){
            //사이트+방송방 금지어 조회 사연 마스킹 처리
            if(!DalbitUtil.isEmpty(banWord)){
                storyVoList.get(i).setContents(DalbitUtil.replaceMaskString(systemBanWord+"|"+banWord, storyVoList.get(i).getContents()));
            }else if(!DalbitUtil.isEmpty(systemBanWord)){
                storyVoList.get(i).setContents(DalbitUtil.replaceMaskString(systemBanWord, storyVoList.get(i).getContents()));
            }
            outVoList.add(new RoomStoryListOutVo(storyVoList.get(i), pRoomStoryListVo.getMem_no()));
        }
        ProcedureOutputVo procedureOutputVo = new ProcedureOutputVo(procedureVo, outVoList);
        storyList.put("list", procedureOutputVo.getOutputBox());
        storyList.put("paging", new PagingVo(Integer.valueOf(procedureOutputVo.getRet()), pRoomStoryListVo.getPageNo(), pRoomStoryListVo.getPageCnt()));

        log.info("프로시저 응답 코드: {}", procedureOutputVo.getRet());
        log.info("프로시저 응답 데이타: {}", procedureOutputVo.getExt());
        log.info(" ### 프로시저 호출결과 ###");

        String result;
        if(Integer.parseInt(procedureOutputVo.getRet()) > 0) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송방사연조회성공, storyList));
        }else if(Status.방송방사연조회_회원아님.getMessageCode().equals(procedureOutputVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송방사연조회_회원아님));
        }else if(Status.방송방사연조회_해당방이없음.getMessageCode().equals(procedureOutputVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송방사연조회_해당방이없음));
        }else if(Status.방송방사연조회_방참가자가아님.getMessageCode().equals(procedureOutputVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송방사연조회_방참가자가아님));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송방사연조회오류));
        }
        return result;
    }


    /**
     * 방송방 사연 삭제
     */
    public String callDeleteStory(P_RoomStoryDeleteVo pRoomStoryDeleteVo) {
        ProcedureVo procedureVo = new ProcedureVo(pRoomStoryDeleteVo);
        contentDao.callDeletetStory(procedureVo);

        log.info("프로시저 응답 ret: {}", procedureVo.getRet());
        log.info("프로시저 응답 exit: {}", procedureVo.getExt());
        log.info("프로시저 응답 data: {}", procedureVo.getData());
        log.info(" ### 프로시저 호출결과 ###");

        String result;

        if(Status.방송방사연삭제성공.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송방사연삭제성공));
        }else if(Status.방송방사연삭제_회원아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송방사연삭제_회원아님));
        }else if(Status.방송방사연삭제_해당방이없음.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송방사연삭제_해당방이없음));
        }else if(Status.방송방사연삭제_방참가자가아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송방사연삭제_방참가자가아님));
        }else if(Status.방송방사연삭제_삭제권한없음.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송방사연삭제_삭제권한없음));
        }else if(Status.방송방사연삭제_사연인덱스오류.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송방사연삭제_사연인덱스오류));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송방사연삭제오류));
        }

        return result;
    }

    /**
     * 방 정보 조회하기
     * 사연 등록에 dj_mem_no 필요 (네이티브 업데이트 안한 경우에만 사용)
    * */
    public P_RoomInfoViewVo getRoomInfo(int isLogin, String reqMemNo, String roomNo){
        P_RoomInfoViewVo pRoomInfoViewVo = new P_RoomInfoViewVo();
        try {
            pRoomInfoViewVo.setMemLogin(isLogin);
            pRoomInfoViewVo.setMem_no(reqMemNo);
            pRoomInfoViewVo.setRoom_no(roomNo);
            ProcedureVo procedureInfoViewVo = new ProcedureVo(pRoomInfoViewVo);
            P_RoomInfoViewVo roomInfoViewVo = roomDao.callBroadCastRoomInfoView(procedureInfoViewVo);
            if (procedureInfoViewVo.getRet().equals(Status.방정보보기.getMessageCode())) {
                return roomInfoViewVo;
            } else {
                log.error("contentService.java / getRoomInfo Fail =>  param: {}", gsonUtil.toJson(pRoomInfoViewVo));
                return null;
            }
        }catch (Exception e){
            log.error("contentService.java / getRoomInfo Exception =>  param: {}, error: {}", gsonUtil.toJson(pRoomInfoViewVo), e);
            return null;
        }
    }
}
