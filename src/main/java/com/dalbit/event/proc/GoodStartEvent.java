package com.dalbit.event.proc;

import com.dalbit.event.vo.GoodStartInfoVo;
import com.dalbit.event.vo.GoodStartRankBjMySelVo;
import com.dalbit.event.vo.GoodStartRankFanMySelVo;
import com.dalbit.event.vo.GoodStartRankNewBjMySelVo;
import com.dalbit.event.vo.request.GoodStartInputVo;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Component
@Repository
public interface GoodStartEvent {
   /**********************************************************************************************
   * @Method 설명 : 이벤트 BJ 순위
   * @작성일   : 2022-01-08
   * @작성자   : 박성민
   * @변경이력  :
   **********************************************************************************************/
   @ResultMap({"ResultMap.integer", "ResultMap.GoodStartRankVo"})
   @Select("CALL rd_data.p_evt_good_start_rank_bj_list(#{goodNo}, #{memNo}, #{pageNo}, #{pagePerCnt})")
   List<Object> goodStartRankBjList(GoodStartInputVo goodStartInputVo);

   /**********************************************************************************************
   * @Method 설명 : 이벤트 BJ 나의 순위
   * @작성일   : 2022-01-08
   * @작성자   : 박성민
   * @변경이력  :
   **********************************************************************************************/
   @Select("CALL rd_data.p_evt_good_start_rank_bj_my_sel(#{goodNo}, #{memNo})")
   GoodStartRankBjMySelVo goodStartRankBjMySel(GoodStartInputVo goodStartInputVo);

   /**********************************************************************************************
   * @Method 설명 : 이벤트 BJ 순위(신입)
   * @작성일   : 2022-01-08
   * @작성자   : 박성민
   * @변경이력  :
   **********************************************************************************************/
   @ResultMap({"ResultMap.integer", "ResultMap.GoodStartRankVo"})
   @Select("CALL rd_data.p_evt_good_start_rank_new_bj_list(#{goodNo}, #{memNo}, #{pageNo}, #{pagePerCnt})")
   List<Object> goodStartRankNewBjSel(GoodStartInputVo goodStartInputVo);

    /**********************************************************************************************
     * @Method 설명 : 이벤트 BJ 나의 순위(신입)
     * @작성일   : 2022-01-08
     * @작성자   : 박성민
     * @변경이력  :
     **********************************************************************************************/
    @Select("CALL rd_data.p_evt_good_start_rank_new_bj_my_sel(#{goodNo}, #{memNo})")
    GoodStartRankNewBjMySelVo goodStartRankNewBjMySel(GoodStartInputVo goodStartInputVo);


    /**********************************************************************************************
     * @Method 설명 : 이벤트 팬 순위
     * @작성일   : 2022-01-08
     * @작성자   : 박성민
     * @변경이력  :
     **********************************************************************************************/
    @ResultMap({"ResultMap.integer", "ResultMap.GoodStartRankVo"})
    @Select("CALL rd_data.p_evt_good_start_rank_fan_list(#{goodNo}, #{memNo}, #{pageNo}, #{pagePerCnt})")
    List<Object> goodStartRankFanList(GoodStartInputVo goodStartInputVo);

    /**********************************************************************************************
     * @Method 설명 : 이벤트 팬 나의 순위
     * @작성일   : 2022-01-08
     * @작성자   : 박성민
     * @변경이력  :
     **********************************************************************************************/
    @Select("CALL rd_data.p_evt_good_start_rank_fan_my_sel(#{goodNo}, #{memNo})")
    GoodStartRankFanMySelVo goodStartRankFanMySel(GoodStartInputVo goodStartInputVo);

    /**********************************************************************************************
     * @Method 설명 : 이벤트 일정 리스트(회차)
     * @작성일   : 2022-01-10
     * @작성자   : 박성민
     * @param  : noSlct : 일정구분[1:해당회차, 2,전체회차]
     **********************************************************************************************/
    @Select("CALL rd_data.p_evt_good_start_no_sel(#{noSlct})")
    List<GoodStartInfoVo> goodStartNoSel(int noSlct);
}
