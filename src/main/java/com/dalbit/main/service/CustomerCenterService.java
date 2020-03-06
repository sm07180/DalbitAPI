package com.dalbit.main.service;

import com.dalbit.common.code.Status;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.common.vo.PagingVo;
import com.dalbit.common.vo.ProcedureOutputVo;
import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.main.dao.CustomerCenterDao;
import com.dalbit.main.vo.NoticeListOutVo;
import com.dalbit.main.vo.procedure.P_NoticeDetailVo;
import com.dalbit.main.vo.procedure.P_NoticeListVo;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class CustomerCenterService {

    @Autowired
    GsonUtil gsonUtil;
    @Autowired
    CustomerCenterDao customerCenterDao;


    /**
     * 고객센터 공지사항 목록 조회
     */
    public String callNoticeList(P_NoticeListVo pNoticeListVo) {
        ProcedureVo procedureVo = new ProcedureVo(pNoticeListVo);
        List<P_NoticeListVo> noticeListVoList = customerCenterDao.callNoticeList(procedureVo);

        ProcedureOutputVo procedureOutputVo;
        if(DalbitUtil.isEmpty(noticeListVoList)){
            procedureOutputVo = null;
        }else{
            List<NoticeListOutVo> outVoList = new ArrayList<>();
            for (int i=0; i<noticeListVoList.size(); i++){
                outVoList.add(new NoticeListOutVo(noticeListVoList.get(i)));
            }
            procedureOutputVo = new ProcedureOutputVo(procedureVo, outVoList);
        }

        HashMap resultMap = new Gson().fromJson(procedureOutputVo.getExt(), HashMap.class);
        HashMap noticeList = new HashMap();
        noticeList.put("list", procedureOutputVo.getOutputBox());
        noticeList.put("paging", new PagingVo(DalbitUtil.getIntMap(resultMap, "totalCnt"), DalbitUtil.getIntMap(resultMap, "pageNo"), DalbitUtil.getIntMap(resultMap, "pageCnt")));

        String result ="";
        if(Integer.parseInt(procedureOutputVo.getRet()) > 0) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.고객센터_공지사항조회_성공, noticeList));
        } else if (procedureVo.getRet().equals(Status.고객센터_공지사항조회_없음.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.고객센터_공지사항조회_없음));
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
        returnMap.put("noticeType", DalbitUtil.getIntMap(resultMap, "slctType"));
        returnMap.put("title", DalbitUtil.getStringMap(resultMap, "title"));
        returnMap.put("contents", DalbitUtil.getStringMap(resultMap, "contents"));
        returnMap.put("writeDt", DalbitUtil.getUTCFormat(DalbitUtil.getDateMap(resultMap, "writeDate")));
        returnMap.put("writeTs", DalbitUtil.getUTCTimeStamp(DalbitUtil.getDateMap(resultMap, "writeDate")));
        procedureVo.setData(returnMap);

        String result ="";
        if(procedureVo.getRet().equals(Status.고객센터_공지사항내용조회_성공.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.고객센터_공지사항내용조회_성공, procedureVo.getData()));
        } else if (procedureVo.getRet().equals(Status.고객센터_공지사항내용조회_없음.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.고객센터_공지사항내용조회_없음));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.고객센터_공지사항내용조회_실패));
        }
        return result;
    }
}
