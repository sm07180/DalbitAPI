package com.dalbit.broadcast.service;

import com.dalbit.broadcast.dao.VoteDao;
import com.dalbit.broadcast.vo.VoteResultVo;
import com.dalbit.broadcast.vo.request.VoteRequestVo;
import com.dalbit.common.code.Status;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.socket.service.SocketService;
import com.dalbit.util.DBUtil;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Member;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
            return gsonUtil.toJson(new JsonOutputVo(Status.투표_생성_파라미터, voteRequestVo));
        }
        int voteNo = 0;
        try {
            voteNo = voteDao.pRoomVoteIns(voteRequestVo);
            if (voteNo == 0) {
                return gsonUtil.toJson(new JsonOutputVo(Status.투표_생성_에러, voteNo));
            }else if (voteNo == -1) {
                return gsonUtil.toJson(new JsonOutputVo(Status.투표_생성_개수초과_에러, voteNo));
            }
            int finalVoteNo = voteNo;
            voteRequestVo.getVoteItemNames().forEach(s -> {
                voteDao.pRoomVoteItemIns(finalVoteNo, voteRequestVo.getMemNo(), voteRequestVo.getRoomNo(), s);
            });
            voteRequestVo.setVoteNo(String.valueOf(voteNo));
            VoteResultVo vo = voteDao.pRoomVoteSel(voteRequestVo);
            socketService.reqVoteByPass(voteRequestVo.getRoomNo(), "reqInsVote", vo, DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request));
        } catch (Exception e) {
            log.error("VoteService insVote Error => {}", e.getMessage());
        }
        return gsonUtil.toJson(new JsonOutputVo(Status.투표_생성, voteNo));
    }

    public String insMemVote(VoteRequestVo voteRequestVo, HttpServletRequest request) {
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
            if (result < 1) {
                return gsonUtil.toJson(new JsonOutputVo(Status.투표_투표처리_에러, result));
            }
            //socketService.reqVoteByPass(voteRequestVo.getRoomNo(), "reqInsMemVote", voteRequestVo, DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request));
        } catch (Exception e) {
            log.error("VoteService insMemVote Error => {}", e.getMessage());
        }
        return gsonUtil.toJson(new JsonOutputVo(Status.투표_투표처리, result));
    }

    public String delVote(VoteRequestVo voteRequestVo, HttpServletRequest request) {
        if (StringUtils.isEmpty(voteRequestVo.getMemNo())
                || StringUtils.isEmpty(voteRequestVo.getRoomNo())
                || StringUtils.isEmpty(voteRequestVo.getVoteNo())
        ) {
            return gsonUtil.toJson(new JsonOutputVo(Status.투표_삭제_파라미터, voteRequestVo));
        }
        int result = 0;
        try {
            result = voteDao.pRoomVoteDel(voteRequestVo);
            if (result < 1) {
                return gsonUtil.toJson(new JsonOutputVo(Status.투표_삭제_에러, result));
            }
            socketService.reqVoteByPass(voteRequestVo.getRoomNo(), "reqDelVote", voteRequestVo, DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request));
        } catch (Exception e) {
            log.error("VoteService delVote Error => {}", e.getMessage());
        }
        return gsonUtil.toJson(new JsonOutputVo(Status.투표_삭제, result));
    }

    public String endVote(VoteRequestVo voteRequestVo, HttpServletRequest request) {
        if (StringUtils.isEmpty(voteRequestVo.getMemNo())
                || StringUtils.isEmpty(voteRequestVo.getRoomNo())
                || StringUtils.isEmpty(voteRequestVo.getVoteNo())
                || StringUtils.isEmpty(voteRequestVo.getEndSlct())
                || !("a".equals(voteRequestVo.getEndSlct()) || "o".equals(voteRequestVo.getEndSlct()))
        ) {
            return gsonUtil.toJson(new JsonOutputVo(Status.투표_마감_파라미터, voteRequestVo));
        }
        int result = 0;
        try {
            result = voteDao.pRoomVoteEnd(voteRequestVo);
            if (result < 1) {
                if("a".equals(voteRequestVo.getEndSlct())){
                    return gsonUtil.toJson(new JsonOutputVo(Status.투표_전체_마감_에러, result));
                }
                if("o".equals(voteRequestVo.getEndSlct())){
                    return gsonUtil.toJson(new JsonOutputVo(Status.투표_단일_마감_에러, result));
                }

            }
            socketService.reqVoteByPass(voteRequestVo.getRoomNo(), "reqEndVote", voteRequestVo, DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request));
        } catch (Exception e) {
            log.error("VoteService endVote Error => {}", e.getMessage());
        }

        if("o".equals(voteRequestVo.getEndSlct())){
            return gsonUtil.toJson(new JsonOutputVo(Status.투표_단일_마감, result));
        }else {
            return gsonUtil.toJson(new JsonOutputVo(Status.투표_전체_마감, result));
        }
    }

    public String getVoteList(VoteRequestVo voteRequestVo, HttpServletRequest request) {
        if (StringUtils.isEmpty(voteRequestVo.getMemNo())
                || StringUtils.isEmpty(voteRequestVo.getRoomNo())
                || StringUtils.isEmpty(voteRequestVo.getVoteSlct())
                || !("s".equals(voteRequestVo.getVoteSlct()) || "e".equals(voteRequestVo.getVoteSlct()))
        ) {
            return gsonUtil.toJson(new JsonOutputVo(Status.투표_리스트조회_파라미터, voteRequestVo));
        }

        Map<String, Object> returnMap = new HashMap<>();
        try {
            voteRequestVo.setPmemNo(MemberVo.getMyMemNo(request));
            List<Object> pRoomVoteList = voteDao.pRoomVoteList(voteRequestVo);
            if (pRoomVoteList == null || pRoomVoteList.isEmpty()) {
                return gsonUtil.toJson(new JsonOutputVo(Status.투표_리스트조회_에러, null));
            }
            Integer cnt = DBUtil.getData(pRoomVoteList, 0, Integer.class);
            List<VoteResultVo> list = DBUtil.getList(pRoomVoteList, 1, VoteResultVo.class);
            returnMap.put("cnt", cnt);
            returnMap.put("list", list);
        } catch (Exception e) {
            log.error("VoteService getVoteList Error => {}", e.getMessage());
        }
        return gsonUtil.toJson(new JsonOutputVo(Status.투표_리스트조회, returnMap));
    }

    public String getVoteSel(VoteRequestVo voteRequestVo, HttpServletRequest request) {
        if (StringUtils.isEmpty(voteRequestVo.getMemNo())
                || StringUtils.isEmpty(voteRequestVo.getRoomNo())
                || StringUtils.isEmpty(voteRequestVo.getVoteNo())
        ) {
            return gsonUtil.toJson(new JsonOutputVo(Status.투표_정보조회_파라미터, voteRequestVo));
        }

        VoteResultVo info = null;
        try {
            voteRequestVo.setPmemNo(MemberVo.getMyMemNo(request));
            info = voteDao.pRoomVoteSel(voteRequestVo);
            if(info == null){
                return gsonUtil.toJson(new JsonOutputVo(Status.투표_정보조회_에러, null));
            }
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
            return gsonUtil.toJson(new JsonOutputVo(Status.투표_항목조회_파라미터, voteRequestVo));
        }

        List<VoteResultVo> list = null;
        try {
            list = voteDao.pRoomVoteDetailList(voteRequestVo);
            if(list == null){
                return gsonUtil.toJson(new JsonOutputVo(Status.투표_항목조회_에러, null));
            }
            list = setVoteRank(list);
        } catch (Exception e) {
            log.error("VoteService getVoteDetailList Error => {}", e.getMessage());
        }
        return gsonUtil.toJson(new JsonOutputVo(Status.투표_항목조회, list));
    }

    public String getVoteSelAndDetailList(VoteRequestVo voteRequestVo, HttpServletRequest request) {
        if (StringUtils.isEmpty(voteRequestVo.getMemNo())
                || StringUtils.isEmpty(voteRequestVo.getPmemNo())
                || StringUtils.isEmpty(voteRequestVo.getRoomNo())
                || StringUtils.isEmpty(voteRequestVo.getVoteNo())
        ) {
            return gsonUtil.toJson(new JsonOutputVo(Status.투표_항목과리스트_조회_파라미터, voteRequestVo));
        }
        VoteResultVo sel = null;
        List<VoteResultVo> detailList = null;
        try {
            detailList = voteDao.pRoomVoteDetailList(voteRequestVo);
            sel = voteDao.pRoomVoteSel(voteRequestVo);
            if(detailList == null || detailList.isEmpty() || sel == null){
                return gsonUtil.toJson(new JsonOutputVo(Status.투표_항목과리스트_조회_에러, null));
            }
            detailList = setVoteRank(detailList);
        } catch (Exception e) {
            log.error("VoteService getVoteDetailList Error => {}", e.getMessage());
        }

        Map<String, Object> map = new HashMap<>();
        map.put("sel", sel);
        map.put("detailList", detailList);

        return gsonUtil.toJson(new JsonOutputVo(Status.투표_항목과리스트_조회, map));
    }

    public boolean isVote(String roomNo, String memNo, String voteSlct, HttpServletRequest request){
        VoteRequestVo voteRequestVo = new VoteRequestVo();
        voteRequestVo.setRoomNo(roomNo);
        voteRequestVo.setMemNo(memNo);
        voteRequestVo.setVoteSlct(voteSlct);
        String voteList = getVoteList(voteRequestVo, request);
        ObjectMapper mapper = new ObjectMapper();
        boolean returnBoolean = false;
        try{
            Map<String, Object> map = mapper.readValue(voteList, Map.class);
            Map<String, Object> inner = (Map<String, Object>) map.get("data");
            Integer cnt = (Integer) inner.get("cnt");
            if(cnt > 0){
                returnBoolean = true;
            }
        }catch (Exception e){
            log.error("mapper.readValue error => {}", e.getMessage());
        }
        return returnBoolean;
    }


    private List<VoteResultVo> setVoteRank(List<VoteResultVo> list){
        list = list.stream()
                .sorted(Comparator.comparing(VoteResultVo::getVoteMemCnt).reversed())
                .collect(Collectors.toList());
        for (int i = 0; i < list.size(); i++) {
            VoteResultVo now = list.get(i);
            if(i==0){
                now.setRank(1);
                continue;
            }
            VoteResultVo prev = list.get(i-1);
            if(now.getVoteMemCnt().equals(prev.getVoteMemCnt())){
                now.setRank(prev.getRank());
            }else{
                now.setRank(prev.getRank()+1);
            }
        }
        list = list.stream()
                .sorted(Comparator.comparing(VoteResultVo::getItemNo))
                .collect(Collectors.toList());

        return list;
    }
}
