package com.dalbit.tnk.dao;

import com.dalbit.common.vo.ProcedureVo;
import org.springframework.stereotype.Repository;

@Repository
public interface TnkCallbackDao {
    void callTnkCallback(ProcedureVo procedureVo);

    void callTnkConfirm(ProcedureVo procedureVo);
}
