package com.dalbit.broadcast.service;

import com.dalbit.broadcast.dao.ContentDao;
import com.dalbit.broadcast.vo.P_RoomNoticeEditVo;
import com.dalbit.broadcast.vo.P_RoomNoticeSelectVo;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import com.dalbit.util.MessageUtil;
import com.dalbit.common.code.Status;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;


@Slf4j
@Service
public class ContentService {

    @Autowired
    ContentDao contentDao;
    @Autowired
    MessageUtil messageUtil;
    @Autowired
    GsonUtil gsonUtil;
    @Value("${server.photo.url}")
    private String SERVER_PHOTO_URL;

    /**
     *  방송방 공지사항 조회
     */
    public String callBroadCastRoomNoticeSelect(P_RoomNoticeSelectVo pRoomNoticeSelectVo) {
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
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.공지가져오기성공, procedureVo.getData())));
        } else if(procedureVo.getRet().equals(Status.공지가져오기실패_정상회원이아님.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.공지가져오기실패_정상회원이아님)));
        } else if(procedureVo.getRet().equals(Status.공지가져오기실패_해당방이없음.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.공지가져오기실패_정상회원이아님)));
        } else if(procedureVo.getRet().equals(Status.공지가져오기실패_방참가자가아님.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.공지가져오기실패_방참가자가아님)));
        } else {
            result = gsonUtil.toJson((messageUtil.setJsonOutputVo(new JsonOutputVo(Status.공지가져오기실패_조회에러))));
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
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.공지입력수정성공, procedureVo.getData())));
        } else if(procedureVo.getRet().equals(Status.공지입력수정실패_정상회원이아님.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.공지입력수정실패_정상회원이아님)));
        } else if(procedureVo.getRet().equals(Status.공지입력수정실패_해당방이없음.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.공지입력수정실패_해당방이없음)));
        } else if(procedureVo.getRet().equals(Status.공지입력수정실패_방참가자가아님.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.공지입력수정실패_방참가자가아님)));
        } else if(procedureVo.getRet().equals(Status.공지입력수정실패_공지권한없음.getMessageCode())){
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.공지입력수정실패_공지권한없음)));
        } else {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.공지입력수정실패_입력수정에러)));
        }

        return result;
    }

    /**
     *  방송방 공지사항 삭제
     */
    public String callBroadCastRoomNoticeDelete(P_RoomNoticeSelectVo pRoomNoticeSelectVo) {
        ProcedureVo procedureVo = new ProcedureVo(pRoomNoticeSelectVo);
        contentDao.callBroadCastRoomNoticeDelete(procedureVo);

        String result;
        if (procedureVo.getRet().equals(Status.공지삭제하기성공.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.공지삭제하기성공, procedureVo.getData())));
        } else if (procedureVo.getRet().equals(Status.공지삭제하기실패_정상회원이아님.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.공지삭제하기실패_정상회원이아님)));
        } else if (procedureVo.getRet().equals(Status.공지삭제하기실패_해당방이없음.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.공지삭제하기실패_해당방이없음)));
        } else if (procedureVo.getRet().equals(Status.공지삭제하기실패_방참가자가아님.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.공지삭제하기실패_방참가자가아님)));
        } else if (procedureVo.getRet().equals(Status.공지삭제하기실패_공지삭제권한없음)) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.공지삭제하기실패_공지삭제권한없음)));
        } else {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.공지삭제하기실패_삭제에러)));
        }

        return result;

    }

    /**
     *  방송방 사연 등록
     */

    /**
     *  방송방 사연 목록 조회
     */

    /**
     *  방송방 사연 삭제
     */

}
