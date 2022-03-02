package com.dalbit.event.proc;

import com.dalbit.event.vo.request.PoemEventReqVo;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PoemEvent {

    //# 리브랜딩 댓글 목록
    @ResultMap({"ResultMap.integer", "ResultMap.PoemEventResVo"})
    @Select("CALL rd_data.p_tb_event_rbd_tail_list(#{memNo}, #{pageNo}, #{pagePerCnt})")
    List<Object> pTbEventRbdTailList(String memNo, Integer pageNo, Integer pagePerCnt);

    //# 리브랜딩 댓글 등록
    @Select("CALL rd_data.p_tb_event_rbd_tail_ins(#{tailMemNo}, #{tailMemId}, #{tailMemSex}, #{tailMemIp}, #{tailConts}, #{tailLoginMedia})")
    int pTbEventRbdTailIns(PoemEventReqVo poemEventReqVo);

    //# 리브랜딩 댓글 변경
    @Select("CALL rd_data.p_tb_event_rbd_tail_upd(#{tailNo}, #{tailMemNo}, #{tailMemId}, #{tailMemSex}, #{tailMemIp}, #{tailConts}, #{tailLoginMedia})")
    int pTbEventRbdTailUpd(PoemEventReqVo poemEventReqVo);

    //# 리브랜딩 댓글 삭제
    @Select("CALL rd_data.p_tb_event_rbd_tail_del(#{tailNo}, #{tailMemNo})")
    int pTbEventRbdTailDel(String tailNo, String tailMemNo);
}
