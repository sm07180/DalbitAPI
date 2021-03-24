package com.dalbit.event.controller;

import com.dalbit.event.service.ChooseokService;
import com.dalbit.event.vo.procedure.P_ChooseokCheckVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/event/chooseok")
@Scope("prototype")
public class ChooseokController {

    @Autowired
    ChooseokService chooseokService;


    /**
     * 추석이벤트 참여 체크
     */
    @GetMapping("/check")
    public String chooseokCheck(HttpServletRequest request){

        P_ChooseokCheckVo apiData = new P_ChooseokCheckVo(request);
        String result = chooseokService.callChooseokCheck(apiData);
        return result;
    }

    /**
     * 추석이벤트 무료 달 지급
     */
    @GetMapping("/freeDal/check")
    public String chooseokFreeDalCheck(HttpServletRequest request) {
        P_ChooseokCheckVo apiData = new P_ChooseokCheckVo(request);
        String result = chooseokService.callChooseokFreeDalCheck(apiData);

        return result;
    }

    /**
     * 추석이벤트 구매 달 확인
     */
    @GetMapping("/purchase/select")
    public String chooseokPurchaseSelect(HttpServletRequest request) {
        P_ChooseokCheckVo apiData = new P_ChooseokCheckVo(request);
        String result = chooseokService.callChooseokPurchaseSelect(apiData);

        return result;
    }

    /**
     * 추석이벤트 구매 달에 따른 추가 보너스 지급
     */
    @GetMapping("/purchase/bonus")
    public String chooseokPurchaseBonus(HttpServletRequest request) {
        P_ChooseokCheckVo apiData = new P_ChooseokCheckVo(request);
        String result = chooseokService.callChooseokPurchaseBonus(apiData);

        return result;
    }
}
