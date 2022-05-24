package com.dalbit.rank.controller;

import com.dalbit.common.vo.ResVO;
import com.dalbit.exception.GlobalException;
import com.dalbit.main.vo.procedure.P_MainRankingPageVo;
import com.dalbit.main.vo.procedure.P_MainTimeRankingPageVo;
import com.dalbit.main.vo.request.MainRankingPageVo;
import com.dalbit.main.vo.request.MainTimeRankingPageVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.procedure.P_MemberRankSettingVo;
import com.dalbit.member.vo.request.MemberRankSettingVo;
import com.dalbit.rank.service.RankService;
import com.dalbit.team.vo.TeamParamVo;
import com.dalbit.util.DalbitUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RankController {

    private final RankService rankService;

    //내랭킹
    @GetMapping("/rank/myRank")
    public String getMyRank(HttpServletRequest request) {
        return rankService.getMyRank(request);
    }

//    //타임랭킹
//    @GetMapping("/rank/list/time")
//    public String mainTimeRankingPage(@Valid MainTimeRankingPageVo mainTimeRankingPageVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
//        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
//        P_MainTimeRankingPageVo apiData = new P_MainTimeRankingPageVo(mainTimeRankingPageVo, request);
//
//        String result = mainService.mainTimeRankingPage(request, apiData);
//
//        return result;
//    }
//
//    //랭킹 [DJ, FAN, CUPID, 일간, 주간, 월간, 연간]
//    @GetMapping("/rank/list")
//    public String mainRankingPage(@Valid MainRankingPageVo mainRankingPageVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
//        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
//        P_MainRankingPageVo apiData = new P_MainRankingPageVo(mainRankingPageVo, request);
//
//        String result = mainService.mainRankingPage(request, apiData);
//
//        return result;
//    }

    //팀랭킹(이번주랭킹, 저번주1,2,3위)
    @GetMapping("/rank/list/team")
    public ResVO getTeamRank(){
        ResVO result = new ResVO();
        try {
            result = rankService.getTeamRank();
        } catch (Exception e) {
            log.error("RankController / getTeamRankWeekList => {}", e.getMessage());
            result.setFailResVO();
        }
        return  result;
    }

    //랭킹데이터 반영 ON/OFF
    @PostMapping("/rank/setting")
    public String rankSetting(@Valid MemberRankSettingVo memberRankSettingVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_MemberRankSettingVo apiData = new P_MemberRankSettingVo(memberRankSettingVo, request);
        return rankService.callRankSetting(apiData);
    }

    @GetMapping("/rank/apply")
    public String rankApply(HttpServletRequest request){
        String memNo = MemberVo.getMyMemNo(request);
        return rankService.rankApply(memNo);
    }

    /**********************************************************************************************
     * @Method 설명 : 파트너DJ리스트
     * @작성일 : 2022-04-19
     * @작성자 : 강알찬
     * @변경이력 :
     **********************************************************************************************/
    @PostMapping("/getPartnerDjList")
    public String getPartnerDjList(@RequestBody Map map, HttpServletRequest request){
        return rankService.getPartnerDjList(map, request);
    }

    /**********************************************************************************************
     * @Method 설명 : 내 스디 점수
     * @작성일 : 2022-04-19
     * @작성자 : 강알찬
     * @변경이력 :
     **********************************************************************************************/
    @PostMapping("/getStarDjScore")
    public String getStarDjScore(@RequestBody Map map, HttpServletRequest request){
        return rankService.getStarDjScore(map, request);
    }

    /**********************************************************************************************
     * @Method 설명 : 스타디제이 신청
     * @작성일 : 2022-04-19
     * @작성자 : 강알찬
     * @변경이력 :
     **********************************************************************************************/
    @RequestMapping("/starDjIns")
    public String starDjIns(Map map, HttpServletRequest request){
        return rankService.starDjIns(map, request);
    }

    /**********************************************************************************************
     * @Method 설명 : 스타디제이 약력
     * @작성일 : 2022-04-20
     * @작성자 : 강알찬
     * @변경이력 :
     **********************************************************************************************/
    @PostMapping("/getStarDjLog")
    public String getStarDjLog(@RequestBody Map map, HttpServletRequest request){
        return rankService.getStarDjLog(map, request);
    }
}
