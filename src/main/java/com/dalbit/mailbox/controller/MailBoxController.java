package com.dalbit.mailbox.controller;

import com.dalbit.mailbox.service.MailBoxService;
import com.dalbit.mailbox.vo.procedure.*;
import com.dalbit.mailbox.vo.request.*;
import com.dalbit.exception.GlobalException;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/mailbox/chat")
public class MailBoxController {
    @Autowired
    GsonUtil gsonUtil;
    @Autowired
    MailBoxService mailBoxService;


    /**
     * 우체통 대화방 리스트
     */
    @GetMapping("/list")
    public String mailboxList(@Valid MailBoxListVo mailBoxListVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_MailBoxListVo apiData = new P_MailBoxListVo(mailBoxListVo, request);
        String result = mailBoxService.callMailboxList(apiData);
        return result;
    }



    /**
     * 우체통 대화방 추가 대상 리스트
     */
    @GetMapping("/target/list")
    public String mailboxChatTargetList(@Valid MailBoxAddTargetListVo mailBoxAddTargetListVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_MailBoxAddTargetListVo apiData = new P_MailBoxAddTargetListVo(mailBoxAddTargetListVo, request);
        String result = mailBoxService.callMailboxAddTargetList(apiData);
        return result;
    }


    /**
     * 우체통 대화방 대상 입장
     */
    @PostMapping("/enter")
    public String mailboxChatEnter(@Valid MailBoxEnterVo mailBoxEnterVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_MailBoxEnterVo apiData = new P_MailBoxEnterVo(mailBoxEnterVo, request);
        String result = mailBoxService.callMailboxChatEnter(apiData);
        return result;
    }


    /**
     * 우체통 대화방 나가기
     */
    @PostMapping("/exit")
    public String mailboxChatExit(@Valid MailBoxExitVo mailBoxExitVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_MailBoxExitVo apiData = new P_MailBoxExitVo(mailBoxExitVo, request);
        String result = mailBoxService.callMailboxChatExit(apiData);
        return result;
    }


    /**
     * 우체통 대화방 대화 전송
     */
    @PostMapping("/send")
    public String mailboxChatSend(@Valid MailBoxSendVo mailBoxSendVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_MailBoxSendVo apiData = new P_MailBoxSendVo(mailBoxSendVo, request);
        String result = mailBoxService.callMailboxChatSend(apiData, request);
        return result;
    }


    /**
     * 우체통 대화방 대화 읽음처리
     */
    @PostMapping("/read")
    public String mailboxChatRead(@Valid MailBoxReadVo mailBoxReadVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_MailBoxReadVo apiData = new P_MailBoxReadVo(mailBoxReadVo, request);
        String result = mailBoxService.callMailboxChatRead(apiData);
        return result;
    }


    /**
     * 우체통 대화방 대화 조회
     */
    @GetMapping("/msg")
    public String mailboxMsg(@Valid MailBoxMsgListVo mailBoxMsgListVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_MailBoxMsgListVo apiData = new P_MailBoxMsgListVo(mailBoxMsgListVo, request);
        String result = mailBoxService.callMailboxMsg(apiData);
        return result;
    }


    /**
     * 우체통 대화방 이미지 리스트
     */
    @GetMapping("/image/list")
    public String mailboxImageList(@Valid MailBoxImageListVo mailBoxImageListVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_MailBoxImageListVo apiData = new P_MailBoxImageListVo(mailBoxImageListVo, request);
        String result = mailBoxService.callMailboxImageList(apiData);
        return result;
    }


    /**
     * 우체통 대화방 이미지 삭제
     */
    @PostMapping("/image/delete")
    public String mailboxImageDelete(@Valid MailBoxImageDeleteVo mailBoxImageDeleteVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_MailBoxImageDeleteVo apiData = new P_MailBoxImageDeleteVo(mailBoxImageDeleteVo, request);
        String result = mailBoxService.callMailboxImageDelete(apiData, request);
        return result;
    }

    /**
     * 우체통 신규메세지 조회
     */
    @GetMapping("/new")
    public String mailboxIsNew(HttpServletRequest request){
        return mailBoxService.callMailboxUnreadCheck(request);
    }
}
