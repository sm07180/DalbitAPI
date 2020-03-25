package com.dalbit.broadcast.service;

import com.dalbit.broadcast.dao.ContentDao;
import com.dalbit.broadcast.vo.RoomStoryListOutVo;
import com.dalbit.broadcast.vo.procedure.*;
import com.dalbit.common.code.Status;
import com.dalbit.common.vo.*;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.socket.service.SocketService;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
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


    /**
     *  방송방 공지사항 조회
     */
    public String callBroadCastRoomNoticeSelect(P_RoomNoticeVo pRoomNoticeSelectVo) {
        ProcedureVo procedureVo = new ProcedureVo(pRoomNoticeSelectVo);
        contentDao.callBroadCastRoomNoticeSelect(procedureVo);

        HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
        String notice = DalbitUtil.getStringMap(resultMap, "notice");

        log.info("프로시저 응답 코드: {}", procedureVo.getRet());
        log.info("프로시저 응답 데이타: {}", procedureVo.getExt());

        HashMap returnMap = new HashMap();
        returnMap.put("notice", notice);
        procedureVo.setData(returnMap);

        String result ="";
        if(procedureVo.getRet().equals(Status.공지가져오기성공.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.공지가져오기성공, procedureVo.getData()));
        } else if(procedureVo.getRet().equals(Status.공지가져오기실패_정상회원이아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.공지가져오기실패_정상회원이아님));
        } else if(procedureVo.getRet().equals(Status.공지가져오기실패_해당방이없음.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.공지가져오기실패_정상회원이아님));
        } else if(procedureVo.getRet().equals(Status.공지가져오기실패_방참가자가아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.공지가져오기실패_방참가자가아님));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.공지가져오기실패_조회에러));
        }
         return result;
    }


    /**
     *  방송방 공지사항 입력/수정
     */
    public String callBroadCastRoomNoticeEdit(P_RoomNoticeEditVo pRoomNoticeEditVo, HttpServletRequest request) {
        ProcedureVo procedureVo = new ProcedureVo(pRoomNoticeEditVo);
        contentDao.callBroadCastRoomNoticeEdit(procedureVo);

        String result;
        if(procedureVo.getRet().equals(Status.공지입력수정성공.getMessageCode())) {
            try{
                socketService.sendNotice(pRoomNoticeEditVo.getRoom_no(), MemberVo.getMyMemNo(request), pRoomNoticeEditVo.getNotice(), DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request));
            }catch(Exception e){
                log.info("Socket Service sendNotice Exception {}", e);
            }
            result = gsonUtil.toJson(new JsonOutputVo(Status.공지입력수정성공));
        } else if(procedureVo.getRet().equals(Status.공지입력수정실패_정상회원이아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.공지입력수정실패_정상회원이아님));
        } else if(procedureVo.getRet().equals(Status.공지입력수정실패_해당방이없음.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.공지입력수정실패_해당방이없음));
        } else if(procedureVo.getRet().equals(Status.공지입력수정실패_방참가자가아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.공지입력수정실패_방참가자가아님));
        } else if(procedureVo.getRet().equals(Status.공지입력수정실패_공지권한없음.getMessageCode())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.공지입력수정실패_공지권한없음));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.공지입력수정실패_입력수정에러));
        }

        return result;
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
                socketService.sendNotice(pRoomNoticeSelectVo.getRoom_no(), MemberVo.getMyMemNo(request), "", DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request));
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
    public String callInsertStory(P_RoomStoryAddVo pRoomStoryAddVo, HttpServletRequest request) {
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
                socketService.sendStory(pRoomStoryAddVo.getRoom_no(), MemberVo.getMyMemNo(request), socketMap, DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request));
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
        }else if(Status.방송방사연등록_10분에한번등록가능.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송방사연등록_10분에한번등록가능, returnMap));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송방사연등록오류, returnMap));
        }

        return result;
    }


    /**
     * 방송방 사연 조회
     */
    public String callGetStory(P_RoomStoryListVo pRoomStoryListVo) {

        ProcedureVo procedureVo = new ProcedureVo(pRoomStoryListVo);
        List<P_RoomStoryListVo> storyVoList = contentDao.callGetStory(procedureVo);

        HashMap storyList = new HashMap();
        if(DalbitUtil.isEmpty(storyVoList)){
            storyList.put("list", new ArrayList<>());
            return gsonUtil.toJson(new JsonOutputVo(Status.방송방사연조회_등록된사연없음, storyList));
        }

        List<RoomStoryListOutVo> outVoList = new ArrayList<>();
        for (int i=0; i<storyVoList.size(); i++){
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
}
