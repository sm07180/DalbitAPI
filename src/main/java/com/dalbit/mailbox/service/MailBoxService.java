package com.dalbit.mailbox.service;

import com.dalbit.common.code.Status;
import com.dalbit.common.vo.ImageVo;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.common.vo.PagingVo;
import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.mailbox.dao.MailBoxDao;
import com.dalbit.mailbox.vo.*;
import com.dalbit.mailbox.vo.procedure.*;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.socket.service.SocketService;
import com.dalbit.socket.vo.SocketVo;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Slf4j
@Service
public class MailBoxService {
    @Autowired
    MailBoxDao mailBoxDao;
    @Autowired
    GsonUtil gsonUtil;
    @Autowired
    SocketService socketService;

    /**
     * 우체통 대화방 리스트
     */
    public String callMailboxList(P_MailBoxListVo pMailBoxListVo) {
        ProcedureVo procedureVo = new ProcedureVo(pMailBoxListVo);
        List<P_MailBoxListVo> mailBoxListVo = mailBoxDao.callMailboxList(procedureVo);

        String result;
        HashMap mailBoxList = new HashMap();
        if(Integer.parseInt(procedureVo.getRet()) > -1) {
            HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            List<MailBoxListOutVo> outVoList = new ArrayList<>();

            if(!DalbitUtil.isEmpty(mailBoxListVo)){
                for (int i=0; i<mailBoxListVo.size(); i++){
                    outVoList.add(new MailBoxListOutVo(mailBoxListVo.get(i)));
                }
            }

            mailBoxList.put("list", outVoList);
            mailBoxList.put("isMailboxOn", DalbitUtil.getIntMap(resultMap, "mailboxOnOff") == 1);
            mailBoxList.put("paging", new PagingVo(Integer.valueOf(procedureVo.getRet()), DalbitUtil.getIntMap(resultMap, "pageNo"), DalbitUtil.getIntMap(resultMap, "pageCnt")));

            result = gsonUtil.toJson(new JsonOutputVo(Status.우체통대화방_조회_성공, mailBoxList));
        } else if (procedureVo.getRet().equals(Status.우체통팬대화방_조회_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.우체통팬대화방_조회_회원아님));
        } else if (procedureVo.getRet().equals(Status.우체통팬대화방_조회_레벨0.getMessageCode())) {
            mailBoxList.put("list", new ArrayList<>());
            result = gsonUtil.toJson(new JsonOutputVo(Status.우체통팬대화방_조회_레벨0, mailBoxList));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.우체통팬대화방_조회_실패));
        }
        return result;
    }


    /**
     * 우체통 대화방 추가 대상 리스트
     */
    public String callMailboxAddTargetList(P_MailBoxAddTargetListVo pMailBoxAddTargetListVo) {
        ProcedureVo procedureVo = new ProcedureVo(pMailBoxAddTargetListVo);
        List<P_MailBoxAddTargetListVo> mailBoxAddTargetListVo = mailBoxDao.callMailboxAddTargetList(procedureVo);

        String result;
        if(Integer.parseInt(procedureVo.getRet()) > -1) {
            HashMap mailBoxAddTargetList = new HashMap();
            HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            List<MailBoxAddTargetListOutVo> outVoList = new ArrayList<>();
            if(!DalbitUtil.isEmpty(mailBoxAddTargetListVo)){
                for (int i=0; i<mailBoxAddTargetListVo.size(); i++){
                    outVoList.add(new MailBoxAddTargetListOutVo(mailBoxAddTargetListVo.get(i), pMailBoxAddTargetListVo.getSlctType()));
                }

            }
            mailBoxAddTargetList.put("list", outVoList);
            mailBoxAddTargetList.put("fanTotalCnt", DalbitUtil.getIntMap(resultMap, "fanTotalCnt"));
            mailBoxAddTargetList.put("starTotalCnt", DalbitUtil.getIntMap(resultMap, "starTotalCnt"));
            mailBoxAddTargetList.put("paging", new PagingVo(Integer.valueOf(procedureVo.getRet()), DalbitUtil.getIntMap(resultMap, "pageNo"), DalbitUtil.getIntMap(resultMap, "pageCnt")));
            result = gsonUtil.toJson(new JsonOutputVo(Status.우체통대화방추가대상_조회_성공, mailBoxAddTargetList));
        } else if (procedureVo.getRet().equals(Status.우체통대화방추가대상_조회_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.우체통대화방추가대상_조회_회원아님));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.우체통대화방추가대상_조회_실패));
        }
        return result;
    }


    /**
     * 우체통 대화방 입장
     */
    public String callMailboxChatEnter(P_MailBoxEnterVo pMailBoxTargetEnterVo) {
        ProcedureVo procedureVo = new ProcedureVo(pMailBoxTargetEnterVo);
        mailBoxDao.callMailboxChatEnter(procedureVo);
        HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
        HashMap returnMap = new HashMap();
        returnMap.put("chatNo", DalbitUtil.getStringMap(resultMap, "chat_no"));
        returnMap.put("title", DalbitUtil.getStringMap(resultMap, "title"));    //대화방제목(대상닉네임)
        returnMap.put("lastMsgIdx", DalbitUtil.getStringMap(resultMap, "lastMsgIdx"));
        returnMap.put("isNewChat", false);

        String result="";
        if(Status.대화방입장_신규입장_성공.getMessageCode().equals(procedureVo.getRet())) {
            returnMap.put("isNewChat", true);
            result = gsonUtil.toJson(new JsonOutputVo(Status.대화방입장_신규입장_성공, returnMap));
        }else if(Status.대화방입장_성공.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.대화방입장_성공, returnMap));
        }else if(Status.대화방입장_요청회원아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.대화방입장_요청회원아님));
        }else if(Status.대화방입장_대상회원아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.대화방입장_대상회원아님));
        }else if(Status.대화방입장_본인안됨.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.대화방입장_본인안됨));
        }else if(Status.대화방입장_차단회원.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.대화방입장_차단회원));
        }else if(Status.대화방입장_레벨0.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.대화방입장_레벨0, returnMap));
        }else if(Status.대화방입장_대상레벨0.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.대화방입장_대상레벨0, returnMap));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.대화방입장_실패));
        }
        return result;
    }


    /**
     *  우체통 대화방 퇴장
     */
    public String callMailboxChatExit(P_MailBoxExitVo pMailBoxExitVo) {
        ProcedureVo procedureVo = new ProcedureVo(pMailBoxExitVo);
        mailBoxDao.callMailboxChatExit(procedureVo);

        String result="";
        if(Status.대화방퇴장_성공.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.대화방퇴장_성공));
        }else if(Status.대화방퇴장_회원아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.대화방퇴장_회원아님));
        }else if(Status.대화방퇴장_번호없음.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.대화방퇴장_번호없음));
        }else if(Status.대화방퇴장_이미나감.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.대화방퇴장_이미나감));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.대화방퇴장_실패));
        }
        return result;
    }


    /**
     * 우체통 대화방 대화 전송
     */
    public String callMailboxChatSend(P_MailBoxSendVo pMailBoxSendVo, HttpServletRequest request) {
        ProcedureVo procedureVo = new ProcedureVo(pMailBoxSendVo);
        mailBoxDao.callMailboxChatSend(procedureVo);

        String result="";
        if(Status.대화전송_성공.getMessageCode().equals(procedureVo.getRet())) {
            HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            HashMap returnMap = new HashMap();
            returnMap.put("memNo", DalbitUtil.getStringMap(resultMap, "mem_no"));
            returnMap.put("nickNm", DalbitUtil.getStringMap(resultMap, "nickName"));
            returnMap.put("gender", DalbitUtil.getStringMap(resultMap, "memSex"));
            returnMap.put("profImg", new ImageVo(DalbitUtil.getStringMap(resultMap, "profileImage"), DalbitUtil.getStringMap(resultMap, "memSex"), DalbitUtil.getProperty("server.photo.url")));
            returnMap.put("msgIdx", DalbitUtil.getStringMap(resultMap, "msgIdx"));
            returnMap.put("chatType", DalbitUtil.getIntMap(resultMap, "chatType"));
            if(DalbitUtil.getIntMap(resultMap, "chatType") == 2){   //이미지
                returnMap.put("msg", "이미지");
            }else if(DalbitUtil.getIntMap(resultMap, "chatType") == 3){ //아이템선물
                returnMap.put("msg", "아이템");
            }else{
                returnMap.put("msg", DalbitUtil.getStringMap(resultMap, "msg"));
            }
            returnMap.put("imageInfo", pMailBoxSendVo.getChatType() != 3 ? new ImageVo(DalbitUtil.getStringMap(resultMap, "addData1"), DalbitUtil.getProperty("server.photo.url")) : new ImageVo());
            returnMap.put("itemInfo", new ItemInfoVo(resultMap));
            returnMap.put("isRead", DalbitUtil.getIntMap(resultMap, "readYn") == 1);
            returnMap.put("targetMemNo", DalbitUtil.getStringMap(resultMap, "target_mem_no"));
            returnMap.put("sendDt", DalbitUtil.getUTCFormat(DalbitUtil.getStringMap(resultMap, "sendDate")));
            returnMap.put("sendTs", DalbitUtil.getUTCTimeStamp(DalbitUtil.getStringMap(resultMap, "sendDate")));

            //선물 소켓발송
            if(pMailBoxSendVo.getChatType() == 3){
                SocketVo vo = socketService.getSocketVo(pMailBoxSendVo.getChat_no(), MemberVo.getMyMemNo(request), DalbitUtil.isLogin(request));
                try{
                    socketService.chatGiftItem(pMailBoxSendVo.getChat_no(), MemberVo.getMyMemNo(request), DalbitUtil.getStringMap(resultMap, "target_mem_no"), returnMap, DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request), vo);
                    vo.resetData();
                }catch(Exception e){
                    log.info("Socket Service chatGiftItem Exception {}", e);
                }
            }

            result = gsonUtil.toJson(new JsonOutputVo(Status.대화전송_성공, returnMap));

        }else if(Status.대화전송_회원아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.대화전송_회원아님));
        }else if(Status.대화전송_채팅번호없음.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.대화전송_채팅번호없음));
        }else if(Status.대화전송_상대회원아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.대화전송_상대회원아님));
        }else if(Status.대화전송_아이템코드없음.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.대화전송_아이템코드없음));
        }else if(Status.대화전송_아이템타입없음.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.대화전송_아이템타입없음));
        }else if(Status.대화전송_달부족.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.대화전송_달부족));
        }else if(Status.대화전송_차단회원.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.대화전송_차단회원));
        }else if(Status.대화전송_본인비활성.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.대화전송_본인비활성));
        }else if(Status.대화전송_상대방비활성.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.대화전송_상대방비활성));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.대화전송_실패));
        }
        return result;
    }


    /**
     * 우체통 대화방 대화 읽음처리
     */
    public String callMailboxChatRead(P_MailBoxReadVo pMailBoxReadVo) {
        ProcedureVo procedureVo = new ProcedureVo(pMailBoxReadVo);
        mailBoxDao.callMailboxChatRead(procedureVo);

        String result="";
        if(Status.대화읽음_성공.getMessageCode().equals(procedureVo.getRet())) {
            HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            HashMap returnMap = new HashMap();
            returnMap.put("targetMemNo", DalbitUtil.getStringMap(resultMap, "target_mem_no"));
            returnMap.put("targetNickNm", DalbitUtil.getStringMap(resultMap, "nickName"));
            returnMap.put("targetGender", DalbitUtil.getStringMap(resultMap, "memSex"));
            returnMap.put("targetProfImg", new ImageVo(DalbitUtil.getStringMap(resultMap, "profileImage"), DalbitUtil.getStringMap(resultMap, "memSex"), DalbitUtil.getProperty("server.photo.url")));
            returnMap.put("lastMsgIdx", DalbitUtil.getStringMap(resultMap, "lastMsgIdx"));
            returnMap.put("readCnt", DalbitUtil.getIntMap(resultMap, "readCnt"));

            result = gsonUtil.toJson(new JsonOutputVo(Status.대화읽음_성공, returnMap));
        }else if(Status.대화읽음_회원아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.대화읽음_회원아님));
        }else if(Status.대화읽음_채팅번호없음.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.대화읽음_채팅번호없음));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.대화읽음_실패));
        }
        return result;
    }


    /**
     * 우체통 대화방 대화 조회
     */
    public String callMailboxMsg(P_MailBoxMsgListVo pMailBoxMsgListVo) {
        ProcedureVo procedureVo = new ProcedureVo(pMailBoxMsgListVo);
        List<P_MailBoxMsgListVo> mailBoxMsgListVo = mailBoxDao.callMailboxMsg(procedureVo);

        String result;
        if(Integer.parseInt(procedureVo.getRet()) > -1) {
            HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            HashMap mailBoxMsgList = new HashMap();
            List<MailBoxMsgListOutVo> outVoList = new ArrayList<>();

            if(!DalbitUtil.isEmpty(mailBoxMsgListVo)){
                for (int i=0; i<mailBoxMsgListVo.size(); i++){
                    outVoList.add(new MailBoxMsgListOutVo(mailBoxMsgListVo.get(i), resultMap));
                }
            }

            Collections.reverse(outVoList);
            mailBoxMsgList.put("list", outVoList);
            mailBoxMsgList.put("paging", new PagingVo(Integer.valueOf(procedureVo.getRet()), DalbitUtil.getIntMap(resultMap, "pageNo"), DalbitUtil.getIntMap(resultMap, "pageCnt")));
            mailBoxMsgList.put("targetMemNo", DalbitUtil.getStringMap(resultMap, "target_mem_no"));
            mailBoxMsgList.put("targetNickNm", DalbitUtil.getStringMap(resultMap, "nickName"));
            mailBoxMsgList.put("targetGender", DalbitUtil.getStringMap(resultMap, "memSex"));
            mailBoxMsgList.put("targetProfImg", new ImageVo(DalbitUtil.getStringMap(resultMap, "profileImage"), DalbitUtil.getStringMap(resultMap, "memSex"), DalbitUtil.getProperty("server.photo.url")));
            mailBoxMsgList.put("isNew", DalbitUtil.getIntMap(resultMap, "unreadCnt") > 0);
            mailBoxMsgList.put("isMailboxOn", DalbitUtil.getIntMap(resultMap, "mailboxOnOff") == 1);
            mailBoxMsgList.put("targetIsMailboxOn", DalbitUtil.getIntMap(resultMap, "target_mailboxOnOff") == 1);

            result = gsonUtil.toJson(new JsonOutputVo(Status.대화조회_성공, mailBoxMsgList));
        } else if (procedureVo.getRet().equals(Status.대화조회_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.대화조회_회원아님));
        } else if (procedureVo.getRet().equals(Status.대화조회_채팅번호없음.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.대화조회_채팅번호없음));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.대화조회_실패));
        }
        return result;
    }


    /**
     * 우체통 이미지 리스트
     */
    public String callMailboxImageList(P_MailBoxImageListVo pMailBoxImageListVo) {
        ProcedureVo procedureVo = new ProcedureVo(pMailBoxImageListVo);
        List<P_MailBoxImageListVo> mailBoxImageListVo = mailBoxDao.callMailboxImageList(procedureVo);

        String result;
        if(Integer.parseInt(procedureVo.getRet()) > -1) {
            HashMap mailBoxMsgList = new HashMap();
            List<MailBoxImageListOutVo> outVoList = new ArrayList<>();
            if(!DalbitUtil.isEmpty(mailBoxImageListVo)){
                for (int i=0; i<mailBoxImageListVo.size(); i++){
                    outVoList.add(new MailBoxImageListOutVo(mailBoxImageListVo.get(i)));
                }
            }
            mailBoxMsgList.put("list", outVoList);
            result = gsonUtil.toJson(new JsonOutputVo(Status.대화방_이미지조회_성공, mailBoxMsgList));
        } else if (procedureVo.getRet().equals(Status.대화방_이미지조회_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.대화방_이미지조회_회원아님));
        } else if (procedureVo.getRet().equals(Status.대화방_이미지조회_채팅번호없음.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.대화방_이미지조회_채팅번호없음));
        } else if (procedureVo.getRet().equals(Status.대화방_이미지조회_이미지타입아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.대화방_이미지조회_이미지타입아님));
        } else if (procedureVo.getRet().equals(Status.대화방_이미지조회_삭제된이미지.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.대화방_이미지조회_삭제된이미지));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.대화방_이미지조회_실패));
        }
        return result;
    }


    /**
     * 우체통 이미지 삭제
     */
    public String callMailboxImageDelete(P_MailBoxImageDeleteVo pMailBoxImageDeleteVo, HttpServletRequest request) {
        ProcedureVo procedureVo = new ProcedureVo(pMailBoxImageDeleteVo);
        mailBoxDao.callMailboxImageDelete(procedureVo);

        String result="";
        if(Status.대화방_이미지삭제_성공.getMessageCode().equals(procedureVo.getRet())) {
            HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            HashMap returnMap = new HashMap();
            returnMap.put("chatNo", DalbitUtil.getStringMap(resultMap, "chat_no"));
            returnMap.put("targetMemNo", DalbitUtil.getStringMap(resultMap, "target_mem_no"));
            returnMap.put("msgIdx", DalbitUtil.getStringMap(resultMap, "msgIdx"));
            returnMap.put("isDelete", "y".equals(DalbitUtil.getStringMap(resultMap, "addData2").toLowerCase()));

            try{
                //이미지 삭제 소켓
               socketService.sendChatImageDelete(pMailBoxImageDeleteVo.getChat_no(), returnMap, DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request));
            }catch(Exception e){}

            result = gsonUtil.toJson(new JsonOutputVo(Status.대화방_이미지삭제_성공, returnMap));
        }else if(Status.대화방_이미지삭제_회원아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.대화방_이미지삭제_회원아님));
        }else if(Status.대화방_이미지삭제_대화번호없음.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.대화방_이미지삭제_대화번호없음));
        }else if(Status.대화방_이미지삭제_이미지타입아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.대화방_이미지삭제_이미지타입아님));
        }else if(Status.대화방_이미지삭제_이미삭제됨.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.대화방_이미지삭제_이미삭제됨));
        }else if(Status.대화방_이미지삭제_본인이미지아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.대화방_이미지삭제_본인이미지아님));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.대화방_이미지삭제_실패));
        }
        return result;
    }

    /**
     * 우체통 신규메세지 조회
     */
    public String callMailboxUnreadCheck(HttpServletRequest request) {
        if(DalbitUtil.isLogin(request)){
            HashMap param = new HashMap();
            param.put("mem_no", MemberVo.getMyMemNo(request));
            ProcedureVo procedureVo = new ProcedureVo(param);
            mailBoxDao.callMailboxUnreadCheck(procedureVo);
            if("-1".equals(procedureVo.getRet())){
                return gsonUtil.toJson(new JsonOutputVo(Status.대화방_알림조회_회원아님));
            }else if("-9".equals(procedureVo.getRet())){
                return gsonUtil.toJson(new JsonOutputVo(Status.대화방_알림조회_실패));
            }else{
                HashMap data = new HashMap();
                data.put("isNew", "1".equals(procedureVo.getRet()));
                return gsonUtil.toJson(new JsonOutputVo(Status.대화방_알림조회_성공, data));
            }
        }else{
            return gsonUtil.toJson(new JsonOutputVo(Status.로그인필요));
        }
    }

    /**
     * 우체통 활성화 설정
     */
    public String callMailBoxIsUse(P_MailBoxIsUseVo pMailBoxIsUseVo, HttpServletRequest request) {
        ProcedureVo procedureVo = new ProcedureVo(pMailBoxIsUseVo);
        mailBoxDao.callMailBoxIsUse(procedureVo);

        String result="";
        if(Status.활성화설정_성공.getMessageCode().equals(procedureVo.getRet())) {
            HashMap returnMap = new HashMap();
            returnMap.put("isMailboxOn", pMailBoxIsUseVo.getMailboxOnOff() == 1);

            if(pMailBoxIsUseVo.getMailboxOnOff() == 1){
                result = gsonUtil.toJson(new JsonOutputVo(Status.활성화설정_ON, returnMap));
            }else{
                result = gsonUtil.toJson(new JsonOutputVo(Status.활성화설정_OFF, returnMap));
            }
        }else if(Status.활성화설정_회원아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.활성화설정_회원아님));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.활성화설정_실패));
        }
        return result;
    }



}
