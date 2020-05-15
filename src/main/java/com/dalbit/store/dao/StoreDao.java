package com.dalbit.store.dao;

import java.util.List;

import com.dalbit.store.vo.PayChargeVo;
import com.dalbit.store.vo.StoreChargeVo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface StoreDao {
    @Transactional(readOnly = true)
    List<StoreChargeVo> selectChargeItem(String value);
    List<StoreChargeVo> selectPayChargeItem(PayChargeVo payChargeVo);
}
