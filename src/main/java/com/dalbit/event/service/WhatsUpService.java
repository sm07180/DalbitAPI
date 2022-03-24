package com.dalbit.event.service;

import com.dalbit.common.code.Status;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.event.dao.WhatsUpDao;
import com.dalbit.event.vo.WhatsUpResultVo;
import com.dalbit.event.vo.request.WhatsUpRequestVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.socket.service.SocketService;
import com.dalbit.util.DBUtil;
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
public class WhatsUpService {

    @Autowired
    WhatsUpDao whatsUpDao;
    @Autowired
    SocketService socketService;
    @Autowired
    GsonUtil gsonUtil;

    public String getWhatsUpDjList(WhatsUpRequestVo whatsUpRequestVo, HttpServletRequest request) {
        if (StringUtils.isEmpty(whatsUpRequestVo.getSeqNo())
            ||  whatsUpRequestVo.getPageNo() == null || whatsUpRequestVo.getPageNo() < 1
            ||  whatsUpRequestVo.getPagePerCnt() == null || whatsUpRequestVo.getPagePerCnt() < 1
        ) {
            return gsonUtil.toJson(new JsonOutputVo(Status.와썹맨_DJ_리스트_파라미터, whatsUpRequestVo));
        }

        Map<String, Object> returnMap = new HashMap<>();
        try {
            List<Object> pEvtWassupManDjRankList = whatsUpDao.pEvtWassupManDjRankList(whatsUpRequestVo);
            if (pEvtWassupManDjRankList == null || pEvtWassupManDjRankList.isEmpty()) {
                return gsonUtil.toJson(new JsonOutputVo(Status.와썹맨_DJ_리스트, null));
            }
            Integer cnt = DBUtil.getData(pEvtWassupManDjRankList, 0, Integer.class);
            List<WhatsUpResultVo> list = DBUtil.getList(pEvtWassupManDjRankList, 1, WhatsUpResultVo.class);
            returnMap.put("cnt", cnt);
            returnMap.put("list", list);
        } catch (Exception e) {
            log.error("WhatsUpService getWhatsUpDjList Error => {}", e.getMessage());
        }
        return gsonUtil.toJson(new JsonOutputVo(Status.와썹맨_DJ_리스트, returnMap));
    }

    public String getWhatsUpDjSel(WhatsUpRequestVo whatsUpRequestVo, HttpServletRequest request) {
        if (StringUtils.isEmpty(whatsUpRequestVo.getSeqNo())) {
            return gsonUtil.toJson(new JsonOutputVo(Status.와썹맨_DJ_조회_파라미터, whatsUpRequestVo));
        }
        WhatsUpResultVo whatsUpResultVo = null;
        try {
            whatsUpRequestVo.setMemNo(MemberVo.getMyMemNo(request));
            whatsUpResultVo = whatsUpDao.pEvtWassupManDjRankSel(whatsUpRequestVo);
        } catch (Exception e) {
            log.error("WhatsUpService getWhatsUpDjSel Error => {}", e.getMessage());
        }
        return gsonUtil.toJson(new JsonOutputVo(Status.와썹맨_DJ_조회, whatsUpResultVo));
    }

    public String getWhatsUpNewMemberList(WhatsUpRequestVo whatsUpRequestVo, HttpServletRequest request) {
        if (StringUtils.isEmpty(whatsUpRequestVo.getSeqNo())
                ||  whatsUpRequestVo.getPageNo() == null || whatsUpRequestVo.getPageNo() < 1
                ||  whatsUpRequestVo.getPagePerCnt() == null || whatsUpRequestVo.getPagePerCnt() < 1
        ) {
            return gsonUtil.toJson(new JsonOutputVo(Status.와썹맨_신입_리스트_파라미터, whatsUpRequestVo));
        }

        Map<String, Object> returnMap = new HashMap<>();
        try {
            List<Object> pEvtWassupManNewMemRankList = whatsUpDao.pEvtWassupManNewMemRankList(whatsUpRequestVo);
            if (pEvtWassupManNewMemRankList == null || pEvtWassupManNewMemRankList.isEmpty()) {
                return gsonUtil.toJson(new JsonOutputVo(Status.와썹맨_신입_리스트, null));
            }
            Integer cnt = DBUtil.getData(pEvtWassupManNewMemRankList, 0, Integer.class);
            List<WhatsUpResultVo> list = DBUtil.getList(pEvtWassupManNewMemRankList, 1, WhatsUpResultVo.class);
            returnMap.put("cnt", cnt);
            returnMap.put("list", list);
        } catch (Exception e) {
            log.error("WhatsUpService getWhatsUpNewMemberList Error => {}", e.getMessage());
        }
        return gsonUtil.toJson(new JsonOutputVo(Status.와썹맨_신입_리스트, returnMap));
    }

    public String getWhatsUpNewMemberSel(WhatsUpRequestVo whatsUpRequestVo, HttpServletRequest request) {
        if (StringUtils.isEmpty(whatsUpRequestVo.getSeqNo())) {
            return gsonUtil.toJson(new JsonOutputVo(Status.와썹맨_신입_조회_파라미터, whatsUpRequestVo));
        }
        WhatsUpResultVo whatsUpResultVo = null;
        try {
            whatsUpRequestVo.setMemNo(MemberVo.getMyMemNo(request));
            whatsUpResultVo = whatsUpDao.pEvtWassupManNewMemRankSel(whatsUpRequestVo);
        } catch (Exception e) {
            log.error("WhatsUpService getWhatsUpNewMemberSel Error => {}", e.getMessage());
        }
        return gsonUtil.toJson(new JsonOutputVo(Status.와썹맨_신입_조회, whatsUpResultVo));
    }

}
