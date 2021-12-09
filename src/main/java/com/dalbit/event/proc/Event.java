package com.dalbit.event.proc;

import com.dalbit.event.vo.ItemInsVo;
import com.dalbit.event.vo.request.*;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Component
@Repository
public interface Event {
    /**********************************************************************************************
    * @Method 설명 : 아이템 지급[서비스]
    * @작성일   : 2021-11-04
    * @작성자   : 박성민
    * @param  :
    * memNo 		BIGINT		-- 회원번호
    * ,itemType 	BIGINT		-- 1:부스터
    * ,itemState  	BIGINT		-- 1: 지급, 2: 사용, 3: 차감
    * ,itemCnt 		BIGINT		-- 아이템 수
    * ,opName 	VARCHAR(50)	 -- 처리내용
    * @return
    * s_return		INT		--  -3: 이벤트 종료, -1회원 및 아이템수 없음, 0:에러, 1:정상
    **********************************************************************************************/
    @Select("CALL rd_data.p_item_ins(#{memNo}, #{itemType}, #{itemState}, #{itemCnt}, #{opName})")
    int eventItemIns(ItemInsVo param);

    /* 깐부 이벤트~~~ */
    /**********************************************************************************************
     * @Method 설명 : 깐부 이벤트 회차번호
     * @작성일   : 2021-12-01
     * @작성자   : 박성민
     * @param  :
     * @return
     * gganbu_no - 회차번호
     * start_date - 시작일자
     * end_date - 종료일자
     * ins_date - 등록일자
     **********************************************************************************************/
    @Select("CALL rd_data.p_evt_gganbu_no_sel()")
    GganbuRoundInfoReqVo getGganbuRoundInfo();

    /**********************************************************************************************
     * @Method 설명 : 깐부 신청
     * @작성일   : 2021-12-01
     * @작성자   : 박성민
     * @param  :
     * gganbuNo - 회차번호
     * memNo - 회원번호(신청자)
     * ptrMemNo - 회원번호(대상자)
     * @return
     * s_return:  -7: 상대가 나에게 이미신청, -6: 동일회원에게 이미신청, -5:회원아님, -4: 신청대상자 깐부있음,
     *            -3: 신청건수초과, -2: 신청대상자 탈퇴&정지회원, -1: 이벤트기간 아님, 0: 에러, 1:정상
     **********************************************************************************************/
    @Select("CALL rd_data.p_evt_gganbu_mem_req_ins(#{gganbuNo}, #{memNo}, #{ptrMemNo})")
    int gganbuMemReqIns(GganbuMemReqInsVo param);

    /**********************************************************************************************
     * @Method 설명 : 깐부 신청 취소
     * @작성일   : 2021-12-01
     * @작성자   : 박성민
     * @param  :
     * gganbuNo - 회차번호
     * memNo - 회원번호(신청자)
     * ptrMemNo - 회원번호(대상자)
     * @return
     * s_return: -3: 신청내역없음, -2:이미수락된 데이터, -1: 이벤트기간 아님, 0: 에러, 1:정상
     **********************************************************************************************/
    @Select("CALL rd_data.p_evt_gganbu_mem_req_cancel(#{gganbuNo}, #{memNo}, #{ptrMemNo})")
    int gganbuMemReqCancel(GganbuMemReqCancelVo param);

    /**********************************************************************************************
     * @Method 설명 : 깐부 신청 리스트
     * @작성일   : 2021-12-01
     * @작성자   : 박성민
     * @param  :
     * insSlct - 신청구분 [r:받은신청, m:나의신청]
     * gganbuNo - 회차번호
     * memNo - 회원번호
     * pageNo - 페이지 번호
     * pagePerCnt - 페이지 당 노출 건수 (Limit)
     * @return
     * row #1
     *  cnt - 총건수
     * row #2
     *  gganbu_no - 회차번호
     *  mem_no - 회원번호 (신청자)
     *  mem_id - 회원아이디 (신청자)
     *  mem_nick - 회원대화명 (신청자)
     *  image_profile - 회원사진(신청자)
     *  mem_level - 회원레벨 (신청자)
     *  mem_state - 회원상태(1:정상3:블럭, 4:탈퇴, 5:영구정지...) (신청자)
     *  ptr_mem_no - 회원번호 (대상자)
     *  ptr_mem_id - 회원아이디 (대상자)
     *  ptr_mem_nick - 회원대화명 (대상자)
     *  ptr_image_profile - 회원사진(대상자)
     *  ptr_mem_level - 회원레벨 (대상자)
     *  ptr_mem_stat - 회원상태(1:정상3:블럭, 4:탈퇴, 5:영구정지...) (대상자)
     *  req_date - 신청일자
     **********************************************************************************************/
    @ResultMap({"ResultMap.integer", "ResultMap.GganbuMemReqListOutputVo"})
    @Select("CALL rd_data.p_evt_gganbu_mem_req_list(#{insSlct}, #{gganbuNo}, #{memNo}, #{pageNo}, #{pagePerCnt})")
    List<Object> gganbuMemReqList(GganbuMemReqListInputVo param);

    /**********************************************************************************************
     * @Method 설명 : 깐부 받은 내역 수락
     * @작성일   : 2021-12-01
     * @작성자   : 박성민
     * @param  :
     * gganbuNo - 회차번호
     * memNo - 회원번호(신청자)
     * ptrMemNo - 회원번호(대상자)
     * @return
     * s_return: -5: 신청내역없음, -4:대상자 이미깐부있음, -3: 신청자 이미깐부있음,
     *           -2: 신청자 탈퇴&정지회원, -1: 이벤트기간 아님, 0: 에러, 1:정상
     **********************************************************************************************/
    @Select("CALL rd_data.p_evt_gganbu_mem_ins(#{gganbuNo}, #{memNo}, #{ptrMemNo})")
    int gganbuMemIns(GganbuMemInsVo gganbuMemInsVo);

    /* 깐부 이벤트 끝~~~ */
}
