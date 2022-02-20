package com.dalbit.broadcast.controller;

import com.dalbit.broadcast.service.MiniGameService;
import com.dalbit.broadcast.service.VoteService;
import com.dalbit.broadcast.vo.procedure.*;
import com.dalbit.broadcast.vo.request.*;
import com.dalbit.exception.GlobalException;
import com.dalbit.util.DalbitUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/broad/vote")
@Scope("prototype")
public class VoteController {

    @Autowired
    VoteService voteService;

    @GetMapping("/insVote")
    public Object vote(VoteRequestVo voteRequestVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        return voteService.insVote(voteRequestVo);
    }
    @GetMapping("/insVoteItem")
    public Object insVoteItem(VoteRequestVo voteRequestVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        return voteService.insVoteItem(voteRequestVo);
    }
    @GetMapping("/delVote")
    public Object delVote(VoteRequestVo voteRequestVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        return voteService.delVote(voteRequestVo);
    }
    @GetMapping("/getVoteList")
    public Object getVoteList(VoteRequestVo voteRequestVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        return voteService.getVoteList(voteRequestVo);
    }
    @GetMapping("/getVoteSel")
    public Object getVoteSel(@RequestBody VoteRequestVo voteRequestVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        return voteService.getVoteSel(voteRequestVo);
    }
}
