package com.dalbit.member.service;

import com.dalbit.common.code.Status;
import com.dalbit.common.service.CommonService;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.common.vo.PagingVo;
import com.dalbit.common.vo.ProcedureOutputVo;
import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.member.dao.ProfileDao;
import com.dalbit.member.vo.*;
import com.dalbit.member.vo.procedure.*;
import com.dalbit.socket.service.SocketService;
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
public class ProfileService {

    @Autowired
    ProfileDao profileDao;
    @Autowired
    GsonUtil gsonUtil;
    @Autowired
    CommonService commonService;
    @Autowired
    SocketService socketService;

    public ProcedureVo getProfile(P_ProfileInfoVo pProfileInfo){

        ProcedureVo procedureVo = new ProcedureVo(pProfileInfo);
        profileDao.callMemberInfo(procedureVo);
        log.info("프로시저 응답 코드: {}", procedureVo.getRet());
        log.info("프로시저 응답 데이타: {}", procedureVo.getExt());
        log.info(" ### 프로시저 호출결과 ###");
        return procedureVo;
    }


    /**
     * 정보 조회
     */
    public String callMemberInfo(P_ProfileInfoVo pProfileInfo) {

        ProcedureVo procedureVo = getProfile(pProfileInfo);
        P_ProfileInfoVo profileInfo = new Gson().fromJson(procedureVo.getExt(), P_ProfileInfoVo.class);
        List fanRankList = commonService.getFanRankList(profileInfo.getFanRank1(), profileInfo.getFanRank2(), profileInfo.getFanRank3());
        ProfileInfoOutVo profileInfoOutVo = new ProfileInfoOutVo(profileInfo, pProfileInfo.getTarget_mem_no(), fanRankList);

        String result;
        if(procedureVo.getRet().equals(Status.회원정보보기_성공.getMessageCode())) {
            HashMap myInfo = socketService.getMyInfo(MemberVo.getMyMemNo());
            profileInfoOutVo.setBirth(DalbitUtil.getBirth(DalbitUtil.getStringMap(myInfo, "birthYear"), DalbitUtil.getStringMap(myInfo, "birthMonth"), DalbitUtil.getStringMap(myInfo, "birthDay")));
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원정보보기_성공, profileInfoOutVo));
        }else if(procedureVo.getRet().equals(Status.회원정보보기_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원정보보기_회원아님));
        }else if(procedureVo.getRet().equals(Status.회원정보보기_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원정보보기_대상아님));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원정보보기_실패));
        }
        return result;
    }


    /**
     * 팬보드 댓글 달기
     */
    public String callMemberFanboardAdd(P_FanboardAddVo pFanboardAddVo) {
        ProcedureVo procedureVo = new ProcedureVo(pFanboardAddVo);
        profileDao.callMemberFanboardAdd(procedureVo);

        HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
        HashMap returnMap = new HashMap();
        returnMap.put("isLevelUp", DalbitUtil.getIntMap(resultMap, "levelUp") == 1 ? true : false);

        String result;
        if(Status.팬보드_댓글달기성공.getMessageCode().equals(procedureVo.getRet())){

            HashMap socketMap = new HashMap();
            //TODO - 레벨업 유무 소켓추가 추후 확인
            //socketMap.put("isLevelUp", DalbitUtil.getIntMap(resultMap, "levelUp") == 1 ? true : false);

            result = gsonUtil.toJson(new JsonOutputVo(Status.팬보드_댓글달기성공, returnMap));
        }else if (Status.팬보드_댓글달기실패_스타회원번호_회원아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬보드_댓글달기실패_스타회원번호_회원아님));

        }else if (Status.팬보드_댓글달기실패_작성자회원번호_회원아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬보드_댓글달기실패_작성자회원번호_회원아님));

        }else if (Status.팬보드_댓글달기실패_잘못된댓글그룹번호.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬보드_댓글달기실패_잘못된댓글그룹번호));

        }else if (Status.팬보드_댓글달기실패_depth값_오류.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬보드_댓글달기실패_depth값_오류));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬보드_댓글달기실패_등록오류));
        }

        return result;
    }


    /**
     * 팬보드 목록조회
     */
    public String callMemberFanboardList(P_FanboardListVo pFanboardListVo) {
        ProcedureVo procedureVo = new ProcedureVo(pFanboardListVo);
        List<P_FanboardListVo> fanboardVoList = profileDao.callMemberFanboardList(procedureVo);

        ProcedureOutputVo procedureOutputVo;
        if(DalbitUtil.isEmpty(fanboardVoList)){
            procedureOutputVo = null;
        }else{
            List<FanboardVo> outVoList = new ArrayList<>();
            for (int i=0; i<fanboardVoList.size(); i++){
                outVoList.add(new FanboardVo(fanboardVoList.get(i)));
            }
            procedureOutputVo = new ProcedureOutputVo(procedureVo, outVoList);
        }
        HashMap fanBoardList = new HashMap();
        fanBoardList.put("list", procedureOutputVo.getOutputBox());

        log.info("프로시저 응답 코드: {}", procedureOutputVo.getRet());
        log.info("프로시저 응답 데이타: {}", procedureOutputVo.getExt());
        log.info(" ### 프로시저 호출결과 ###");

        String result;
        if(Integer.parseInt(procedureVo.getRet()) > 0) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬보드조회성공, fanBoardList));
        }else if(Status.팬보드_댓글없음.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬보드_댓글없음));
        } else if(Status.팬보드_요청회원번호_회원아님.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬보드_요청회원번호_회원아님));
        } else if(Status.팬보드_스타회원번호_회원아님.getMessageCode().equals((procedureVo.getRet()))){
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬보드_스타회원번호_회원아님));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬보드_조회오류));
        }

        return result;
    }


    /**
     * 팬보드 삭제하기
     */
    public String callMemberFanboardDelete(P_FanboardDeleteVo p_fanboardDeleteVo) {
        ProcedureVo procedureVo = new ProcedureVo(p_fanboardDeleteVo);
        profileDao.callMemberFanboardDelete(procedureVo);

        String result;
        if(Status.팬보드_댓글삭제성공.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status. 팬보드_댓글삭제성공));
        } else if(Status.팬보드_댓글삭제실패_스타회원번호_회원아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬보드_댓글삭제실패_스타회원번호_회원아님));
        } else if(Status.팬보드_댓글삭제실패_삭제자회원번호_회원아님.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬보드_댓글삭제실패_삭제자회원번호_회원아님));
        } else if(Status.팬보드_댓글삭제실패_댓글인덱스번호_잘못된번호.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬보드_댓글삭제실패_요청인덱스번호_스타회원번호가다름));
        } else if(Status.팬보드_댓글삭제실패_요청인덱스번호_스타회원번호가다름.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬보드_댓글삭제실패_요청인덱스번호_스타회원번호가다름));
        } else if(Status.팬보드_댓글삭제실패_이미삭제됨.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬보드_댓글삭제실패_이미삭제됨));
        } else if(Status.팬보드_댓글삭제실패_삭제권한없음.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬보드_댓글삭제실패_삭제권한없음));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬보드_댓글삭제오류));
        }

        return result;
    }


    /**
     * 팬보드 대댓글 조회하기
     */
    public String callMemberFanboardReply(P_FanboardReplyVo pFanboardReplyVo) {
        ProcedureVo procedureVo = new ProcedureVo(pFanboardReplyVo);
        List<P_FanboardReplyVo> fanboardVoReplyList = profileDao.callMemberFanboardReply(procedureVo);


        ProcedureOutputVo procedureOutputVo;
        if(DalbitUtil.isEmpty(fanboardVoReplyList)){
            procedureOutputVo = null;
        }else{
            List<FanboardReplyOutVo> outVoList = new ArrayList<>();
            for (int i=0; i<fanboardVoReplyList.size(); i++){
                outVoList.add(new FanboardReplyOutVo(fanboardVoReplyList.get(i)));
            }
            procedureOutputVo = new ProcedureOutputVo(procedureVo, outVoList);
        }
        HashMap fanboardReplyList = new HashMap();
        fanboardReplyList.put("list", procedureOutputVo.getOutputBox());

        log.info("프로시저 응답 코드: {}", procedureOutputVo.getRet());
        log.info("프로시저 응답 데이타: {}", procedureOutputVo.getExt());
        log.info(" ### 프로시저 호출결과 ###");

        String result;
        if(Integer.parseInt(procedureOutputVo.getRet()) > 0) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬보드_대댓글조회성공, fanboardReplyList));
        } else if(Status.팬보드_대댓글조회실패_대댓글없음.getMessageCode().equals(procedureOutputVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬보드_대댓글조회실패_대댓글없음));
        } else if(Status.팬보드_대댓글조회실패_요청회원번호_회원아님.getMessageCode().equals(procedureOutputVo.getRet())) {
            result = gsonUtil.toJson((new JsonOutputVo(Status.팬보드_대댓글조회실패_요청회원번호_회원아님)));
        } else if(Status.팬보드_대댓글조회실패_스타회원번호_회원아님.getMessageCode().equals(procedureOutputVo.getRet())) {
            result = gsonUtil.toJson((new JsonOutputVo(Status.팬보드_대댓글조회실패_스타회원번호_회원아님)));
        } else {
            result = gsonUtil.toJson((new JsonOutputVo(Status.팬보드_대댓글조회오류)));
        }

        return result;
    }


    /**
     * 회원 팬 랭킹 조회
     */
    public String callMemberFanRanking(P_FanRankingVo pFanRankingVo) {
        ProcedureVo procedureVo = new ProcedureVo(pFanRankingVo);
        List<P_FanRankingVo> fanRankingVoList = profileDao.callMemberFanRanking(procedureVo);

        ProcedureOutputVo procedureOutputVo;
        if(DalbitUtil.isEmpty(fanRankingVoList)){
            procedureOutputVo = null;
        }else{
            List<FanRankingOutVo> outVoList = new ArrayList<>();
            for (int i=0; i<fanRankingVoList.size(); i++){
                outVoList.add(new FanRankingOutVo(fanRankingVoList.get(i)));
            }
            procedureOutputVo = new ProcedureOutputVo(procedureVo, outVoList);
        }

        HashMap resultMap = new Gson().fromJson(procedureOutputVo.getExt(), HashMap.class);
        HashMap fanRankingList = new HashMap();
        fanRankingList.put("list", procedureOutputVo.getOutputBox());
        fanRankingList.put("paging", new PagingVo(DalbitUtil.getIntMap(resultMap, "totalCnt"), DalbitUtil.getIntMap(resultMap, "pageNo"), DalbitUtil.getIntMap(resultMap, "pageCnt")));

        String result ="";
        if(Integer.parseInt(procedureOutputVo.getRet()) > 0) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬랭킹조회_성공, fanRankingList));
        } else if (procedureVo.getRet().equals(Status.팬랭킹조회_팬없음.getMessageCode())) {
            fanRankingList.put("list", new ArrayList<>());
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬랭킹조회_팬없음, fanRankingList));
        } else if (procedureVo.getRet().equals(Status.팬랭킹조회_요청회원_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬랭킹조회_요청회원_회원아님));
        } else if (procedureVo.getRet().equals(Status.팬랭킹조회_대상회원_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬랭킹조회_대상회원_회원아님));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬랭킹조회_실패));
        }
        return result;
    }


    /**
     *  회원 레벨 업 확인
     */
    public String callMemberLevelUpCheck(P_LevelUpCheckVo pLevelUpCheckVo) {
        ProcedureVo procedureVo = new ProcedureVo(pLevelUpCheckVo);
        profileDao.callMemberLevelUpCheck(procedureVo);

        HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
        HashMap returnMap = new HashMap();
        returnMap.put("oldLevel", DalbitUtil.getIntMap(resultMap, "oldLevel"));
        returnMap.put("newLevel", DalbitUtil.getIntMap(resultMap, "newLevel"));
        returnMap.put("oldGrade", DalbitUtil.getStringMap(resultMap, "oldGrade"));
        returnMap.put("newGrade", DalbitUtil.getStringMap(resultMap, "newGrade"));

        String result;
        if(procedureVo.getRet().equals(Status.레벨업확인_성공.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.레벨업확인_성공, returnMap));
        }else if(procedureVo.getRet().equals(Status.레벨업확인_요청회원번호_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.레벨업확인_요청회원번호_회원아님));
        }else if(procedureVo.getRet().equals(Status.레벨업확인_레벨업_없음.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.레벨업확인_레벨업_없음));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.레벨업확인_실패));
        }
        return result;
    }
}
