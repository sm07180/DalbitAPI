package com.dalbit.broadcast.proc;

import com.dalbit.broadcast.vo.TtsLogVo;
import com.dalbit.common.vo.ItemVo;
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

}
