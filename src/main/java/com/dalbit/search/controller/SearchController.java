package com.dalbit.search.controller;

import com.dalbit.exception.GlobalException;
import com.dalbit.search.service.SearchService;
import com.dalbit.search.vo.procedure.P_MemberSearchVo;
import com.dalbit.search.vo.request.SearchVo;
import com.dalbit.util.DalbitUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("search")
public class SearchController {

    @Autowired
    SearchService searchService;

    /**
     * 회원 닉네임 검색
     */
    @PostMapping("member")
    public String memberNickSearch(@Valid SearchVo searchVo, BindingResult bindingResult) throws GlobalException {

        DalbitUtil.throwValidaionException(bindingResult);

        P_MemberSearchVo apiData = new P_MemberSearchVo(searchVo);

        String result = searchService.callMemberNickSearch(apiData);

        return result;
    }
}
