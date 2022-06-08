package com.dalbit.broadcast.proc;

import com.dalbit.broadcast.vo.BoosterInfoVO;
import com.dalbit.broadcast.vo.DallaRoomSelResultVO;
import com.dalbit.broadcast.vo.TtsLogVo;
import com.dalbit.common.vo.ItemVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Component
@Repository
public interface Broadcast {
    /**
     * tts 선물 로그
     *
     * ttsYn CHAR(1)			-- TTS 아이템사용여부
     * memNo BIGINT			-- 회원번호 (보낸이)
     * pmemNo BIGINT			-- 회원번호 (받은이)
     * itemCode VARCHAR(10)		-- 사용아이템 코드
     * itemName VARCHAR(50)		-- 사용아이템 이름
     * ttsCrtSlct CHAR(1)		-- TTS 캐릭터 구분 (a:빠다가이, b:하나)
     * ttsMsg VARCHAR(50)		-- 메세지 내용
     * sendItemCnt TINYINT 		-- 아이템선물수
     * sendDalCnt INT			-- 선물달수
     */
    @Select("CALL rd_data.p_dalla_tts_log_ins(#{ttsYn}, #{memNo}, #{pmemNo}, #{itemCode}, #{itemName}, #{ttsCrtSlct}, #{ttsMsg}, #{sendItemCnt}, #{sendDalCnt})")
    Integer ttsLogIns(TtsLogVo ttsLogVo);

    /**
     * 특정 회원이 소유한 시그니처 아이템 목록 조회
     * */
    @Select("CALL rd_data.sp_signature_item_select(#{memNo}, '')")
    List<ItemVo> spSignatureItemSelect(Map<String, Object> param);

    /**********************************************************************************************
     * @Method 설명 : 방송중인 방의 부스터 리스트
     * @작성일 : 2022-06-03
     * @작성자 : 이정혁
     * @변경이력 :
     * @Parameter : roomNo - 방고유번호 (0 입력시 전체 방에 대한 리스트 가져옴)
     * @Return :
     **********************************************************************************************/
    @Select("CALL rd_data.p_dalla_room_booster_list(#{roomNo})")
    List<BoosterInfoVO> getBoosterList(@Param(value = "roomNo") String roomNo);

    /**********************************************************************************************
     * @Method 설명 : 방송점수 업데이트
     * @작성일 : 2022-05-26
     * @작성자 : 이정혁
     * @변경이력 :
     * @Parameter : roomNo BIGINT		-- 방송방 고유번호
     *              updSlct CHAR(1)	-- 점수 구분 [b:부스터, l:좋아요, v:시청자, s:받은별]
     *              updCnt	INT		-- 점수 건수
     *              updScore INT		-- 수정점수
     * @Return : s_return	INT	-- -1: 방없음, 0: 에러, 1:정상
     **********************************************************************************************/
    @Select("CALL rd_data.p_dalla_room_score_upd(#{roomNo}, #{updSlct}, #{updCnt}, #{updScore})")
    Integer updRoomScoreUpd(Map<String, Object> param);
    /**
     * 방정보(Native API)
     * */
    DallaRoomSelResultVO pDallaRoomSel(Map<String, Object> param);

}
