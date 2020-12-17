package com.dalbit.member.service;

import com.dalbit.common.code.Status;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.member.dao.AwardDao;
import com.dalbit.member.vo.AwardListOutVo;
import com.dalbit.member.vo.AwardVoteResultOutVo;
import com.dalbit.member.vo.procedure.P_AwardListVo;
import com.dalbit.member.vo.procedure.P_AwardVoteResultVo;
import com.dalbit.member.vo.procedure.P_AwardVoteVo;
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
public class AwardService {

    @Autowired
    AwardDao awardDao;
    @Autowired
    GsonUtil gsonUtil;

    /**
     * 어워드 DJ리스트
     */
    public String callAwardList(P_AwardListVo pAwardListVo) {
        ProcedureVo procedureVo = new ProcedureVo(pAwardListVo);
        List<P_AwardListVo> awardListVo = awardDao.callAwardList(procedureVo);

        HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);

        String result;
        if(Integer.parseInt(procedureVo.getRet()) > -1) {
            HashMap mailBoxList = new HashMap();
            List<AwardListOutVo> outVoList = new ArrayList<>();
            if(!DalbitUtil.isEmpty(awardListVo)){
                for (int i=0; i<awardListVo.size(); i++){
                    outVoList.add(new AwardListOutVo(awardListVo.get(i)));
                }
            }
            mailBoxList.put("voteState", DalbitUtil.getIntMap(resultMap, "voteYn"));    //0:미투표, 1:투표완료, 2:기간만료
            mailBoxList.put("list", outVoList);
            result = gsonUtil.toJson(new JsonOutputVo(Status.어워드DJ조회_성공, mailBoxList));
        } else if (procedureVo.getRet().equals(Status.어워드DJ조회_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.어워드DJ조회_회원아님));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.어워드DJ조회_실패));
        }
        return result;
    }


    /**
     * 어워드 DJ투표
     */
    public String callAwardVote(P_AwardVoteVo pAwardVoteVo) {
        ProcedureVo procedureVo = new ProcedureVo(pAwardVoteVo);
        awardDao.callAwardVote(procedureVo);

        String result="";
        if(Status.투표_성공.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.투표_성공));
        }else if(Status.투표_회원아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.투표_회원아님));
        }else if(Status.투표_이미함.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.투표_이미함));
        }else if(Status.투표_10레벨미만.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.투표_10레벨미만));
        }else if(Status.투표_동일번호.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.투표_동일번호));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.투표_실패));
        }
        return result;
    }


    /**
     * 어워드 투표결과
     */
    public String callAwardVoteResult(P_AwardVoteResultVo pAwardVoteResultVo) {
        ProcedureVo procedureVo = new ProcedureVo(pAwardVoteResultVo);
        List<P_AwardVoteResultVo> awardVoteResultVo = awardDao.callAwardVoteResult(procedureVo);

        String result;
        if(Integer.parseInt(procedureVo.getRet()) > -1) {
            HashMap mailBoxList = new HashMap();
            List<AwardVoteResultOutVo> outVoList = new ArrayList<>();
            if(!DalbitUtil.isEmpty(awardVoteResultVo)){
                for (int i=0; i<awardVoteResultVo.size(); i++){
                    outVoList.add(new AwardVoteResultOutVo(awardVoteResultVo.get(i)));
                }
            }
            mailBoxList.put("list", outVoList);
            result = gsonUtil.toJson(new JsonOutputVo(Status.투표결과조회_성공, mailBoxList));
        } else if (procedureVo.getRet().equals(Status.투표결과조회_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.투표결과조회_회원아님));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.투표결과조회_실패));
        }
        return result;
    }
}
