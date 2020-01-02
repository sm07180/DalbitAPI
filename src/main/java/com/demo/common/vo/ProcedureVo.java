package com.demo.common.vo;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;

/**
 * Procedure 조회를 위한 VO
 */
@Getter
@Setter
public class ProcedureVo {

    public ProcedureVo(){}

    public ProcedureVo(Object paramVo){
        setData(new Gson().toJson(paramVo));
    }

    private String data;

    private int ret;
    private String ext;


}
