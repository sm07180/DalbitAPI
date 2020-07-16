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
     * 메인
     */
    @GetMapping("/main")
    public String main(HttpServletRequest request){
        return mainService.getMain(request);
    }

    /**
     * 팬 랭킹
     */
    @GetMapping("/rank/fan")
    public String mainFanRanking(@Valid MainFanRankingVo mainFanRankingVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_MainFanRankingVo apiData = new P_MainFanRankingVo(mainFanRankingVo, request);

        String result = mainService.callMainFanRanking(apiData);

        return result;
    }


    /**
     * DJ 랭킹
     */
    @GetMapping("/rank/dj")
    public String mainDjRanking(@Valid MainDjRankingVo mainDjRankingVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_MainDjRankingVo apiData = new P_MainDjRankingVo(mainDjRankingVo, request);

        String result = mainService.callMainDjRanking(apiData);

        return result;
    }

    /**
     * 배너
     */
    @GetMapping("/banner")
    public String banner(HttpServletRequest request) {
        return mainService.selectBanner(request);
    }

}
