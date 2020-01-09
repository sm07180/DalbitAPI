package com.demo.common.vo;

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
        this.setRet(procedureVo.getRet());
        this.setExt(procedureVo.getExt());
    }
}
