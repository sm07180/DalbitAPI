package com.dalbit.main.controller;

import com.dalbit.exception.GlobalException;
import com.dalbit.main.service.MainService;
import com.dalbit.main.vo.procedure.P_MainDjRankingVo;
import com.dalbit.main.vo.procedure.P_MainFanRankingVo;
import com.dalbit.main.vo.procedure.P_MainMyDjVo;
import com.dalbit.main.vo.procedure.P_MainRecommandVo;
import com.dalbit.main.vo.request.MainDjRankingVo;
import com.dalbit.main.vo.request.MainFanRankingVo;
import com.dalbit.main.vo.request.MainMyDjVo;
import com.dalbit.util.DalbitUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
public class MainController {

    @Autowired
    MainService mainService;

    /**
     * 메인 TODO- 반환 서비스 추후 정의필요
     */
    @GetMapping("/main")
    public String main(){
        return "";
    }


    /**
     * 추천BJ 리스트
     */
    @GetMapping("/recommand")
    public String mainRecommandList(HttpServletRequest request){

        P_MainRecommandVo apiData = new P_MainRecommandVo();

        String result = mainService.callMainRecommandList(apiData);

        return result;
    }


    /**
     * 팬 랭킹
     */
    @GetMapping("/rank/fan")
    public String mainFanRanking(@Valid MainFanRankingVo mainFanRankingVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult);
        P_MainFanRankingVo apiData = new P_MainFanRankingVo(mainFanRankingVo, request);

        String result = mainService.callMainFanRanking(apiData);

        return result;
    }


    /**
     * DJ 랭킹
     */
    @GetMapping("/rank/dj")
    public String mainDjRanking(@Valid MainDjRankingVo mainDjRankingVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult);
        P_MainDjRankingVo apiData = new P_MainDjRankingVo(mainDjRankingVo, request);

        String result = mainService.callMainDjRanking(apiData);

        return result;
    }


    /**
     * 마이 DJ
     */
    @GetMapping("/my/dj")
    public String mainMyDjList(@Valid MainMyDjVo mainMyDjVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult);
        P_MainMyDjVo adiData = new P_MainMyDjVo(mainMyDjVo, request);

        String result = mainService.callMainMyDjList(adiData);
        return result;
    }


}
