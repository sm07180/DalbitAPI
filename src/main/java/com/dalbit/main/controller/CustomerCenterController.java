package com.dalbit.main.controller;

import com.dalbit.common.code.CommonStatus;
import com.dalbit.common.code.CustomerStatus;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.exception.GlobalException;
import com.dalbit.main.service.CustomerCenterService;
import com.dalbit.main.vo.NoticeReadUpdVo;
import com.dalbit.main.vo.procedure.*;
import com.dalbit.main.vo.request.*;
import com.dalbit.member.service.MemberService;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.TokenCheckVo;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
@Scope("prototype")
public class CustomerCenterController {

    @Autowired
    CustomerCenterService customerCenterService;

    @Autowired
    MemberService memberService;

    @Autowired
    GsonUtil gsonUtil;

    /**
     * 고객센터 공지사항 목록 조회
     */
    @GetMapping("/center/notice")
    public String customerCenterNoticeList(@Valid NoticeListVo noticeListVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        //로그인 체크 && 로그인이면 회원 조회
        TokenCheckVo tokenCheckVo = DalbitUtil.isLogin(request) ? memberService.selectMemState(MemberVo.getMyMemNo(request)) : null;

        //회원 조회 시 결과가 없을 수도 있으므로 tokenCheckVo 한번 더 체크
        MemberVo memberVo = new MemberVo();
        if(!DalbitUtil.isEmpty(tokenCheckVo)){
            memberVo.setGender(tokenCheckVo.getMem_sex());
        }

        P_NoticeListVo apiData = new P_NoticeListVo(noticeListVo, request, memberVo);

        String result = customerCenterService.callNoticeList(apiData);

        return result;
    }

    @PostMapping("center/notice/read")
    public String callNoticeReadUpd(@Valid NoticeReadUpdVo noticeReadUpdVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        try {
            return customerCenterService.callNoticeReadUpd(noticeReadUpdVo, request);
        } catch (Exception e) {
            log.error("callNoticeReadUpd failed : {}", e);
            return gsonUtil.toJson(new JsonOutputVo(CustomerStatus.공지사항_읽음확인_실패));
        }
    }

    /**
     * 고객센터 공지사항 내용(상세) 조회
     */
    @GetMapping("/center/notice/detail")
    public String customerCenterNoticeView(@Valid NoticeDetailVo noticeDetailVo, BindingResult bindingResult) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_NoticeDetailVo apiData = new P_NoticeDetailVo(noticeDetailVo);

        String result = customerCenterService.callNoticeDetail(apiData);

        return result;
    }

    /**
     * 고객센터 FAQ 목록 조회
     */
    @GetMapping("/center/faq")
    public String customerCenterFaqList(@Valid FaqListVo faqListVo, BindingResult bindingResult) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_FaqListVo apiData = new P_FaqListVo(faqListVo);

        String result = customerCenterService.callFaqList(apiData);

        return result;
    }

    /**
     * 고객센터 FAQ 내용(상세) 조회
     */
    @GetMapping("/center/faq/detail")
    public String customerCenterFaqView(@Valid FaqDetailVo faqDetailVo, BindingResult bindingResult) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_FaqDetailVo apiData = new P_FaqDetailVo(faqDetailVo);

        String result = customerCenterService.callFaqDetail(apiData);

        return result;
    }


    /**
     * 고객센터 1:1 문의작성
     */
    @PostMapping("/center/qna/add")
    public String callQnaAdd(@Valid QnaVo qnaVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_QnaVo apiData = new P_QnaVo(qnaVo, request);

        String result = customerCenterService.callQnaAdd(apiData, request);
        return result;
    }


    /**
     * 고객센터 나의 문의목록 조회
     */
    @GetMapping("/center/qna/list")
    public String callQnaList(@Valid QnaListVo qnaListVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_QnaListVo apiData = new P_QnaListVo(qnaListVo, request);

        String result = customerCenterService.callQnaList(apiData);

        return result;
    }

    /**
     * 고객센터 나의 1:1문의 삭제
     */
    @PostMapping("/center/qna/del")
    public String callQnaDel(@Valid QnaDelVo qnaDelVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_QnaDelVo apiData = new P_QnaDelVo(qnaDelVo, request);

        String result = customerCenterService.callQnaDel(apiData);

        return result;
    }

    /**
     * 버전확인
     */
    @GetMapping("/center/version")
    public String checkVersion(HttpServletRequest request){
        return gsonUtil.toJson(new JsonOutputVo(CommonStatus.조회, customerCenterService.checkAppVersion(request)));
    }
}
