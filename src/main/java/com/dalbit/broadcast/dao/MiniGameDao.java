package com.dalbit.broadcast.dao;

import com.dalbit.broadcast.vo.procedure.P_MiniGameListVo;
import com.dalbit.broadcast.vo.procedure.P_MiniGameWinListVo;
import com.dalbit.common.vo.ProcedureVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MiniGameDao {
    List<P_MiniGameListVo> callMiniGameList(ProcedureVo procedureVo);

    ProcedureVo callMiniGameAdd(ProcedureVo procedureVo);

    ProcedureVo callMiniGameEdit(ProcedureVo procedureVo);

    ProcedureVo callMiniGameSelect(ProcedureVo procedureVo);

    ProcedureVo callMiniGameStart(ProcedureVo procedureVo);

    ProcedureVo callMiniGameEnd(ProcedureVo procedureVo);

    List<P_MiniGameWinListVo> callRouletteWinList(ProcedureVo procedureVo);

    ProcedureVo callMiniGameSetSelect(ProcedureVo procedureVo);
}
