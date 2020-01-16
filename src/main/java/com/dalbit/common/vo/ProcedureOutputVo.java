package com.dalbit.common.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Procedure 조회를 위한 VO
 */
@Getter
@Setter
@ToString
public class ProcedureOutputVo extends ProcedureVo {

    private Object outputBox;

    public ProcedureOutputVo(ProcedureVo procedureVo) {
        setRet(procedureVo.getRet());
        setExt(procedureVo.getExt());
    }

    public ProcedureOutputVo(ProcedureVo procedureVo, Object outputBox) {
        setRet(procedureVo.getRet());
        setExt(procedureVo.getExt());
        setOutputBox(outputBox);

    }
}
