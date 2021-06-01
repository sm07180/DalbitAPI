package com.dalbit.oneYearEvent.service;

import com.dalbit.common.proc.OneYearEvent;
import com.dalbit.oneYearEvent.vo.OneYearDalVO;
import com.dalbit.oneYearEvent.vo.OneYearTailVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class OneYearEventService {
    @Autowired OneYearEvent oneYearEvent;

    public List<Object> getTailList(OneYearTailVO tailVO) {
        return oneYearEvent.getTailList(tailVO);
    }

    public int insertTail(OneYearTailVO tailVO) {
        /*tailVO.setTailMemNo("11584609037640");
        tailVO.setTailMemId("avseihll");
        tailVO.setTailMemSex("f");
        tailVO.setTailMemIp("125.136.6.80");
        tailVO.setTailConts("123122345234");
        tailVO.setTailLoginMedia("w");
        oneYearEvent.insTail(tailVO);*/
        return oneYearEvent.insTail(tailVO);
    }

    public int deleteTail(OneYearTailVO tailVO) {
        return oneYearEvent.deleteTail(tailVO);
    }

    public int updateTail(OneYearTailVO tailVO) {
        return oneYearEvent.updateTail(tailVO);
    }

    public int dalRcvCheck(OneYearDalVO dalVO) {
        return oneYearEvent.dalRcvCheck(dalVO);
    }

    public Map<String, Object> dalInsAndLogIns(OneYearDalVO dalVO) {
        return oneYearEvent.dalInsAndLogIns(dalVO);
    }
}
