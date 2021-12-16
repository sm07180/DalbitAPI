package com.dalbit.event.proc;

import com.dalbit.event.vo.DrawGiftVO;
import com.dalbit.event.vo.DrawTicketVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Component
@Repository
public interface DrawEvent {
    /**********************************************************************************************
     * @Method 설명 : 추억의 뽑기(이벤트) 당첨내역 조회
     * @작성일 : 2021-12-07
     * @작성자 : 이정혁
     * @변경이력 :
     * @Parameter :
     * @Return :
     **********************************************************************************************/
    @Select("CALL rd_data.p_evt_bbopgi_gift_use_my_stat_list(#{memNo})")
    List<DrawGiftVO> getDrawWinningInfo(@Param(value = "memNo") String memNo);

    /**********************************************************************************************
     * @Method 설명 : 추억의 뽑기(이벤트) 응모권 개수 조회
     * @작성일 : 2021-12-07
     * @작성자 : 이정혁
     * @변경이력 :
     * @Parameter :
     * @Return :
     **********************************************************************************************/ 
    @Select("CALL rd_data.p_evt_bbopgi_gift_my_sel(#{memNo})")
    DrawTicketVO getDrawTicketCnt(@Param(value = "memNo") String memNo);


    /**********************************************************************************************
     * @Method 설명 : 추억의 뽑기(이벤트) 뽑기 리스트 조회
     * @작성일 : 2021-12-07
     * @작성자 : 이정혁
     * @변경이력 :
     * @Parameter : memNo 		BIGINT		-- 회원번호
     * @Return :
     **********************************************************************************************/
    @ResultMap({"ResultMap.DrawGiftVo", "ResultMap.DrawListVo"})
    @Select("CALL rd_data.p_evt_bbopgi_gift_list(#{memNo})")
    List<Object> getDrawListInfo(@Param(value = "memNo") String memNo);

    /**********************************************************************************************
     * @Method 설명 : 뽑기 이벤트 응모권 사용
     * @작성일 : 2021-12-07
     * @작성자 : 이정혁
     * @변경이력 :
     * @Parameter : memNo 		BIGINT		-- 회원번호(신청자)
     *              bbopgiGiftNo 	BIGINT		-- 경품번호
     *              bbopgiGiftPosNo 	BIGINT		-- 경품위치번호
     * @Return :  s_return		INT		-- -1경품 응모권수 초과, 0: 에러, 1:정상
     **********************************************************************************************/
    @Select("CALL rd_data.p_evt_bbopgi_coupon_ins(#{memNo}, #{bbopgiGiftNo}, #{bbopgiGiftPosNo})")
    Integer putDrawSelect(@Param(value = "memNo") String memNo, @Param(value = "bbopgiGiftNo") Integer bbopgiGiftNo, @Param(value = "bbopgiGiftPosNo") Integer bbopgiGiftPosNo);
}
