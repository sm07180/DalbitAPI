package com.dalbit.oneYearEvent.action;

import com.dalbit.common.code.Status;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.exception.GlobalException;
import com.dalbit.member.service.MemberService;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.TokenCheckVo;
import com.dalbit.oneYearEvent.service.OneYearEventService;
import com.dalbit.oneYearEvent.vo.OneYearDalVO;
import com.dalbit.oneYearEvent.vo.OneYearTailVO;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

@Slf4j
@RestController
@Scope("prototype")
@RequestMapping("/oneYear")
public class OneYearEventAction {
    @Autowired OneYearEventService oneYearEventService;
    @Autowired GsonUtil gsonUtil;
    @Autowired MemberService memberService;

    @PostMapping("/tail/list")
    public String getTailList(@Valid OneYearTailVO tailVO, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        //로그인 체크 && 로그인이면 회원 조회
        TokenCheckVo tokenCheckVo = DalbitUtil.isLogin(request) ? memberService.selectMemState(MemberVo.getMyMemNo(request)) : null;

        if(!DalbitUtil.isEmpty(tokenCheckVo)){
            tailVO.setMemNo(tokenCheckVo.getMem_no());
        } else {
            return gsonUtil.toJson(new JsonOutputVo(Status.댓글목록조회_실패));
        }

        return gsonUtil.toJson(new JsonOutputVo(Status.댓글목록조회_성공, oneYearEventService.getTailList(tailVO)));
    }

    @PostMapping("/tail/ins")
    public String insertTail(@Valid OneYearTailVO tailVO, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        //로그인 체크 && 로그인이면 회원 조회
        TokenCheckVo tokenCheckVo = DalbitUtil.isLogin(request) ? memberService.selectMemState(MemberVo.getMyMemNo(request)) : null;

        if(!DalbitUtil.isEmpty(tokenCheckVo)){
            tailVO.setTailMemNo(tokenCheckVo.getMem_no());
        } else {
            return gsonUtil.toJson(new JsonOutputVo(Status.댓글등록_실패));
        }

        int result = oneYearEventService.insertTail(tailVO);

        if (result > 0) {
            return gsonUtil.toJson(new JsonOutputVo(Status.댓글등록_성공, result));
        } else {
            return gsonUtil.toJson(new JsonOutputVo(Status.댓글등록_실패));
        }
    }

    @PostMapping("/tail/upd")
    public String updateTail(@Valid OneYearTailVO tailVO, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        //로그인 체크 && 로그인이면 회원 조회
        TokenCheckVo tokenCheckVo = DalbitUtil.isLogin(request) ? memberService.selectMemState(MemberVo.getMyMemNo(request)) : null;

        if(!DalbitUtil.isEmpty(tokenCheckVo)){
            tailVO.setTailMemNo(tokenCheckVo.getMem_no());
        } else {
            return gsonUtil.toJson(new JsonOutputVo(Status.댓글수정_실패));
        }

        int result = oneYearEventService.updateTail(tailVO);

        if (result > 0) {
            return gsonUtil.toJson(new JsonOutputVo(Status.댓글수정_성공, result));
        } else {
            return gsonUtil.toJson(new JsonOutputVo(Status.댓글수정_실패));
        }
    }

    @PostMapping("/tail/del")
    public String deleteTail(@Valid OneYearTailVO tailVO, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        //로그인 체크 && 로그인이면 회원 조회
        TokenCheckVo tokenCheckVo = DalbitUtil.isLogin(request) ? memberService.selectMemState(MemberVo.getMyMemNo(request)) : null;

        if(!DalbitUtil.isEmpty(tokenCheckVo)){
            tailVO.setTailMemNo(tokenCheckVo.getMem_no());
        } else {
            return gsonUtil.toJson(new JsonOutputVo(Status.댓글삭제_실패));
        }

        int result = oneYearEventService.deleteTail(tailVO);

        if (result > 0) {
            return gsonUtil.toJson(new JsonOutputVo(Status.댓글삭제_성공, result));
        } else {
            return gsonUtil.toJson(new JsonOutputVo(Status.댓글삭제_실패));
        }
    }

    @PostMapping("/dal/check")
    public String dalRcvCheck(@Valid OneYearDalVO dalVO, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        //로그인 체크 && 로그인이면 회원 조회
        TokenCheckVo tokenCheckVo = DalbitUtil.isLogin(request) ? memberService.selectMemState(MemberVo.getMyMemNo(request)) : null;

        if(!DalbitUtil.isEmpty(tokenCheckVo)){
            dalVO.setMemNo(tokenCheckVo.getMem_no());
        } else {
            return gsonUtil.toJson(new JsonOutputVo(Status.달지급조회_실패));
        }

        int result = oneYearEventService.dalRcvCheck(dalVO);

        if (result == 1) {
            return gsonUtil.toJson(new JsonOutputVo(Status.달지급조회_성공, result));
        } else if (result == -1) {
            return gsonUtil.toJson(new JsonOutputVo(Status.달지급조회_이미받음, result));
        } else if (result == -2) {
            return gsonUtil.toJson(new JsonOutputVo(Status.달지급조회_레벨안됨, result));
        } else {
            return gsonUtil.toJson(new JsonOutputVo(Status.달지급조회_실패));
        }
    }

    @PostMapping("/dal/ins")
    public String dalInsAndLogIns(@Valid OneYearDalVO dalVO, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        //로그인 체크 && 로그인이면 회원 조회
        TokenCheckVo tokenCheckVo = DalbitUtil.isLogin(request) ? memberService.selectMemState(MemberVo.getMyMemNo(request)) : null;

        if(!DalbitUtil.isEmpty(tokenCheckVo)){
            dalVO.setMemNo(tokenCheckVo.getMem_no());
        } else {
            return gsonUtil.toJson(new JsonOutputVo(Status.달지급처리및로그기록_실패));
        }

        Map<String, Object> result = oneYearEventService.dalInsAndLogIns(dalVO);
        int sReturn = Integer.parseInt(String.valueOf(result.get("s_return")));

        if (sReturn == 1) {
            return gsonUtil.toJson(new JsonOutputVo(Status.달지급처리및로그기록_성공, result));
        } else if (sReturn == -1) {
            return gsonUtil.toJson(new JsonOutputVo(Status.달지급처리및로그기록_이미받음, result));
        } else if (sReturn == -2) {
            return gsonUtil.toJson(new JsonOutputVo(Status.달지급처리및로그기록_레벨안됨, result));
        } else {
            return gsonUtil.toJson(new JsonOutputVo(Status.달지급처리및로그기록_실패));
        }
    }
}