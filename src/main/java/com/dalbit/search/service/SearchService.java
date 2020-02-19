package com.dalbit.search.service;

import com.dalbit.common.code.Status;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.common.vo.PagingVo;
import com.dalbit.common.vo.ProcedureOutputVo;
import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.search.dao.SearchDao;
import com.dalbit.search.vo.SearchMemberOutVo;
import com.dalbit.search.vo.procedure.P_MemberSearchVo;
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
public class SearchService {

    @Autowired
    SearchDao searchDao;
    @Autowired
    GsonUtil gsonUtil;

    /**
     * 회원 닉네임 검색
     */
    public String callMemberNickSearch(P_MemberSearchVo pMemberSearchVo) {
        ProcedureVo procedureVo = new ProcedureVo(pMemberSearchVo);
        List<P_MemberSearchVo> memberSearchVoList = searchDao.callMemberNickSearch(procedureVo);

        ProcedureOutputVo procedureOutputVo;
        if(DalbitUtil.isEmpty(memberSearchVoList)){
            procedureOutputVo = null;
        }else{
            List<SearchMemberOutVo> outVoList = new ArrayList<>();
            for (int i=0; i<memberSearchVoList.size(); i++){
                outVoList.add(new SearchMemberOutVo(memberSearchVoList.get(i)));
            }
            procedureOutputVo = new ProcedureOutputVo(procedureVo, outVoList);
        }

        HashMap resultMap = new Gson().fromJson(procedureOutputVo.getExt(), HashMap.class);
        HashMap memberSearchList = new HashMap();
        memberSearchList.put("list", procedureOutputVo.getOutputBox());
        memberSearchList.put("paging", new PagingVo(DalbitUtil.getIntMap(resultMap, "totalCnt"), DalbitUtil.getIntMap(resultMap, "pageNo"), DalbitUtil.getIntMap(resultMap, "pageCnt")));

        String result ="";
        if(Integer.parseInt(procedureOutputVo.getRet()) > 0) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원닉네임검색_성공, memberSearchList));
        } else if (procedureVo.getRet().equals(Status.회원닉네임검색_결과없음.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원닉네임검색_결과없음));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원닉네임검색_실패));
        }
        return result;

    }
}
