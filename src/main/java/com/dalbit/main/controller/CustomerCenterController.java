package com.dalbit.main.controller;

import com.dalbit.exception.GlobalException;
import com.dalbit.main.service.CustomerCenterService;
import com.dalbit.main.vo.procedure.*;
import com.dalbit.main.vo.request.*;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
public class CustomerCenterController {

    @Autowired
    CustomerCenterService customerCenterService;

    @Autowired
    RedisUtil redisUtil;
    /**
     * 고객센터 공지사항 목록 조회
     */
    @GetMapping("/center/notice")
    public String customerCenterNoticeList(@Valid NoticeListVo noticeListVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult);

        MemberVo memberVo = redisUtil.getMemberInfo(request);
        P_NoticeListVo apiData = new P_NoticeListVo(noticeListVo, request, memberVo);

        String result = customerCenterService.callNoticeList(apiData);

        return result;
    }

    /**
     * 고객센터 공지사항 내용(상세) 조회
     */
    @GetMapping("/center/notice/detail")
    public String customerCenterNoticeView(@Valid NoticeDetailVo noticeDetailVo, BindingResult bindingResult) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult);
        P_NoticeDetailVo apiData = new P_NoticeDetailVo(noticeDetailVo);

        String result = customerCenterService.callNoticeDetail(apiData);

        return result;
    }

    /**
     * 고객센터 FAQ 목록 조회
     */
    @GetMapping("/center/faq")
    public String customerCenterFaqList(@Valid FaqListVo faqListVo, BindingResult bindingResult) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult);
        P_FaqListVo apiData = new P_FaqListVo(faqListVo);

        String result = customerCenterService.callFaqList(apiData);

        return result;
    }

    /**
     * 고객센터 FAQ 내용(상세) 조회
     */
    @GetMapping("/center/faq/detail")
    public String customerCenterFaqView(@Valid FaqDetailVo faqDetailVo, BindingResult bindingResult) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult);
        P_FaqDetailVo apiData = new P_FaqDetailVo(faqDetailVo);

        String result = customerCenterService.callFaqDetail(apiData);

        return result;
    }


    /**
     * 고객센터 1:1 문의작성
     */
    @PostMapping("/center/qna/add")
    public String callQnaAdd(@Valid QnaVo qnaVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult);
        P_QnaVo apiData = new P_QnaVo(qnaVo, request);

        String result = customerCenterService.callQnaAdd(apiData, request);
        return result;
    }


    /**
     * 고객센터 나의 문의목록 조회
     */
    @GetMapping("/center/qna/list")
    public String callQnaList(@Valid QnaListVo qnaListVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult);
        P_QnaListVo apiData = new P_QnaListVo(qnaListVo, request);

        String result = customerCenterService.callQnaList(apiData);

        return result;
    }
}
