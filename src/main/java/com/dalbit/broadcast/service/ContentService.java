package com.dalbit.broadcast.service;

import com.dalbit.broadcast.dao.ContentDao;
import com.dalbit.broadcast.vo.RoomStoryListOutVo;
import com.dalbit.broadcast.vo.procedure.*;
import com.dalbit.common.code.Status;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.common.vo.PagingVo;
import com.dalbit.common.vo.ProcedureOutputVo;
import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public String callBroadCastRoomNoticeEdit(P_RoomNoticeEditVo pRoomNoticeEditVo) {
        ProcedureVo procedureVo = new ProcedureVo(pRoomNoticeEditVo);
        contentDao.callBroadCastRoomNoticeEdit(procedureVo);

        String result;
        if(procedureVo.getRet().equals(Status.공지입력수정성공.getMessageCode())) {
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
    public String callBroadCastRoomNoticeDelete(P_RoomNoticeVo pRoomNoticeSelectVo) {
        ProcedureVo procedureVo = new ProcedureVo(pRoomNoticeSelectVo);
        contentDao.callBroadCastRoomNoticeDelete(procedureVo);

        String result;
        if (procedureVo.getRet().equals(Status.공지삭제하기성공.getMessageCode())) {
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
    public String callInsertStory(P_RoomStoryAddVo pRoomStoryAddVo) {
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
        procedureVo.setData(returnMap);

        String result;
        if(Status.방송방사연등록성공.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송방사연등록성공));
        }else if(Status.방송방사연등록_회원아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송방사연등록_회원아님));
        }else if(Status.방송방사연등록_해당방이없음.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송방사연등록_해당방이없음));
        }else if(Status.방송방사연등록_방참가자가아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송방사연등록_방참가자가아님));
        }else if(Status.방송방사연등록_10분에한번등록가능.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송방사연등록_10분에한번등록가능, procedureVo.getData()));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송방사연등록오류));
        }

        return result;
    }


    /**
     * 방송방 조회
     */
    public ProcedureOutputVo callGetStoryList(P_RoomStoryListVo pRoomStoryListVo) {
        ProcedureVo procedureVo = new ProcedureVo(pRoomStoryListVo);
        List<P_RoomStoryListVo> storyVoList = contentDao.callGetStory(procedureVo);

        ProcedureOutputVo procedureOutputVo;
        if(DalbitUtil.isEmpty(storyVoList)){
            procedureOutputVo = null;
        }else{
            List<RoomStoryListOutVo> outVoList = new ArrayList<>();
            for (int i=0; i<storyVoList.size(); i++){
                outVoList.add(new RoomStoryListOutVo(storyVoList.get(i), pRoomStoryListVo.getMem_no()));
            }
            procedureOutputVo = new ProcedureOutputVo(procedureVo, outVoList);
        }
        return procedureOutputVo;
    }

    /**
     * 방송방 사연 조회
     */
    public String callGetStory(P_RoomStoryListVo pRoomStoryListVo) {

        ProcedureOutputVo procedureOutputVo = callGetStoryList(pRoomStoryListVo);
        HashMap storyList = new HashMap();
        storyList.put("list", procedureOutputVo.getOutputBox());
        storyList.put("paging", new PagingVo(Integer.valueOf(procedureOutputVo.getRet()), pRoomStoryListVo.getPageNo(), pRoomStoryListVo.getPageCnt()));

        log.info("프로시저 응답 코드: {}", procedureOutputVo.getRet());
        log.info("프로시저 응답 데이타: {}", procedureOutputVo.getExt());
        log.info(" ### 프로시저 호출결과 ###");

        String result;
        if(Integer.parseInt(procedureOutputVo.getRet()) > 0) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송방사연조회성공, storyList));
        }else if(Status.방송방사연조회_등록된사연없음.getMessageCode().equals(procedureOutputVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.방송방사연조회_등록된사연없음, storyList));
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

            //사연 목록 재조회
            P_RoomStoryListVo pRoomStoryListVo = new P_RoomStoryListVo();
            pRoomStoryListVo.setMem_no(MemberVo.getMyMemNo());
            pRoomStoryListVo.setRoom_no(pRoomStoryDeleteVo.getRoom_no());
            pRoomStoryListVo.setPageNo(pRoomStoryDeleteVo.getPage());
            pRoomStoryListVo.setPageCnt(pRoomStoryDeleteVo.getRecords());

            ProcedureOutputVo procedureOutputVo = callGetStoryList(pRoomStoryListVo);
            HashMap storyList = new HashMap();
            storyList.put("list", procedureOutputVo.getOutputBox());
            storyList.put("paging", new PagingVo(Integer.valueOf(procedureOutputVo.getRet()), pRoomStoryListVo.getPageNo(), pRoomStoryListVo.getPageCnt()));

            log.info("프로시저 응답 코드: {}", procedureOutputVo.getRet());
            log.info("프로시저 응답 데이타: {}", procedureOutputVo.getExt());
            log.info(" ### 프로시저 호출결과 ###");

            result = gsonUtil.toJson(new JsonOutputVo(Status.방송방사연삭제성공, storyList));

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
     * 방송설정 금지어 단어 조회
     */
    public String callgetBanWord(P_BanWordSelectVo pBanWordSelectVo) {
        ProcedureVo procedureVo = new ProcedureVo(pBanWordSelectVo);
        contentDao.callgetBanWord(procedureVo);

        HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
        log.info("프로시저 응답 코드: {}", procedureVo.getRet());
        log.info("프로시저 응답 데이타: {}", resultMap);
        log.info(" ### 프로시저 호출결과 ###");

        HashMap returnMap = new HashMap();
        returnMap.put("banWord", DalbitUtil.getStringMap(resultMap, "banWord"));
        returnMap.put("banWordCnt", DalbitUtil.getIntMap(resultMap, "count"));
        procedureVo.setData(returnMap);

        String result;
        if(Status.금지어조회_성공.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.금지어조회_성공, procedureVo.getData()));
        }else if(Status.금지어조회_요청번호_회원아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.금지어조회_요청번호_회원아님));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.금지어조회_실패));
        }

        return result;
    }


    /**
     * 방송설정 금지어 저장
     */
    public String callInsertBanWord(P_BanWordInsertVo pBanWordInsertVo) {
        ProcedureVo procedureVo = new ProcedureVo(pBanWordInsertVo);
        contentDao.callInsertBanWord(procedureVo);

        HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
        log.info("프로시저 응답 코드: {}", procedureVo.getRet());
        log.info("프로시저 응답 데이타: {}", resultMap);
        log.info(" ### 프로시저 호출결과 ###");

        HashMap returnMap = new HashMap();
        returnMap.put("banWord", DalbitUtil.getStringMap(resultMap, "banWord"));
        returnMap.put("banWordCnt", DalbitUtil.getIntMap(resultMap, "count"));
        procedureVo.setData(returnMap);

        String result;
        if(Status.금지어저장_성공.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.금지어저장_성공, procedureVo.getData()));
        }else if(Status.금지어저장_요청번호_회원아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.금지어저장_요청번호_회원아님));
        }else if(Status.금지어저장_초과.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.금지어저장_초과));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.금지어저장_실패));
        }

        return result;

    }
}
