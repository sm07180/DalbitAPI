package com.dalbit.broadcast.dao;

import com.dalbit.broadcast.vo.procedure.P_GuestListVo;
import com.dalbit.broadcast.vo.procedure.P_GuestManagementListVo;
import com.dalbit.broadcast.vo.request.GuestListVo;
import com.dalbit.common.vo.ProcedureVo;
import org.springframework.stereotype.Repository;
//import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Repository
public interface GuestDao {

    ProcedureVo callGuestPropose(ProcedureVo procedureVo);

    ProcedureVo callGuestProposeCancel(ProcedureVo procedureVo);

    //@Transactional(readOnly = true)
    List<P_GuestManagementListVo> callGuestManagementList(ProcedureVo procedureVo);

    ProcedureVo callGuestInvite(ProcedureVo procedureVo);

    ProcedureVo callGuestInviteCancel(ProcedureVo procedureVo);

    ProcedureVo callGuestInviteOk(ProcedureVo procedureVo);

    ProcedureVo callBroadCastRoomGuestAdd(ProcedureVo procedureVo);

    ProcedureVo callBroadCastRoomGuestCancel(ProcedureVo procedureVo);

    //@Transactional(readOnly = true)
    List<P_GuestListVo> selectGuestList(GuestListVo guestListVo);

    //@Transactional(readOnly = true)
    HashMap selectGuestInfo(HashMap params);

    //@Transactional(readOnly = true)
    List<P_GuestListVo> getGuestInfo(P_GuestListVo pGuestListVo);
}
