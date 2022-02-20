package com.dalbit.broadcast.service;

import com.dalbit.broadcast.dao.VoteDao;
import com.dalbit.broadcast.vo.VoteResultVo;
import com.dalbit.broadcast.vo.request.VoteRequestVo;
import com.dalbit.util.DBUtil;
import com.dalbit.util.DalbitUtil;
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
            int result = voteDao.pRoomVoteIns(voteRequestVo);
            returnMap.put("result", result);
        } catch (Exception e) {
            log.error("VoteService insVote Error => {}", e.getMessage());
        }
        return returnMap;
    }
    public Map<String, Object> insVoteItem(VoteRequestVo voteRequestVo) {
        Map<String, Object> returnMap = new HashMap<>();
        try {
            int result = voteDao.pRoomVoteItemIns(voteRequestVo);
            returnMap.put("result", result);
        } catch (Exception e) {
            log.error("VoteService insVoteItem Error => {}", e.getMessage());
        }
        return returnMap;
    }
    public Map<String, Object> delVote(VoteRequestVo voteRequestVo) {
        Map<String, Object> returnMap = new HashMap<>();

        try {
            int result = voteDao.pRoomVoteDel(voteRequestVo);
            returnMap.put("result", result);
        } catch (Exception e) {
            log.error("VoteService delVote Error => {}", e.getMessage());
        }
        return returnMap;
    }
    public Map<String, Object> getVoteList(VoteRequestVo voteRequestVo) {
        Map<String, Object> returnMap = new HashMap<>();

        try {
            List<Object> pRoomVoteList = voteDao.pRoomVoteList(voteRequestVo);
            Integer cnt = DBUtil.getData(pRoomVoteList, 0, Integer.class);
            List<VoteResultVo> list = DBUtil.getList(pRoomVoteList, 1, VoteResultVo.class);
            returnMap.put("cnt", cnt);
            returnMap.put("list", list);
        } catch (Exception e) {
            log.error("VoteService getVoteList Error => {}", e.getMessage());
        }
        return returnMap;
    }
    public Map<String, Object> getVoteSel(VoteRequestVo voteRequestVo) {
        Map<String, Object> returnMap = new HashMap<>();

        try {
            List<?> resultSets = voteDao.pRoomVoteSel(voteRequestVo);
            if(!DalbitUtil.isEmpty(resultSets)){
                VoteResultVo info = DBUtil.getData(resultSets, 0, VoteResultVo.class);
                List<VoteResultVo> list = DBUtil.getList(resultSets, 1, VoteResultVo.class);
                returnMap.put("info", info);
                returnMap.put("list", list);
            }
        } catch (Exception e) {
            log.error("VoteService getVoteSel Error => {}", e.getMessage());
        }
        return returnMap;
    }

}
