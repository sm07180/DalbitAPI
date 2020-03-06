package com.dalbit.main.controller;

import com.dalbit.exception.GlobalException;
import com.dalbit.main.service.CustomerCenterService;
import com.dalbit.main.vo.procedure.P_NoticeDetailVo;
import com.dalbit.main.vo.procedure.P_NoticeListVo;
import com.dalbit.main.vo.request.NoticeDetailVo;
import com.dalbit.main.vo.request.NoticeListVo;
import com.dalbit.util.DalbitUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
public class CustomerCenterController {

    @Autowired
    CustomerCenterService customerCenterService;

    /**
     * 고객센터 공지사항 목록 조회
     */
    @GetMapping("/center/notice")
    public String customerCenterNoticeList(@Valid NoticeListVo noticeListVo, BindingResult bindingResult) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult);
        P_NoticeListVo apiData = new P_NoticeListVo(noticeListVo);

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
}
