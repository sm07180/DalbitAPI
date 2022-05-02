package com.dalbit.member.service;

import com.dalbit.common.code.CommonStatus;
import com.dalbit.common.code.MainStatus;
import com.dalbit.common.vo.ImageVo;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.member.vo.AwardHonorOutVo;
import com.dalbit.member.vo.procedure.P_AwardHonorListVo;
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
            mailBoxList.put("voteState", DalbitUtil.getIntMap(resultMap, "voteYn"));    //0:미투표, 1:투표완료, 2:기간만료, 3:재투표완료
            mailBoxList.put("dj1_memNo", DalbitUtil.getStringMap(resultMap, "dj1_memNo"));    //투표한 dj_memNo
            mailBoxList.put("dj2_memNo", DalbitUtil.getStringMap(resultMap, "dj2_memNo"));
            mailBoxList.put("dj3_memNo", DalbitUtil.getStringMap(resultMap, "dj3_memNo"));
            mailBoxList.put("list", outVoList);
            result = gsonUtil.toJson(new JsonOutputVo(MainStatus.어워드DJ조회_성공, mailBoxList));
        } else if (procedureVo.getRet().equals(MainStatus.어워드DJ조회_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(MainStatus.어워드DJ조회_회원아님));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(MainStatus.어워드DJ조회_실패));
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
        if(MainStatus.투표_성공.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(MainStatus.투표_성공));
        }else if(MainStatus.투표_회원아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(MainStatus.투표_회원아님));
        }else if(MainStatus.투표_이미함.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(MainStatus.투표_이미함));
        }else if(MainStatus.투표_10레벨미만.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(MainStatus.투표_10레벨미만));
        }else if(MainStatus.투표_동일번호.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(MainStatus.투표_동일번호));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(MainStatus.투표_실패));
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
            result = gsonUtil.toJson(new JsonOutputVo(MainStatus.투표결과조회_성공, mailBoxList));
        } else if (procedureVo.getRet().equals(MainStatus.투표결과조회_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(MainStatus.투표결과조회_회원아님));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(MainStatus.투표결과조회_실패));
        }
        return result;
    }


    /**
     * 어워드 명예의전당
     */
    public String callAwardHonorList(P_AwardHonorListVo pAwardHonorListVo) {
        ProcedureVo procedureVo = new ProcedureVo(pAwardHonorListVo);
        List<P_AwardHonorListVo> awardVoteResultVo = awardDao.callAwardHonorList(procedureVo);

        String result;
        if(Integer.parseInt(procedureVo.getRet()) > -1) {
            HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            HashMap honorList = new HashMap();
            List<AwardHonorOutVo> outVoList = new ArrayList<>();
            if(!DalbitUtil.isEmpty(awardVoteResultVo)){
                for (int i=0; i<awardVoteResultVo.size(); i++){
                    outVoList.add(new AwardHonorOutVo(awardVoteResultVo.get(i)));
                }
            }
            honorList.put("list", outVoList);
            honorList.put("djMemNo", DalbitUtil.getStringMap(resultMap, "mem_no"));
            honorList.put("djNickNm", DalbitUtil.getStringMap(resultMap, "mem_nick"));
            honorList.put("djProfImg", new ImageVo(DalbitUtil.getStringMap(resultMap, "profileImage"), DalbitUtil.getStringMap(resultMap, "memSex"), DalbitUtil.getProperty("server.photo.url")));
            honorList.put("djTitle", DalbitUtil.getStringMap(resultMap, "dj_title"));
            honorList.put("djMsg", DalbitUtil.getStringMap(resultMap, "dj_msg"));
            honorList.put("listenerPoint", DalbitUtil.getIntMap(resultMap, "listenerPoint"));
            honorList.put("goodPoint", DalbitUtil.getIntMap(resultMap, "goodPoint"));
            honorList.put("day", DalbitUtil.getIntMap(resultMap, "dateDiff"));
            honorList.put("isFan", DalbitUtil.getIntMap(resultMap, "enableFan") == 0);
            honorList.put("joinDt", DalbitUtil.getUTCFormat(DalbitUtil.getStringMap(resultMap, "joinDate")));
            honorList.put("joinTs", DalbitUtil.getUTCTimeStamp(DalbitUtil.getStringMap(resultMap, "joinDate")));

            result = gsonUtil.toJson(new JsonOutputVo(CommonStatus.명예의전당_조회_성공, honorList));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(CommonStatus.명예의전당_조회_실패));
        }
        return result;
    }
}
