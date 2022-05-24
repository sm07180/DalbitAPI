package com.dalbit.event.service;

import com.dalbit.common.code.EventStatus;
import com.dalbit.common.vo.ImageVo;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.common.vo.PagingVo;
import com.dalbit.event.dao.ElectricSignDao;
import com.dalbit.event.vo.ElectricSignDJListOutVo;
import com.dalbit.event.vo.ElectricSignDJListVo;
import com.dalbit.event.vo.ElectricSignFanListOutVo;
import com.dalbit.event.vo.ElectricSignFanListVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.DBUtil;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class ElectricSignService {
    @Autowired
    ElectricSignDao electricSignDao;
    @Autowired
    GsonUtil gsonUtil;

    /**
     * 전광판 이벤트 Dj 리스트
     */
    public String getElectricSignDjList(ElectricSignDJListVo param) {
        HashMap paramMap = new HashMap();
        List<Object> electricSignMultiRow = null;
        List<ElectricSignDJListOutVo> list = null;
        int cnt = 0;

        paramMap.put("pageNo", param.getPageNo());
        paramMap.put("pagePerCnt", param.getPagePerCnt());

        electricSignMultiRow = electricSignDao.pElectricSignDjList(paramMap);
        cnt = DBUtil.getData(electricSignMultiRow, 0, Integer.class);
        list = DBUtil.getList(electricSignMultiRow, 1, ElectricSignDJListOutVo.class);

        HashMap resultMap = new HashMap();
        if(DalbitUtil.isEmpty(electricSignMultiRow) || list.size() == 0) {
            resultMap.put("cnt", 0);
            resultMap.put("list", new ArrayList());
            resultMap.put("paging", new PagingVo(cnt, DalbitUtil.getIntMap(paramMap, "pageNo"), DalbitUtil.getIntMap(paramMap, "pageCnt")));
            return gsonUtil.toJson(new JsonOutputVo(EventStatus.전광판_디제이_조회_실패, resultMap));
        }

        for(int i = 0; i < list.size(); i++) {
            list.get(i).setProfImg(new ImageVo(list.get(i).getImage_profile(), list.get(i).getMem_sex(), DalbitUtil.getProperty("server.photo.url")));
        }

        resultMap.put("cnt", cnt);
        resultMap.put("list", list);
        resultMap.put("paging", new PagingVo(cnt, DalbitUtil.getIntMap(paramMap, "pageNo"), DalbitUtil.getIntMap(paramMap, "pageCnt")));

        return gsonUtil.toJson(new JsonOutputVo(EventStatus.전광판_디제이_조회_성공, resultMap));
    }

    /**
     * 전광판 이벤트 Dj 회원 정보
     */
    public String getElectricSignDjSel(ElectricSignDJListVo param, HttpServletRequest request) {
        HashMap paramMap = new HashMap();
        Long memNo = Long.parseLong(MemberVo.getMyMemNo(request));

        paramMap.put("memNo", memNo);
        ElectricSignDJListOutVo resultVo = electricSignDao.pElectricSignDjSel(paramMap);

        if(resultVo == null) {
            return gsonUtil.toJson(new JsonOutputVo(EventStatus.전광판_디제이_조회_실패, resultVo));
        }
        resultVo.setProfImg(new ImageVo(resultVo.getImage_profile(), resultVo.getMem_sex(), DalbitUtil.getProperty("server.photo.url")));

        if(resultVo != null) {
            return gsonUtil.toJson(new JsonOutputVo(EventStatus.전광판_디제이_조회_성공, resultVo));
        }
        return gsonUtil.toJson(new JsonOutputVo(EventStatus.전광판_디제이_조회_실패, resultVo));
    }

    /**
     * 전광판 이벤트 시청자 리스트
     */
    public String getElectricSignFanList(ElectricSignFanListVo param) {
        HashMap paramMap = new HashMap();
        List<Object> electricSignFanRow = null;
        List<ElectricSignFanListOutVo> list = null;
        int cnt = 0;

        paramMap.put("pageNo", param.getPageNo());
        paramMap.put("pagePerCnt", param.getPagePerCnt());

        electricSignFanRow = electricSignDao.pElectricSignFanList(paramMap);
        cnt = DBUtil.getData(electricSignFanRow, 0, Integer.class);
        list = DBUtil.getList(electricSignFanRow, 1, ElectricSignFanListOutVo.class);

        HashMap resultMap = new HashMap();
        if(DalbitUtil.isEmpty(electricSignFanRow) || list.size() == 0) {
            resultMap.put("cnt", 0);
            resultMap.put("list", new ArrayList());
            resultMap.put("paging", new PagingVo(cnt, DalbitUtil.getIntMap(paramMap, "pageNo"), DalbitUtil.getIntMap(paramMap, "pageCnt")));
            return gsonUtil.toJson(new JsonOutputVo(EventStatus.전광판_시청자_조회_실패, resultMap));
        }

        for(int i = 0; i < list.size(); i++) {
            list.get(i).setProfImg(new ImageVo(list.get(i).getImage_profile(), list.get(i).getMem_sex(), DalbitUtil.getProperty("server.photo.url")));
        }

        resultMap.put("cnt", cnt);
        resultMap.put("list", list);
        resultMap.put("paging", new PagingVo(cnt, DalbitUtil.getIntMap(paramMap, "pageNo"), DalbitUtil.getIntMap(paramMap, "pageCnt")));

        return gsonUtil.toJson(new JsonOutputVo(EventStatus.전광판_시청자_조회_성공, resultMap));
    }

    /**
     * 전광판 이벤트 시청자 회원 정보
     */
    public String getElectricSignFanSel(ElectricSignFanListVo param, HttpServletRequest request) {
        HashMap paramMap = new HashMap();
        Long memNo = Long.parseLong(MemberVo.getMyMemNo(request));

        paramMap.put("memNo", memNo);
        ElectricSignFanListOutVo resultVo = electricSignDao.pElectricSignFanSel(paramMap);

        if(resultVo == null) {
            return gsonUtil.toJson(new JsonOutputVo(EventStatus.전광판_시청자_조회_실패, resultVo));
        }
        resultVo.setProfImg(new ImageVo(resultVo.getImage_profile(), resultVo.getMem_sex(), DalbitUtil.getProperty("server.photo.url")));

        if(resultVo != null) {
            return gsonUtil.toJson(new JsonOutputVo(EventStatus.전광판_시청자_조회_성공, resultVo));
        }
        return gsonUtil.toJson(new JsonOutputVo(EventStatus.전광판_시청자_조회_실패, resultVo));
    }

}
