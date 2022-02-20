package com.dalbit.broadcast.service;

import com.dalbit.broadcast.dao.VoteDao;
import com.dalbit.broadcast.vo.request.VoteRequestVo;
import com.dalbit.util.DBUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class VoteService {

    @Autowired
    VoteDao voteDao;

    public Map<String, Object> insVote(VoteRequestVo voteRequestVo) {
        Map<String, Object> returnMap = new HashMap<>();

        try {
            voteDao.pRoomVoteIns(voteRequestVo);
        } catch (Exception e) {
            log.error("VoteService insVote Error => {}", e.getMessage());
        }
        return returnMap;
    }
    public Map<String, Object> insVoteItem(VoteRequestVo voteRequestVo) {
        Map<String, Object> returnMap = new HashMap<>();
        try {
            int result = voteDao.pRoomVoteItemIns(voteRequestVo);
            returnMap.put("data", result);
        } catch (Exception e) {
            log.error("VoteService insVoteItem Error => {}", e.getMessage());
        }
        return returnMap;
    }
    public Map<String, Object> delVote(VoteRequestVo voteRequestVo) {
        Map<String, Object> returnMap = new HashMap<>();

        try {
            voteDao.pRoomVoteDel(voteRequestVo);
        } catch (Exception e) {
            log.error("VoteService delVote Error => {}", e.getMessage());
        }
        return returnMap;
    }
    public Map<String, Object> getVoteList(VoteRequestVo voteRequestVo) {
        Map<String, Object> returnMap = new HashMap<>();

        try {
            voteDao.pRoomVoteList(voteRequestVo);
        } catch (Exception e) {
            log.error("VoteService getVoteList Error => {}", e.getMessage());
        }
        return returnMap;
    }
    public Map<String, Object> getVoteSel(VoteRequestVo voteRequestVo) {
        Map<String, Object> returnMap = new HashMap<>();

        try {
            List<Object> pRoomVoteSel = voteDao.pRoomVoteSel(voteRequestVo);
            log.error("{}", pRoomVoteSel);
//            List<Object> cnt = DBUtil.getList(pRoomVoteSel, 0, List.class);
        } catch (Exception e) {
            log.error("VoteService getVoteSel Error => {}", e.getMessage());
        }
        return returnMap;
    }

}
