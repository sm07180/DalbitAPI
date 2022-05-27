package com.dalbit.rank.service;

import com.dalbit.common.code.CommonStatus;
import com.dalbit.common.code.MemberStatus;
import com.dalbit.common.code.Status;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.common.vo.ResMessage;
import com.dalbit.common.vo.ResVO;
import com.dalbit.member.dao.MypageDao;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.rank.dao.RankDao;
import com.dalbit.rank.proc.Rank;
import com.dalbit.rank.proc.StarDjPage;
import com.dalbit.rank.vo.*;
import com.dalbit.rank.vo.procedure.P_MemberRankSettingVo;
import com.dalbit.rank.vo.procedure.P_RankListVo;
import com.dalbit.rank.vo.procedure.P_TimeRankListVo;
import com.dalbit.util.DBUtil;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class RankService {

    private final Rank rank;
    private final RankDao rankDao;
    private final GsonUtil gsonUtil;
    private final StarDjPage starDjPage;
    private final MypageDao mypageDao;

    //내랭킹
    public String getMyRank(HttpServletRequest request) {
        String memNo = MemberVo.getMyMemNo(request);
        List<MyRankVO> list = rank.getMyRank(memNo);
        if (memNo == null) {
            return gsonUtil.toJson(new JsonOutputVo(CommonStatus.공통_기본_요청회원_정보없음));
        } else if (list.size() > 0) {
            return gsonUtil.toJson(new JsonOutputVo(CommonStatus.공통_기본_성공, list));
        } else {
            return gsonUtil.toJson(new JsonOutputVo(CommonStatus.공통_기본_실패));
        }
    }

    //타임랭킹조회
    public String timeRankList(TimeRankListDTO timeRankListDTO, HttpServletRequest request) {
        HashMap<String, Object> result = new HashMap<>();

        //현재날짜 랭킹 조회
        P_TimeRankListVo currentApiData = new P_TimeRankListVo(timeRankListDTO, request);
        ProcedureVo procedureVo = new ProcedureVo(currentApiData);
        List<P_TimeRankListVo> timeRankList = rankDao.callTimeRankList(procedureVo);

        List<TimeRankListOutVO> timeRankListOutVOS = new ArrayList<>();
        for (P_TimeRankListVo timeRankListVo : timeRankList) {
            timeRankListOutVOS.add(new TimeRankListOutVO(timeRankListVo));
        }
        result.put("listCnt", timeRankListOutVOS.size());
        result.put("list", timeRankListOutVOS);

        //지난날짜 랭킹 조회(Top3)
        timeRankListDTO.setRankingDate(timeRankListDTO.getPrevRankingDate());
        timeRankListDTO.setRecords(3);

        P_TimeRankListVo prevApiData = new P_TimeRankListVo(timeRankListDTO, request);
        ProcedureVo prevProcedureVo = new ProcedureVo(prevApiData);
        List<P_TimeRankListVo> prevTimeRankList = rankDao.callTimeRankList(prevProcedureVo);

        List<TimeRankListOutVO> prevTimeRankListOutVOS = new ArrayList<>();
        for (P_TimeRankListVo prevTimeRankListVo : prevTimeRankList) {
            prevTimeRankListOutVOS.add(new TimeRankListOutVO(prevTimeRankListVo));
        }
        result.put("prevTop", prevTimeRankListOutVOS);

        if (procedureVo.getRet() != null && Integer.parseInt(procedureVo.getRet()) > 0) {
            return gsonUtil.toJson(new JsonOutputVo(CommonStatus.공통_기본_성공, result));
        } else {
            return gsonUtil.toJson(new JsonOutputVo(CommonStatus.공통_기본_실패));
        }
    }

    //랭킹조회
    public String rankList(RankListDTO rankListDTO, HttpServletRequest request) {
        HashMap<String, Object> result = new HashMap<>();

        //현재날짜 랭킹 조회
        P_RankListVo currentApiData = new P_RankListVo(rankListDTO, request);
        ProcedureVo procedureVo = new ProcedureVo(currentApiData);
        List<P_RankListVo> rankList = rankDao.callRankList(procedureVo);

        List<RankListOutVO> rankListOutVOS = new ArrayList<>();
        for (P_RankListVo p_rankListVo : rankList) {
            rankListOutVOS.add(new RankListOutVO(p_rankListVo));
        }
        result.put("listCnt", rankListOutVOS.size());
        result.put("list", rankListOutVOS);

        //지난날짜 랭킹 조회(Top3)
        rankListDTO.setRankingDate(rankListDTO.getPrevRankingDate());
        rankListDTO.setRecords(3);
        P_RankListVo prevApiData = new P_RankListVo(rankListDTO, request);
        ProcedureVo preProcedureVo = new ProcedureVo(prevApiData);
        List<P_RankListVo> prevRankList = rankDao.callRankList(preProcedureVo);

        List<RankListOutVO> prevRankListOutVOS = new ArrayList<>();
        for (P_RankListVo p_rankListVo : prevRankList) {
            prevRankListOutVOS.add(new RankListOutVO(p_rankListVo));
        }
        result.put("prevTop", prevRankListOutVOS);

        if (procedureVo.getRet() != null && Integer.parseInt(procedureVo.getRet()) > 0) {
            return gsonUtil.toJson(new JsonOutputVo(CommonStatus.공통_기본_성공, result));
        } else {
            return gsonUtil.toJson(new JsonOutputVo(CommonStatus.공통_기본_실패));
        }
    }

    //팀랭킹
    public ResVO getTeamRank(){
        ResVO resVO = new ResVO();
        HashMap<String, Object> resultMap = new HashMap<>();
        try {
            //현재날짜 랭킹 조회
            List<Object> object = rank.getTeamRankProc(this.getDate().get("week"), "0", 1, 500);
            Integer listCnt = DBUtil.getData(object, 0, Integer.class);
            List<TeamRankVO> list = DBUtil.getList(object, 1, TeamRankVO.class);
            resultMap.put("listCnt", listCnt);
            resultMap.put("list", list);

            //지난날짜 랭킹 조회(Top3)
            List<Object> preObject = rank.getTeamRankProc(this.getDate().get("prev"), "0", 1, 3);
            List<TeamRankVO> prevTop = DBUtil.getList(preObject, 1, TeamRankVO.class);
            resultMap.put("prevTop", prevTop);

            resVO.setResVO(ResMessage.C00000.getCode(), ResMessage.C00000.getCodeNM(), resultMap);
        } catch (Exception e) {
            log.error("RankService / getTeamRank => {}", e.getMessage());
            resVO.setResVO(ResMessage.C99999.getCode(), ResMessage.C99999.getCodeNM(), null);
        }
        return resVO;
    }


    private HashMap<String, String> getDate() {
        HashMap<String, String> date = new HashMap<>();
        LocalDateTime now = LocalDateTime.now();
        String week = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String prev = now.minusDays(7).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        date.put("week", week);
        date.put("prev", prev);
        return date;
    }

    // 팬 랭킹 참여 ON/OFF
    public String callRankSetting(P_MemberRankSettingVo pMemberRankSettingVo) {
        ProcedureVo procedureVo = new ProcedureVo(pMemberRankSettingVo);
        rankDao.callRankSetting(procedureVo);
        if(procedureVo.getRet().equals(CommonStatus.랭킹반영.getMessageCode())) {
            val resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            Status status = null;
            if(pMemberRankSettingVo.getApply_ranking() == 1){
                status = CommonStatus.랭킹반영;
            }else{
                status = CommonStatus.랭킹미반영;
            }
            val returnMap = new HashMap();
            returnMap.put("isRankData", DalbitUtil.getIntMap(resultMap, "apply_ranking") == 1);
            return gsonUtil.toJson(new JsonOutputVo(status, returnMap));
        } else if(procedureVo.getRet().equals(CommonStatus.랭킹반영설정_회원아님.getMessageCode())) {
            return gsonUtil.toJson(new JsonOutputVo(CommonStatus.랭킹반영설정_회원아님));
        } else {
            return gsonUtil.toJson(new JsonOutputVo(CommonStatus.랭킹반영설정_실패));
        }
    }

    // 팬 랭킹 참여/비참여 여부
    public String rankApply(String memNo) {
        RankApplyVO result = rank.rankApplyProc(memNo);
        return gsonUtil.toJson(new JsonOutputVo(CommonStatus.조회, result));
    }


    /**********************************************************************************************
     * @Method 설명 : 파트너DJ리스트
     * @작성일 : 2022-04-19
     * @작성자 : 강알찬
     * @변경이력 :
     **********************************************************************************************/
    public String getPartnerDjList(Map map, HttpServletRequest request){
        String memNo = MemberVo.getMyMemNo(request);
        map.put("memNo", memNo);
        List<Object> djResult = rank.getPartnerDjList(map);
        return gsonUtil.toJson(new JsonOutputVo(CommonStatus.조회, djResult));
    }

    /**********************************************************************************************
     * @Method 설명 : 내 스디 점수
     * @작성일 : 2022-04-19
     * @작성자 : 강알찬
     * @변경이력 :
     **********************************************************************************************/
    public String getStarDjScore(Map map, HttpServletRequest request){
        String memNo = MemberVo.getMyMemNo(request);
        map.put("memNo", memNo);
        StarDjPointVO result = starDjPage.getStarDjScore(map);
        return gsonUtil.toJson(new JsonOutputVo(CommonStatus.조회, result));
    }

    /**********************************************************************************************
     * @Method 설명 : 스타디제이 신청
     * @작성일 : 2022-04-19
     * @작성자 : 강알찬
     * @변경이력 :
     **********************************************************************************************/
    public String starDjIns(Map map, HttpServletRequest request){
        String result = gsonUtil.toJson(new JsonOutputVo(MemberStatus.스페셜DJ_신청실패));
        String memNo = MemberVo.getMyMemNo(request);
        map.put("memNo", memNo);
        try{
            HashMap starMap = mypageDao.selectExistsSpecialReq(memNo);
            if (starMap.get("is_already") != null){
                if (Integer.parseInt(starMap.get("is_already").toString()) < 1){
                    starDjPage.starDjIns(map);
                    result = gsonUtil.toJson(new JsonOutputVo(MemberStatus.스페셜DJ_신청성공));
                } else {
                    result = gsonUtil.toJson(new JsonOutputVo(MemberStatus.스페셜DJ_신청실패));
                }
            } else {
                result = gsonUtil.toJson(new JsonOutputVo(MemberStatus.스페셜DJ_신청실패));
            }

        }catch(Exception e){
            log.info("스페셜 DJ DB 오류 {}", e.getMessage());
        }
        return result;
    }

    /**********************************************************************************************
     * @Method 설명 : 스타디제이 약력
     * @작성일 : 2022-04-20
     * @작성자 : 강알찬
     * @변경이력 :
     **********************************************************************************************/
    public String getStarDjLog(Map map, HttpServletRequest request){
        String memNo = MemberVo.getMyMemNo(request);
        if (map.get("memNo") == null){
            map.put("memNo", memNo);
        }
        List<Object> result = starDjPage.getStarDjLog(map);
        return gsonUtil.toJson(new JsonOutputVo(CommonStatus.조회, result));
    }

}
