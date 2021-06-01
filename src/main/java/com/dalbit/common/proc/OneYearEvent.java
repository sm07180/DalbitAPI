package com.dalbit.common.proc;

import com.dalbit.oneYearEvent.vo.OneYearDalVO;
import com.dalbit.oneYearEvent.vo.OneYearTailVO;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Component
@Repository
public interface OneYearEvent {

    @ResultMap({"ResultMap.integer", "ResultMap.map"})
    @Select("CALL rd_data.p_tb_event_year_tail_list(#{memNo}, #{pageNo}, #{pagePerCnt})")
    List<Object> getTailList(OneYearTailVO tailVO);

    @Select("CALL rd_data.p_tb_event_year_tail_ins(#{tailMemNo}, #{tailMemId}, #{tailMemSex}, #{tailMemIp}, #{tailConts}, #{tailLoginMedia})")
    int insTail(OneYearTailVO tailVO);

    @Select("CALL rd_data.p_tb_event_year_tail_upd(#{tailNo}, #{tailMemNo}, #{tailMemId}, #{tailMemSex}, #{tailMemIp}, #{tailConts}, #{tailLoginMedia})")
    int updateTail(OneYearTailVO tailVO);

    @Select("CALL rd_data.p_tb_event_year_tail_del(#{tailNo}, #{tailMemNo})")
    int deleteTail(OneYearTailVO tailVO);

    @Select("CALL rd_data.p_tb_event_year_costn_rcv_chk(#{memNo})")
    int dalRcvCheck(OneYearDalVO dalVO);

    @Select("CALL rd_data.p_tb_event_year_costn_rcv_ins(#{memNo})")
    Map<String, Object> dalInsAndLogIns(OneYearDalVO dalVO);
}
