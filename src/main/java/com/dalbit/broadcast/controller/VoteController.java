package com.dalbit.broadcast.controller;

import com.dalbit.broadcast.service.VoteService;
import com.dalbit.broadcast.vo.request.VoteRequestVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/broad/vote")
@Scope("prototype")
public class VoteController {

    @Autowired
    VoteService voteService;

    @PostMapping("/insVote")
    public Object vote(@RequestBody VoteRequestVo voteRequestVo, HttpServletRequest request){
        return voteService.insVote(voteRequestVo);
    }
    @PostMapping("/insVoteItem")
    public Object insVoteItem(@RequestBody VoteRequestVo voteRequestVo, HttpServletRequest request){
        return voteService.insVoteItem(voteRequestVo);
    }
    @PostMapping("/delVote")
    public Object delVote(@RequestBody VoteRequestVo voteRequestVo, HttpServletRequest request){
        return voteService.delVote(voteRequestVo);
    }
    @PostMapping("/getVoteList")
    public Object getVoteList(@RequestBody VoteRequestVo voteRequestVo, HttpServletRequest request){
        return voteService.getVoteList(voteRequestVo);
    }
    @PostMapping("/getVoteSel")
    public Object getVoteSel(@RequestBody VoteRequestVo voteRequestVo, HttpServletRequest request){
        return voteService.getVoteSel(voteRequestVo);
    }
}
