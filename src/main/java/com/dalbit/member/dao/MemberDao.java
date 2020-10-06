package com.dalbit.member.dao;

import com.dalbit.common.vo.BlockVo;
import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.common.vo.procedure.P_SelfAuthVo;
import com.dalbit.member.vo.ConnectRoomVo;
import com.dalbit.member.vo.ExchangeSuccessVo;
import com.dalbit.member.vo.TokenCheckVo;
import com.dalbit.member.vo.request.ExchangeReApplyVo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface MemberDao {

    @Transactional(readOnly = true)
    ProcedureVo callNickNameCheck(ProcedureVo procedureVo);
    List<ConnectRoomVo> callMemberLogin(ProcedureVo procedureVo);
    ProcedureVo callMemberJoin(ProcedureVo procedureVo);
    ProcedureVo callChangePassword(ProcedureVo procedureVo);
    @Transactional(readOnly = true)
    ProcedureVo callMemberInfoView(ProcedureVo procedureVo);
    ProcedureVo callMemberSessionUpdate(ProcedureVo procedureVo);
    ProcedureVo callMemberWithdrawal(ProcedureVo procedureVo);
    @Transactional(readOnly = true)
    TokenCheckVo selectMemState(String mem_no);
    @Transactional(readOnly = true)
    TokenCheckVo selectMemStateForPhone(String phoneNo);
    @Transactional(readOnly = true)
    int selectAdminBlock(BlockVo blockVo);
    @Transactional(readOnly = true)
    TokenCheckVo selectAnonymousMem(String mem_no);
    ProcedureVo callExchangeCalc(ProcedureVo procedureVo);
    ProcedureVo callExchangeApply(ProcedureVo procedureVo);

    @Transactional(readOnly = true)
    ExchangeSuccessVo exchangeApprovalSelect(String memNo);
    @Transactional(readOnly = true)
    ExchangeSuccessVo exchangeReApprovalSelect(ExchangeReApplyVo exchangeReApplyVo);

    @Transactional(readOnly = true)
    List<String> selectListeningRoom(String memNo);

    int callProfileEditHistory(P_SelfAuthVo pSelfAuthVo);
}
