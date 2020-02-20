package com.dalbit.search.service;

import com.dalbit.common.code.Status;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.common.vo.PagingVo;
import com.dalbit.common.vo.ProcedureOutputVo;
import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.search.dao.SearchDao;
import com.dalbit.search.vo.MemberSearchOutVo;
import com.dalbit.search.vo.RoomSearchOutVo;
import com.dalbit.search.vo.procedure.P_LiveRoomSearchVo;
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
            List<MemberSearchOutVo> outVoList = new ArrayList<>();
            for (int i=0; i<memberSearchVoList.size(); i++){
                outVoList.add(new MemberSearchOutVo(memberSearchVoList.get(i)));
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


    /**
     * 라이브 방송 검색
     */
    public String callLiveRoomSearch(P_LiveRoomSearchVo pLiveRoomSearchVo) {
        ProcedureVo procedureVo = new ProcedureVo(pLiveRoomSearchVo);
        List<P_LiveRoomSearchVo> liveRoomSearchVoList = searchDao.callLiveRoomSearch(procedureVo);

        ProcedureOutputVo procedureOutputVo;
        if(DalbitUtil.isEmpty(liveRoomSearchVoList)){
            procedureOutputVo = null;
        }else{
            List<RoomSearchOutVo> outVoList = new ArrayList<>();
            for (int i=0; i<liveRoomSearchVoList.size(); i++){
                outVoList.add(new RoomSearchOutVo(liveRoomSearchVoList.get(i)));
            }
            procedureOutputVo = new ProcedureOutputVo(procedureVo, outVoList);
        }

        HashMap resultMap = new Gson().fromJson(procedureOutputVo.getExt(), HashMap.class);
        HashMap roomSearchList = new HashMap();
        roomSearchList.put("list", procedureOutputVo.getOutputBox());
        roomSearchList.put("paging", new PagingVo(DalbitUtil.getIntMap(resultMap, "totalCnt"), DalbitUtil.getIntMap(resultMap, "pageNo"), DalbitUtil.getIntMap(resultMap, "pageCnt")));

        String result ="";
        if(Integer.parseInt(procedureOutputVo.getRet()) > 0) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.라이브방송검색_성공, roomSearchList));
        } else if (procedureVo.getRet().equals(Status.라이브방송검색_결과없음.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.라이브방송검색_결과없음));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.라이브방송검색_실패));
        }
        return result;
    }
}
