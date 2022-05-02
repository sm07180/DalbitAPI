package com.dalbit.admin.service;

import com.dalbit.admin.dao.AdminMemberDao;
import com.dalbit.common.code.CommonStatus;
import com.dalbit.common.code.Status;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Slf4j
@Service
public class AdminMemberService {

    @Autowired
    GsonUtil gsonUtil;

    @Autowired
    AdminMemberDao adminMemberDao;

    /**
     * 회원 상세
     */
    public String memberDetail(HashMap<String, String> paramMap) {

        ProcedureVo procedureVo = new ProcedureVo(paramMap);
        adminMemberDao.callMemberDetail(procedureVo);

        HashMap detail = new HashMap();

        Status status;
        if(procedureVo.getRet().equals("-1")) {
            status = CommonStatus.데이터없음;

        }else if(procedureVo.getRet().equals("0")){
            detail = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            status = CommonStatus.조회;

        }else{
            status = CommonStatus.비즈니스로직오류;
        }

        HashMap resultMap = new HashMap();
        resultMap.put("detail", detail);
        return gsonUtil.toJson(new JsonOutputVo(status, resultMap));
    }
    /**
     * 방송관리 목록
     */
    public String broadcastList(HashMap<String, String> paramMap) {

        ProcedureVo procedureVo = new ProcedureVo(paramMap);
        ArrayList<HashMap> list = adminMemberDao.callBroadcastRoomList(procedureVo);

        HashMap pagingInfo = new HashMap();

        Status status;
        if(procedureVo.getRet().equals(CommonStatus.데이터없음.getMessageCode())) {
            status = CommonStatus.데이터없음;

        }else if(0 < Integer.valueOf(procedureVo.getRet())){
            pagingInfo = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            status = CommonStatus.조회;

        }else{
            status = CommonStatus.비즈니스로직오류;
        }

        HashMap resultMap = new HashMap();
        resultMap.put("list", list);
        resultMap.put("pagingInfo", pagingInfo);
        return gsonUtil.toJson(new JsonOutputVo(status, resultMap));
    }

    /**
     * 클립관리 목록
     */
    public String clipList(HashMap<String, Object> paramMap) {

        paramMap.put("search_testId", 0);
        //paramMap.put("searchText", DalbitUtil.getStringMap(paramMap, "mem_no"));

        ProcedureVo procedureVo = new ProcedureVo(paramMap);
        ArrayList<HashMap> list = adminMemberDao.callClipList(procedureVo);

        HashMap pagingInfo = new HashMap();

        Status status;
        if(procedureVo.getRet().equals(CommonStatus.데이터없음.getMessageCode())) {
            status = CommonStatus.데이터없음;

        }else if(0 < Integer.valueOf(procedureVo.getRet())){
            pagingInfo = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            status = CommonStatus.조회;

        }else{
            status = CommonStatus.비즈니스로직오류;
        }

        HashMap resultMap = new HashMap();
        resultMap.put("list", list);
        resultMap.put("pagingInfo", pagingInfo);
        return gsonUtil.toJson(new JsonOutputVo(status, resultMap));
    }

    /**
     * 1:1 문의 관리 목록
     */
    public String questionList(HashMap<String, Object> paramMap) {

        paramMap.put("search_testId", 0);
        paramMap.put("searchText", DalbitUtil.getStringMap(paramMap, "mem_no"));

        ProcedureVo procedureVo = new ProcedureVo(paramMap);
        ArrayList<HashMap> list = adminMemberDao.callQuestionList(procedureVo);

        HashMap pagingInfo = new HashMap();

        Status status;
        if(procedureVo.getRet().equals(CommonStatus.데이터없음.getMessageCode())) {
            status = CommonStatus.데이터없음;

        }else if(0 < Integer.valueOf(procedureVo.getRet())){
            pagingInfo = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            status = CommonStatus.조회;

        }else{
            status = CommonStatus.비즈니스로직오류;
        }

        HashMap resultMap = new HashMap();
        resultMap.put("list", list);
        resultMap.put("pagingInfo", pagingInfo);
        return gsonUtil.toJson(new JsonOutputVo(status, resultMap));
    }

    /**
     * 이미지 관리 목록
     */
    public String imageList(HashMap<String, Object> paramMap) {

        ProcedureVo procedureVo = new ProcedureVo(paramMap);
        ArrayList<HashMap> list = adminMemberDao.callImageList(procedureVo);

        HashMap pagingInfo = new HashMap();

        Status status;
        if(procedureVo.getRet().equals(CommonStatus.데이터없음.getMessageCode())) {
            status = CommonStatus.데이터없음;

        }else if(0 < Integer.valueOf(procedureVo.getRet())){
            pagingInfo = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            status = CommonStatus.조회;

        }else{
            status = CommonStatus.비즈니스로직오류;
        }

        HashMap resultMap = new HashMap();
        resultMap.put("list", list);
        resultMap.put("pagingInfo", pagingInfo);
        return gsonUtil.toJson(new JsonOutputVo(status, resultMap));
    }

    /**
     * 내 지갑 관리 목록
     */
    public String walletList(HashMap<String, Object> paramMap) {

        paramMap.put("slctType", DalbitUtil.getIntMap(paramMap, "slctType"));

        ProcedureVo procedureVo = new ProcedureVo(paramMap);
        ArrayList<HashMap> list = adminMemberDao.callWalletList(procedureVo);

        HashMap pagingInfo = new HashMap();

        Status status;
        if(procedureVo.getRet().equals(CommonStatus.데이터없음.getMessageCode())) {
            status = CommonStatus.데이터없음;
        } else if(0 < Integer.valueOf(procedureVo.getRet())) {
            pagingInfo = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            status = CommonStatus.조회;
        } else {
            status = CommonStatus.비즈니스로직오류;
        }

        HashMap resultMap = new HashMap();
        resultMap.put("list", list);
        resultMap.put("pagingInfo", pagingInfo);

        return gsonUtil.toJson(new JsonOutputVo(status, resultMap));
    }


}
