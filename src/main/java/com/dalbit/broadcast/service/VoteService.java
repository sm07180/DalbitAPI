package com.dalbit.broadcast.service;

import com.dalbit.broadcast.dao.VoteDao;
import com.dalbit.broadcast.vo.VoteResultVo;
import com.dalbit.broadcast.vo.request.VoteRequestVo;
import com.dalbit.common.code.Status;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.socket.service.SocketService;
import com.dalbit.util.DBUtil;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
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
    @Autowired
    GsonUtil gsonUtil;

    public String insVote(VoteRequestVo voteRequestVo, HttpServletRequest request) {
        if (StringUtils.isEmpty(voteRequestVo.getMemNo())
                || StringUtils.isEmpty(voteRequestVo.getRoomNo())
                || StringUtils.isEmpty(voteRequestVo.getVoteTitle())
                || StringUtils.isEmpty(voteRequestVo.getVoteAnonyYn())
                || StringUtils.isEmpty(voteRequestVo.getVoteDupliYn())
                || voteRequestVo.getVoteItemNames().isEmpty()
                || voteRequestVo.getVoteItemCnt() < 1
                || voteRequestVo.getEndTime() < 1
        ) {
            return gsonUtil.toJson(new JsonOutputVo(Status.투표_생성_파리미터, voteRequestVo));
        }
        int voteNo = 0;
        try {
            voteNo = voteDao.pRoomVoteIns(voteRequestVo);
            if (voteNo < 1) {
                return gsonUtil.toJson(new JsonOutputVo(Status.투표_생성_에러, voteNo));
            }
            int finalVoteNo = voteNo;
            voteRequestVo.getVoteItemNames().forEach(s -> {
                voteDao.pRoomVoteItemIns(finalVoteNo, voteRequestVo.getMemNo(), voteRequestVo.getRoomNo(), s);
            });
            socketService.reqPopVote(voteRequestVo.getRoomNo(), voteRequestVo, DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request));
        } catch (Exception e) {
            log.error("VoteService insVote Error => {}", e.getMessage());
        }
        return gsonUtil.toJson(new JsonOutputVo(Status.투표_생성, voteNo));
    }

    public String insMemVote(VoteRequestVo voteRequestVo) {
        if (StringUtils.isEmpty(voteRequestVo.getMemNo())
                || StringUtils.isEmpty(voteRequestVo.getPmemNo())
                || StringUtils.isEmpty(voteRequestVo.getRoomNo())
                || StringUtils.isEmpty(voteRequestVo.getVoteNo())
                || StringUtils.isEmpty(voteRequestVo.getItemNo())
                || StringUtils.isEmpty(voteRequestVo.getVoteItemName())
        ) {
            return gsonUtil.toJson(new JsonOutputVo(Status.투표_투표처리_파라미터, voteRequestVo));
        }
        int result = 0;

        try {
            result = voteDao.pRoomVoteMemIns(voteRequestVo);
        } catch (Exception e) {
            log.error("VoteService insMemVote Error => {}", e.getMessage());
        }
        return result > 0
                ? gsonUtil.toJson(new JsonOutputVo(Status.투표_투표처리, result))
                : gsonUtil.toJson(new JsonOutputVo(Status.투표_투표처리_에러, null));
    }

    public String delVote(VoteRequestVo voteRequestVo) {
        if (StringUtils.isEmpty(voteRequestVo.getMemNo())
                || StringUtils.isEmpty(voteRequestVo.getRoomNo())
                || StringUtils.isEmpty(voteRequestVo.getVoteNo())
        ) {
            return gsonUtil.toJson(new JsonOutputVo(Status.투표_삭제_파라미터, voteRequestVo));
        }
        int result = 0;
        try {
            result = voteDao.pRoomVoteDel(voteRequestVo);

        } catch (Exception e) {
            log.error("VoteService delVote Error => {}", e.getMessage());
        }
        return result > 0
                ? gsonUtil.toJson(new JsonOutputVo(Status.투표_삭제, result))
                : gsonUtil.toJson(new JsonOutputVo(Status.투표_삭제_에러, null));
    }

    public String getVoteList(VoteRequestVo voteRequestVo) {
        if (StringUtils.isEmpty(voteRequestVo.getMemNo())
                || StringUtils.isEmpty(voteRequestVo.getRoomNo())
                || StringUtils.isEmpty(voteRequestVo.getVoteSlct())
                || !("s".equals(voteRequestVo.getVoteSlct()) || "e".equals(voteRequestVo.getVoteSlct()))
        ) {
            return gsonUtil.toJson(new JsonOutputVo(Status.투표_리스트조회_파리미터, voteRequestVo));
        }

        Map<String, Object> returnMap = new HashMap<>();
        try {
            List<Object> pRoomVoteList = voteDao.pRoomVoteList(voteRequestVo);
            if (pRoomVoteList != null && !pRoomVoteList.isEmpty()) {
                Integer cnt = DBUtil.getData(pRoomVoteList, 0, Integer.class);
                List<VoteResultVo> list = DBUtil.getList(pRoomVoteList, 1, VoteResultVo.class);
                returnMap.put("cnt", cnt);
                returnMap.put("list", list);
            }
        } catch (Exception e) {
            log.error("VoteService getVoteList Error => {}", e.getMessage());
        }
        return returnMap.containsKey("cnt")
                ? gsonUtil.toJson(new JsonOutputVo(Status.투표_리스트조회, returnMap))
                : gsonUtil.toJson(new JsonOutputVo(Status.투표_리스트조회_에러, null));
    }

    public String getVoteSel(VoteRequestVo voteRequestVo) {
        if (StringUtils.isEmpty(voteRequestVo.getMemNo())
                || StringUtils.isEmpty(voteRequestVo.getRoomNo())
                || StringUtils.isEmpty(voteRequestVo.getVoteNo())
        ) {
            return gsonUtil.toJson(new JsonOutputVo(Status.투표_정보조회_파리미터, voteRequestVo));
        }

        VoteResultVo info = null;
        try {
            info = voteDao.pRoomVoteSel(voteRequestVo);
        } catch (Exception e) {
            log.error("VoteService getVoteSel Error => {}", e.getMessage());
        }
        return gsonUtil.toJson(new JsonOutputVo(Status.투표_정보조회, info));
    }

    public String getVoteDetailList(VoteRequestVo voteRequestVo) {
        if (StringUtils.isEmpty(voteRequestVo.getMemNo())
                || StringUtils.isEmpty(voteRequestVo.getPmemNo())
                || StringUtils.isEmpty(voteRequestVo.getRoomNo())
                || StringUtils.isEmpty(voteRequestVo.getVoteNo())
        ) {
            return gsonUtil.toJson(new JsonOutputVo(Status.투표_항목조회_파리미터, voteRequestVo));
        }

        List<VoteResultVo> list = null;
        try {
            list = voteDao.pRoomVoteDetailList(voteRequestVo);
        } catch (Exception e) {
            log.error("VoteService getVoteDetailList Error => {}", e.getMessage());
        }
        return list != null
                ? gsonUtil.toJson(new JsonOutputVo(Status.투표_항목조회, list))
                : gsonUtil.toJson(new JsonOutputVo(Status.투표_항목조회_에러, null));
    }
}
