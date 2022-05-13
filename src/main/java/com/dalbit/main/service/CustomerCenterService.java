package com.dalbit.main.service;

import com.dalbit.common.code.Code;
import com.dalbit.common.code.CustomerStatus;
import com.dalbit.common.vo.*;
import com.dalbit.exception.GlobalException;
import com.dalbit.main.dao.CustomerCenterDao;
import com.dalbit.main.etc.MainEtc;
import com.dalbit.main.vo.FaqListOutVo;
import com.dalbit.main.vo.NoticeListOutVo;
import com.dalbit.main.vo.NoticeReadUpdVo;
import com.dalbit.main.vo.QnaListOutVo;
import com.dalbit.main.vo.procedure.*;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.rest.service.RestService;
import com.dalbit.slack.service.SlackSenderService;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import com.dalbit.util.MessageUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Slf4j
@Service
public class CustomerCenterService {

    @Autowired
    GsonUtil gsonUtil;
    @Autowired
    CustomerCenterDao customerCenterDao;
    @Autowired
    RestService restService;
    @Autowired
    SlackSenderService slackSenderService;
    @Autowired
    MessageUtil messageUtil;

    /**
     * 고객센터 공지사항 목록 조회
     */
    public String callNoticeList(P_NoticeListVo pNoticeListVo) {
        ProcedureVo procedureVo = new ProcedureVo(pNoticeListVo);
        List<P_NoticeListVo> noticeListVoList = customerCenterDao.callNoticeList(procedureVo);

        HashMap noticeList = new HashMap();
        if(DalbitUtil.isEmpty(noticeListVoList)){
            noticeList.put("list", new ArrayList<>());
            return gsonUtil.toJson(new JsonOutputVo(CustomerStatus.고객센터_공지사항조회_없음, noticeList));
        }

        List<NoticeListOutVo> outVoList = new ArrayList<>();
        for (int i=0; i<noticeListVoList.size(); i++){
            outVoList.add(new NoticeListOutVo(noticeListVoList.get(i)));
        }
        ProcedureOutputVo procedureOutputVo = new ProcedureOutputVo(procedureVo, outVoList);
        HashMap resultMap = new Gson().fromJson(procedureOutputVo.getExt(), HashMap.class);
        noticeList.put("list", procedureOutputVo.getOutputBox());
        noticeList.put("paging", new PagingVo(DalbitUtil.getIntMap(resultMap, "totalCnt"), DalbitUtil.getIntMap(resultMap, "pageNo"), DalbitUtil.getIntMap(resultMap, "pageCnt")));

        String result;
        if(Integer.parseInt(procedureOutputVo.getRet()) > 0) {
            result = gsonUtil.toJson(new JsonOutputVo(CustomerStatus.고객센터_공지사항조회_성공, noticeList));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(CustomerStatus.고객센터_공지사항조회_실패));
        }
        return result;
    }

    /**
     * 공지사항 읽음 확인
     */
    public String callNoticeReadUpd(NoticeReadUpdVo noticeReadUpdVo, HttpServletRequest request) {
        HashMap paramMap = new HashMap();
        paramMap.put("memNo", MemberVo.getMyMemNo(request));
        paramMap.put("notiNo", noticeReadUpdVo.getNotiNo());
        Integer result = customerCenterDao.callNoticeReadUpd(paramMap);

        if(result > 0) {
            return gsonUtil.toJson(new JsonOutputVo(CustomerStatus.공지사항_읽음확인_성공));
        } else if(result == 0) {
            return gsonUtil.toJson(new JsonOutputVo(CustomerStatus.공지사항_읽음확인_실패));
        } else {
            return gsonUtil.toJson(new JsonOutputVo(CustomerStatus.공지사항_읽음확인_이미읽음));
        }
    }

    /**
     * 고객센터 공지사항 내용(상세) 조회
     */
    public String callNoticeDetail(P_NoticeDetailVo pNoticeDetailVo) {
        ProcedureVo procedureVo = new ProcedureVo(pNoticeDetailVo);
        customerCenterDao.callNoticeDetail(procedureVo);

        log.info("프로시저 응답 코드: {}", procedureVo.getRet());
        log.info("프로시저 응답 데이타: {}", procedureVo.getExt());
        log.info(" ### 프로시저 호출결과 ###");

        String result;
        if(procedureVo.getRet().equals(CustomerStatus.고객센터_공지사항내용조회_성공.getMessageCode())) {
            HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            HashMap returnMap = new HashMap();
            returnMap.put("noticeIdx", DalbitUtil.getIntMap(resultMap, "noticeIdx"));
            returnMap.put("noticeType", DalbitUtil.getIntMap(resultMap, "slctType"));
            returnMap.put("title", DalbitUtil.getStringMap(resultMap, "title"));
            returnMap.put("contents", DalbitUtil.getStringMap(resultMap, "contents"));
            returnMap.put("writeDt", DalbitUtil.getUTCFormat(DalbitUtil.getDateMap(resultMap, "writeDate")));
            returnMap.put("writeTs", DalbitUtil.getUTCTimeStamp(DalbitUtil.getDateMap(resultMap, "writeDate")));
            procedureVo.setData(returnMap);

            result = gsonUtil.toJsonAdm(new JsonOutputVo(CustomerStatus.고객센터_공지사항내용조회_성공, procedureVo.getData()));
        } else if (procedureVo.getRet().equals(CustomerStatus.고객센터_공지사항내용조회_없음.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(CustomerStatus.고객센터_공지사항내용조회_없음));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(CustomerStatus.고객센터_공지사항내용조회_실패));
        }
        return result;
    }


    /**
     * 고객센터 FAQ 목록 조회
     */
    public String callFaqList(P_FaqListVo pFaqListVo) {
        ProcedureVo procedureVo = new ProcedureVo(pFaqListVo);
        List<P_FaqListVo> faqListVoList = customerCenterDao.callFaqList(procedureVo);

        HashMap faqList = new HashMap();
        if(DalbitUtil.isEmpty(faqListVoList)){
            faqList.put("list", new ArrayList<>());
            return gsonUtil.toJson(new JsonOutputVo(CustomerStatus.고객센터_FAQ조회_없음, faqList));
        }

        List<FaqListOutVo> outVoList = new ArrayList<>();
        for (int i=0; i<faqListVoList.size(); i++){
            outVoList.add(new FaqListOutVo(faqListVoList.get(i)));
        }
        ProcedureOutputVo procedureOutputVo = new ProcedureOutputVo(procedureVo, outVoList);
        HashMap resultMap = new Gson().fromJson(procedureOutputVo.getExt(), HashMap.class);
        faqList.put("list", procedureOutputVo.getOutputBox());
        faqList.put("paging", new PagingVo(DalbitUtil.getIntMap(resultMap, "totalCnt"), DalbitUtil.getIntMap(resultMap, "pageNo"), DalbitUtil.getIntMap(resultMap, "pageCnt")));

        String result;
        if(Integer.parseInt(procedureOutputVo.getRet()) > 0) {
            result = gsonUtil.toJson(new JsonOutputVo(CustomerStatus.고객센터_FAQ조회_성공, faqList));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(CustomerStatus.고객센터_FAQ조회_실패));
        }
        return result;
    }


    /**
     * 고객센터 FAQ 내용(상세) 조회
     */
    public String callFaqDetail(P_FaqDetailVo pFaqDetailVo) {
        ProcedureVo procedureVo = new ProcedureVo(pFaqDetailVo);
        customerCenterDao.callFaqDetail(procedureVo);

        log.info("프로시저 응답 코드: {}", procedureVo.getRet());
        log.info("프로시저 응답 데이타: {}", procedureVo.getExt());
        log.info(" ### 프로시저 호출결과 ###");

        String result;
        if(procedureVo.getRet().equals(CustomerStatus.고객센터_FAQ내용조회_성공.getMessageCode())) {
            HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            HashMap returnMap = new HashMap();
            returnMap.put("faqIdx", DalbitUtil.getIntMap(resultMap, "faqIdx"));
            returnMap.put("faqType", DalbitUtil.getIntMap(resultMap, "slctType"));
            returnMap.put("question", DalbitUtil.getStringMap(resultMap, "question"));
            returnMap.put("answer", DalbitUtil.getStringMap(resultMap, "answer"));
            returnMap.put("writeDt", DalbitUtil.getUTCFormat(DalbitUtil.getDateMap(resultMap, "writeDate")));
            returnMap.put("writeTs", DalbitUtil.getUTCTimeStamp(DalbitUtil.getDateMap(resultMap, "writeDate")));
            procedureVo.setData(returnMap);

            result = gsonUtil.toJsonAdm(new JsonOutputVo(CustomerStatus.고객센터_FAQ내용조회_성공, procedureVo.getData()));
        } else if (procedureVo.getRet().equals(CustomerStatus.고객센터_FAQ내용조회_없음.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(CustomerStatus.고객센터_FAQ내용조회_없음));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(CustomerStatus.고객센터_FAQ내용조회_실패));
        }
        return result;
    }


    /**
     * 고객센터 1:1 문의작성
     */
    public String callQnaAdd(P_QnaVo pQnaVo, HttpServletRequest request) throws GlobalException {
        String qnaFile = pQnaVo.getAddFile();
        String qnaFile1 = pQnaVo.getAddFile1();
        String qnaFile2 = pQnaVo.getAddFile2();
        String qnaFile3 = pQnaVo.getAddFile3();
        Boolean isDone = false;
        Boolean isDone1 = false;
        Boolean isDone2 = false;
        Boolean isDone3 = false;
        if(!DalbitUtil.isEmpty(qnaFile) && qnaFile.startsWith(Code.포토_일대일_임시_PREFIX.getCode())){
            isDone = true;
            qnaFile = DalbitUtil.replacePath(qnaFile);
        }
        if(!DalbitUtil.isEmpty(qnaFile1) && qnaFile1.startsWith(Code.포토_일대일_임시_PREFIX.getCode())){
            isDone1 = true;
            qnaFile1 = DalbitUtil.replacePath(qnaFile1);
        }
        if(!DalbitUtil.isEmpty(qnaFile2) && qnaFile2.startsWith(Code.포토_일대일_임시_PREFIX.getCode())){
            isDone2 = true;
            qnaFile2 = DalbitUtil.replacePath(qnaFile2);
        }
        if(!DalbitUtil.isEmpty(qnaFile3) && qnaFile3.startsWith(Code.포토_일대일_임시_PREFIX.getCode())){
            isDone3 = true;
            qnaFile3 = DalbitUtil.replacePath(qnaFile3);
        }
        pQnaVo.setAddFile(qnaFile);
        pQnaVo.setAddFile1(qnaFile1);
        pQnaVo.setAddFile2(qnaFile2);
        pQnaVo.setAddFile3(qnaFile3);

        ProcedureVo procedureVo = new ProcedureVo(pQnaVo);
        customerCenterDao.callQnaAdd(procedureVo);

        String result;
        if(procedureVo.getRet().equals(CustomerStatus.고객센터_문의작성_성공.getMessageCode())) {
            if(isDone){
                restService.imgDone(DalbitUtil.replaceDonePath(qnaFile), request);
            }
            if(isDone1){
                restService.imgDone(DalbitUtil.replaceDonePath(qnaFile1), request);
            }
            if(isDone2){
                restService.imgDone(DalbitUtil.replaceDonePath(qnaFile2), request);
            }
            if(isDone3){
                restService.imgDone(DalbitUtil.replaceDonePath(qnaFile3), request);
            }

            String contents =  messageUtil.get("customer.center.qna.add.message.work");

            Calendar cal = Calendar.getInstance();
            int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
            int hour = Integer.valueOf(DalbitUtil.convertDateFormat(new Date(), "HH"));
            String date = DalbitUtil.convertDateFormat(new Date(), "yyyyMMdd");

            boolean opTimeYn = true;
            if(hour < 9 || hour >= 18) {
                opTimeYn = false;
            }

            /* 설날 공휴일 임시 추가 */
            if(date.equals("20210211") || date.equals("20210212")) {
                opTimeYn = false;
            }
            /*===================*/

            if(!opTimeYn || (dayOfWeek == 1 || dayOfWeek == 7)) {
                contents = messageUtil.get("customer.center.qna.add.message.weekend");
            }


            var msgMap = new HashMap<String, String>();
            msgMap.put("title", "1:1문의 등록이 완료되었습니다");
            msgMap.put("contents", contents);
            result = gsonUtil.toJson(new JsonOutputVo(CustomerStatus.고객센터_문의작성_성공, msgMap));

            try{
                var procedureResult = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
                int qnaIdx = DalbitUtil.getIntMap(procedureResult, "qnaIdx");
                pQnaVo.setQnaIdx(qnaIdx);
                slackSenderService.sendQnaMessage(pQnaVo);
            }catch(Exception e){

            }

        } else if (procedureVo.getRet().equals(CustomerStatus.고객센터_문의작성_요청회원번호_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(CustomerStatus.고객센터_문의작성_요청회원번호_회원아님));
        } else if (procedureVo.getRet().equals(CustomerStatus.고객센터_문의작성_재문의불가.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(CustomerStatus.고객센터_문의작성_재문의불가));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(CustomerStatus.고객센터_문의작성_실패));
        }

        return result;

    }

    /**
     * 고객센터 나의 문의목록 조회
     */
    public String callQnaList(P_QnaListVo pQnaListVo) {
        ProcedureVo procedureVo = new ProcedureVo(pQnaListVo);
        List<P_QnaListVo> qnaListVoList = customerCenterDao.callQnaList(procedureVo);

        HashMap qnaList = new HashMap();
        if(DalbitUtil.isEmpty(qnaListVoList)){
            qnaList.put("list", new ArrayList<>());
            return gsonUtil.toJson(new JsonOutputVo(CustomerStatus.고객센터_문의내역_없음, qnaList));
        }

        List<QnaListOutVo> outVoList = new ArrayList<>();
        for (int i=0; i<qnaListVoList.size(); i++){
            outVoList.add(new QnaListOutVo(qnaListVoList.get(i)));
        }
        ProcedureOutputVo procedureOutputVo = new ProcedureOutputVo(procedureVo, outVoList);
        HashMap resultMap = new Gson().fromJson(procedureOutputVo.getExt(), HashMap.class);
        qnaList.put("list", procedureOutputVo.getOutputBox());
        qnaList.put("paging", new PagingVo(DalbitUtil.getIntMap(resultMap, "totalCnt"), DalbitUtil.getIntMap(resultMap, "pageNo"), DalbitUtil.getIntMap(resultMap, "pageCnt")));

        String result;
        if(Integer.parseInt(procedureOutputVo.getRet()) > 0) {
            result = gsonUtil.toJson(new JsonOutputVo(CustomerStatus.고객센터_문의내역조회_성공, qnaList));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(CustomerStatus.고객센터_문의내역조회_실패));
        }
        return result;

    }

    /**
     * 고객센터 1:1 문의내역 삭제
     */
    public String callQnaDel(P_QnaDelVo apiData) {
        ProcedureVo procedureVo = new ProcedureVo(apiData);
        customerCenterDao.callQnaDel(procedureVo);

        String result;
        if(procedureVo.getRet().equals(CustomerStatus.고객센터_문의삭제_성공.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(CustomerStatus.고객센터_문의삭제_성공));
        } else if (procedureVo.getRet().equals(CustomerStatus.고객센터_문의삭제_요청회원번호_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(CustomerStatus.고객센터_문의삭제_요청회원번호_회원아님));
        } else if (procedureVo.getRet().equals(CustomerStatus.고객센터_문의삭제_문의번호없음.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(CustomerStatus.고객센터_문의삭제_문의번호없음));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(CustomerStatus.고객센터_문의삭제_실패));
        }

        return result;
    }

    public Map<String, Object> checkAppVersion(HttpServletRequest request){
        Map<String, Object> data = new HashMap<>();
        try{
            data.put("lastVersion", "");
            data.put("nowVersion", "");
            data.put("storeUrl", "");
            data.put("isUpdate", false);
            DeviceVo deviceVo = new DeviceVo(request);
            if(!(deviceVo.getOs() == 1 || deviceVo.getOs() == 2)){
                return data;
            }
            Map<String, Object> appVersion = customerCenterDao.selectAppVersion();
            if(appVersion == null || appVersion.isEmpty()){
                return data;
            }
            if(!appVersion.containsKey("iosVersion") || !appVersion.containsKey("aosVersion")){
                return data;
            }

            data.put("lastVersion", deviceVo.getOs() == 1 ? appVersion.get("aosVersion") : appVersion.get("iosVersion"));
            data.put("nowVersion", deviceVo.getAppVersion());
            data.put("storeUrl", deviceVo.getOs() == 1 ? MainEtc.AOS_STORE_URL : MainEtc.IOS_STORE_URL);
            data.put("isUpdate", DalbitUtil.versionCompare((String)data.get("lastVersion"), deviceVo.getAppVersion()));
        }catch (Exception e){
            log.error("CustomerCenterService checkAppVersion Error => {}", e.getMessage());
        }
        return data;
    }
}
