package com.dalbit.broadcast.service;

import com.dalbit.broadcast.dao.VoteDao;
import com.dalbit.broadcast.vo.VoteResultVo;
import com.dalbit.broadcast.vo.request.VoteRequestVo;
import com.dalbit.socket.service.SocketService;
import com.dalbit.socket.vo.SocketVo;
import com.dalbit.util.DBUtil;
import com.dalbit.util.DalbitUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class VoteService {

    @Autowired
    VoteDao voteDao;
    @Autowired
    SocketService socketService;
    public Map<String, Object> insVote(VoteRequestVo voteRequestVo, HttpServletRequest request) {
        Map<String, Object> returnMap = new HashMap<>();
        try {
            int voteNo = voteDao.pRoomVoteIns(voteRequestVo);
            if(voteNo < 1){
                returnMap.put("result", voteNo);
                return returnMap;
            }
            voteRequestVo.getVoteItemNames().forEach(s -> {
                voteDao.pRoomVoteItemIns(voteNo, voteRequestVo.getMemNo(), voteRequestVo.getRoomNo(), s);
            });
            returnMap.put("result", voteNo);
            socketService.reqPopVote(voteRequestVo.getRoomNo(), voteRequestVo, DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request));
        } catch (Exception e) {
            log.error("VoteService insVote Error => {}", e.getMessage());
        }
        return returnMap;
    }

    public Map<String, Object> insMemVote(VoteRequestVo voteRequestVo) {
        Map<String, Object> returnMap = new HashMap<>();
        try {
            int result = voteDao.pRoomVoteMemIns(voteRequestVo);
            returnMap.put("result", result);
        } catch (Exception e) {
            log.error("VoteService insMemVote Error => {}", e.getMessage());
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
            VoteResultVo info = voteDao.pRoomVoteSel(voteRequestVo);
            returnMap.put("info", info);
        } catch (Exception e) {
            log.error("VoteService getVoteSel Error => {}", e.getMessage());
        }
        return returnMap;
    }

    public Map<String, Object> getVoteDetailList(VoteRequestVo voteRequestVo) {
        Map<String, Object> returnMap = new HashMap<>();
        try {
            List<VoteResultVo> list = voteDao.pRoomVoteDetailList(voteRequestVo);
            returnMap.put("list", list);
        } catch (Exception e) {
            log.error("VoteService getVoteDetailList Error => {}", e.getMessage());
        }
        return returnMap;
    }
}
