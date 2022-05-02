package com.dalbit.common.service;

import com.dalbit.common.code.CommonStatus;
import com.dalbit.common.dao.PushDao;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.common.vo.procedure.P_pushInsertVo;
import com.dalbit.common.vo.procedure.P_pushStmpInsertVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Slf4j
@Service
public class PushService {

    @Autowired
    PushDao pushDao;

    @Autowired
    GsonUtil gsonUtil;

    @Value("${server.photo.url}")
    String SERVER_PHOTO_URL;

    @Value("${push.logo.img.101}")
    String PUSH_LOGO_IMG_101;
    @Value("${push.logo.img.102}")
    String PUSH_LOGO_IMG_102;
    @Value("${push.logo.img.103}")
    String PUSH_LOGO_IMG_103;

    public String sendPushReqOK(P_pushInsertVo pPushInsertVo){

        pPushInsertVo.setSend_cnt("1");
        pPushInsertVo.setIs_all("7");
        pPushInsertVo.setPlatform("111");
        pPushInsertVo.setStatus("0");
        pPushInsertVo.setMsg_type("0");
        pPushInsertVo.setIs_direct("0");

        String pushResult = callContentsPushAdd(pPushInsertVo);
        log.info("[PUSH SEND RESULT] : {}" , pushResult);

        return pushResult;
    }

    /**
     * 푸시  등록
     */
    public String callContentsPushAdd(P_pushInsertVo pPushInsertVo){
        pPushInsertVo.setOpName(MemberVo.getMyMemNo());
        ProcedureVo procedureVo = new ProcedureVo(pPushInsertVo);
        String result = null;

        try{
            if(DalbitUtil.isEmpty(pPushInsertVo.getSend_url())){
                if(!DalbitUtil.isEmpty(pPushInsertVo.getImage_type())){
                    if(pPushInsertVo.getImage_type().equals("102")){
                        pPushInsertVo.setSend_url(PUSH_LOGO_IMG_102);
                    }else if(pPushInsertVo.getImage_type().equals("103")){
                        pPushInsertVo.setSend_url(PUSH_LOGO_IMG_103);
                    }else{
                        pPushInsertVo.setSend_url(PUSH_LOGO_IMG_101);
                    }
                }
            }

            int insertResult = pushDao.insertContentsPushAdd(pPushInsertVo);

            if(insertResult > 0){
                HashMap resultHash = null;
                // 지정 일 경우 푸시 발송
                resultHash = callSendPush(pPushInsertVo);

                result = gsonUtil.toJson(new JsonOutputVo(CommonStatus.푸시등록_성공, resultHash));

            }else{
                result = gsonUtil.toJson(new JsonOutputVo(CommonStatus.푸시등록_에러));
            }
        }catch (Exception e){
            e.printStackTrace();
            result = gsonUtil.toJson(new JsonOutputVo(CommonStatus.푸시등록_에러));
        }

        return result;
    }

    /**
     * 푸시 발송 모듈 적재
     */
    public HashMap callSendPush(P_pushInsertVo pPushInsertVo){
        String mem_nos = pPushInsertVo.getMem_nos();
        HashMap resultMap = new HashMap();
        int sucCnt=0;
        int notTokenCnt=0;
        int notMemNoCnt=0;
        int failCnt=0;

        pPushInsertVo.setSend_url(pPushInsertVo.getSend_url().replace(SERVER_PHOTO_URL, ""));

        if(pPushInsertVo.getIs_all().equals("7")){        // 지정 일 경우 푸시 발송
            if(mem_nos != null && mem_nos.length() > 0){
                String[] arryMem_no = mem_nos.split("\\|");

                for(String target : arryMem_no){
                    P_pushStmpInsertVo pPushStmpInsertVo = new P_pushStmpInsertVo(target, pPushInsertVo);
                    ProcedureVo procedureVo = new ProcedureVo(pPushStmpInsertVo);

                    pushDao.callStmpPushAdd(procedureVo);

                    if(CommonStatus.푸시성공.getMessageCode().equals(procedureVo.getRet())){
                        log.debug("[PUSH_SEND] 푸시 발송 성공 (" + target + ")");
                        sucCnt++;
                    }else if(CommonStatus.푸시_디바이스토큰없음.getMessageCode().equals(procedureVo.getRet())){
                        log.error("[PUSH_SEND] ERROR 디바이스토큰 미존재 (" + target + ")");
                        notTokenCnt++;
                    }else if(CommonStatus.푸시_회원아님.getMessageCode().equals(procedureVo.getRet())){
                        log.error("[PUSH_SEND] ERROR mem_no 미존재 (" + target + ")");
                        notMemNoCnt++;
                    } else {
                        log.error("[PUSH_SEND] ERROR [ targetMemNo:{} | 실패코드:{} | 실패내용:{} ]", target, procedureVo.getRet(), procedureVo.getExt());
                        failCnt++;
                    }
                }
            }
        }else if(pPushInsertVo.getIs_all().equals("11")){  // 전체 발송
            P_pushStmpInsertVo pPushStmpInsertVo = new P_pushStmpInsertVo(null, pPushInsertVo);
            ProcedureVo procedureVo = new ProcedureVo(pPushStmpInsertVo);

            pushDao.callStmpPushAdd(procedureVo);

            if(CommonStatus.푸시성공.getMessageCode().equals(procedureVo.getRet())){
                //log.debug("[PUSH_SEND] 푸시 발송 성공 (" + MemberVo.getUserInfo().getEmp_no() + ")");
                sucCnt++;
            } else {
                log.error("[PUSH_SEND] ERROR [ 실패코드:{} | 실패내용:{} ]", procedureVo.getRet(), procedureVo.getExt());
                failCnt++;
            }
        }else{
            log.error("[PUSH_SEND] ERROR 발송 타입이 존재하지 않습니다. [ is_all:{}]", pPushInsertVo.getIs_all());
        }

        log.error("[PUSH_SEND] Result [ 성공:{} | 디바이스토큰 미존재:{} | mem_no 미존재:{} | 실패:{} ]", sucCnt, notTokenCnt, notMemNoCnt, failCnt);

        resultMap.put("is_all", pPushInsertVo.getIs_all());
        resultMap.put("sucCnt", sucCnt);
        resultMap.put("notTokenCnt", notTokenCnt);
        resultMap.put("notMemNoCnt", notMemNoCnt);
        resultMap.put("failCnt", failCnt);

        return resultMap;
    }
}
