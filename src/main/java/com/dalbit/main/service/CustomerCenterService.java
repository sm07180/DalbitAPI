package com.dalbit.main.service;

import com.dalbit.common.code.Code;
import com.dalbit.common.code.Status;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.common.vo.PagingVo;
import com.dalbit.common.vo.ProcedureOutputVo;
import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.exception.GlobalException;
import com.dalbit.main.dao.CustomerCenterDao;
import com.dalbit.main.vo.FaqListOutVo;
import com.dalbit.main.vo.NoticeListOutVo;
import com.dalbit.main.vo.QnaListOutVo;
import com.dalbit.main.vo.procedure.*;
import com.dalbit.rest.service.RestService;
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
public class CustomerCenterService {

    @Autowired
    GsonUtil gsonUtil;
    @Autowired
    CustomerCenterDao customerCenterDao;
    @Autowired
    RestService restService;


    /**
     * 고객센터 공지사항 목록 조회
     */
    public String callNoticeList(P_NoticeListVo pNoticeListVo) {
        ProcedureVo procedureVo = new ProcedureVo(pNoticeListVo);
        List<P_NoticeListVo> noticeListVoList = customerCenterDao.callNoticeList(procedureVo);

        HashMap noticeList = new HashMap();
        if(DalbitUtil.isEmpty(noticeListVoList)){
            noticeList.put("list", new ArrayList<>());
            return gsonUtil.toJson(new JsonOutputVo(Status.고객센터_공지사항조회_없음, noticeList));
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
            result = gsonUtil.toJson(new JsonOutputVo(Status.고객센터_공지사항조회_성공, noticeList));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.고객센터_공지사항조회_실패));
        }
        return result;
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

        HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
        HashMap returnMap = new HashMap();
        returnMap.put("noticeIdx", DalbitUtil.getIntMap(resultMap, "noticeIdx"));
        returnMap.put("noticeType", DalbitUtil.getIntMap(resultMap, "slctType"));
        returnMap.put("title", DalbitUtil.getStringMap(resultMap, "title"));
        returnMap.put("contents", DalbitUtil.getStringMap(resultMap, "contents"));
        returnMap.put("writeDt", DalbitUtil.getUTCFormat(DalbitUtil.getDateMap(resultMap, "writeDate")));
        returnMap.put("writeTs", DalbitUtil.getUTCTimeStamp(DalbitUtil.getDateMap(resultMap, "writeDate")));
        procedureVo.setData(returnMap);

        String result;
        if(procedureVo.getRet().equals(Status.고객센터_공지사항내용조회_성공.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.고객센터_공지사항내용조회_성공, procedureVo.getData()));
        } else if (procedureVo.getRet().equals(Status.고객센터_공지사항내용조회_없음.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.고객센터_공지사항내용조회_없음));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.고객센터_공지사항내용조회_실패));
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
            return gsonUtil.toJson(new JsonOutputVo(Status.고객센터_FAQ조회_없음, faqList));
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
            result = gsonUtil.toJson(new JsonOutputVo(Status.고객센터_FAQ조회_성공, faqList));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.고객센터_FAQ조회_실패));
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

        HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
        HashMap returnMap = new HashMap();
        returnMap.put("faqIdx", DalbitUtil.getIntMap(resultMap, "faqIdx"));
        returnMap.put("faqType", DalbitUtil.getIntMap(resultMap, "slctType"));
        returnMap.put("question", DalbitUtil.getStringMap(resultMap, "question"));
        returnMap.put("answer", DalbitUtil.getStringMap(resultMap, "answer"));
        returnMap.put("writeDt", DalbitUtil.getUTCFormat(DalbitUtil.getDateMap(resultMap, "writeDate")));
        returnMap.put("writeTs", DalbitUtil.getUTCTimeStamp(DalbitUtil.getDateMap(resultMap, "writeDate")));
        procedureVo.setData(returnMap);

        String result;
        if(procedureVo.getRet().equals(Status.고객센터_FAQ내용조회_성공.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.고객센터_FAQ내용조회_성공, procedureVo.getData()));
        } else if (procedureVo.getRet().equals(Status.고객센터_FAQ내용조회_없음.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.고객센터_FAQ내용조회_없음));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.고객센터_FAQ내용조회_실패));
        }
        return result;
    }


    /**
     * 고객센터 1:1 문의작성
     */
    public String callQnaAdd(P_QnaVo pQnaVo, HttpServletRequest request) throws GlobalException {
        String qnaFile = pQnaVo.getAddFile();
        Boolean isDone = false;
        if(!DalbitUtil.isEmpty(qnaFile) && qnaFile.startsWith(Code.포토_일대일_임시_PREFIX.getCode())){
            isDone = true;
        }
        qnaFile = DalbitUtil.replacePath(qnaFile);
        pQnaVo.setAddFile(qnaFile);

        ProcedureVo procedureVo = new ProcedureVo(pQnaVo);
        customerCenterDao.callQnaAdd(procedureVo);

        String result;
        if(procedureVo.getRet().equals(Status.고객센터_문의작성_성공.getMessageCode())) {
            if(isDone){
                restService.imgDone(DalbitUtil.replaceDonePath(qnaFile), request);
            }
            result = gsonUtil.toJson(new JsonOutputVo(Status.고객센터_문의작성_성공));
        } else if (procedureVo.getRet().equals(Status.고객센터_문의작성_요청회원번호_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.고객센터_문의작성_요청회원번호_회원아님));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.고객센터_문의작성_실패));
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
            return gsonUtil.toJson(new JsonOutputVo(Status.고객센터_문의내역_없음, qnaList));
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
            result = gsonUtil.toJson(new JsonOutputVo(Status.고객센터_문의내역조회_성공, qnaList));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.고객센터_문의내역조회_실패));
        }
        return result;

    }
}
