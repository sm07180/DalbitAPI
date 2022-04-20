package com.dalbit.store.dao;

import java.util.List;

import com.dalbit.store.vo.PayChargeVo;
import com.dalbit.store.vo.StoreChargeVo;
import com.dalbit.store.vo.StoreResultVo;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface StoreDao {
    @Transactional(readOnly = true)
    List<StoreChargeVo> selectChargeItem(Object value);
    @Transactional(readOnly = true)
    StoreChargeVo selectPayChargeItem(PayChargeVo payChargeVo);


    /**
     * ##### 외부결제 설정 정보 조회
     * CALL p_payment_set_sel()
     * #return
     * ios_payment_set CHAR    -- IOS설정[y:오픈,n:숨김]
     * aos_payment_set CHAR    -- AOS설정[y:오픈,n:숨김]
     */
    @Transactional(readOnly = true)
    StoreResultVo pPaymentSetSel();

    /**
     * ##### 외부결제 활성화 체크 프로시저
     * CALL p_payment_set_member_chk(
     * memNo       BIGINT      -- 회원번호
     * ,deviceUuid     VARCHAR(100)    -- 디바이스의 고유 ID
     * )
     * #return
     * s_return        INT     -- -1: 조건  미달,  0: 에러, 1:정상
     *
     * 디바이스 기준으로 외부결제수단으로 결제 성공 이력이 2회 이상 있으면 외부 결제 노출
     * 디바이스 기준으로 인앱 결제로 결제 성공 이력이 3회 이상 있으면 외부 결제 노출
     * 회원번호 기준으로 외부결제수단으로 결제 성공 이력이 2회 이상 있으면 외부 결제 노출
     * 회원번호 기준으로 입앱 결제로 결제 성공 이력이 3회 이상 있으면 외부 결제 노출
     */
    @Transactional(readOnly = true)
    @Select("CALL p_payment_set_member_chk(#{param1},#{param2})")
    Integer pPaymentSetMemberChk(String memNo, String deviceUuid);

}
