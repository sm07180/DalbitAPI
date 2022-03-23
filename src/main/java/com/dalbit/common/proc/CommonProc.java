package com.dalbit.common.proc;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
@Repository
public interface CommonProc {
    /**
     * 회원 본인인증 체크 및 상태해제 (휴면회원)
     * s_return		INT		--  -3:회원 없음, -2:휴면상태가 아님, -1: 본인인증 결과 없음,  0: 에러, 1:정상
     */
    @Select("CALL p_sleep_mem_chk_upd(#{memNo}, #{memPhone})")
    Integer sleepMemChkUpd(String memNo, String memPhone);
}
