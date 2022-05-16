package com.dalbit.mailbox.dao;

import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.mailbox.vo.procedure.P_MailBoxAddTargetListVo;
import com.dalbit.mailbox.vo.procedure.P_MailBoxImageListVo;
import com.dalbit.mailbox.vo.procedure.P_MailBoxListVo;
import com.dalbit.mailbox.vo.procedure.P_MailBoxMsgListVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MailBoxDao {
    // @Transactional(readOnly = true)
    List<P_MailBoxListVo> callMailboxList(ProcedureVo procedureVo);

    // @Transactional(readOnly = true)
    List<P_MailBoxAddTargetListVo> callMailboxAddTargetList(ProcedureVo procedureVo);

    ProcedureVo callMailboxChatEnter(ProcedureVo procedureVo);

    ProcedureVo callMailboxChatExit(ProcedureVo procedureVo);

    ProcedureVo callMailboxChatSend(ProcedureVo procedureVo);

    ProcedureVo callMailboxChatRead(ProcedureVo procedureVo);

    List<P_MailBoxMsgListVo> callMailboxMsg(ProcedureVo procedureVo);

    // @Transactional(readOnly = true)
    List<P_MailBoxImageListVo> callMailboxImageList(ProcedureVo procedureVo);

    ProcedureVo callMailboxImageDelete(ProcedureVo procedureVo);

    // @Transactional(readOnly = true)
    ProcedureVo callMailboxUnreadCheck(ProcedureVo procedureVo);

    ProcedureVo callMailBoxIsUse(ProcedureVo procedureVo);
}
