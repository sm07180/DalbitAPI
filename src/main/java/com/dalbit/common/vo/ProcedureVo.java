package com.dalbit.common.vo;

import com.dalbit.util.DalbitUtil;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Procedure 조회를 위한 VO
 */
@Getter
@Setter
@ToString
public class ProcedureVo {

    public ProcedureVo(){}

    public ProcedureVo(Object paramVo){
        //setData(new GsonBuilder().disableHtmlEscaping().create().toJson(paramVo).replace("\\\\", "\\").replace("\n", "\r"));
        String data = new Gson().toJson(paramVo).replace("\\\\", "\\");
        setData(DalbitUtil.uniDecode(data));
    }

    public ProcedureVo(String nickNm){
        setNickName(nickNm);
    }

    public ProcedureVo(String phoneNo, String password){
        setPhoneNo(phoneNo);
        setPassword(password);
    }

    private Object data;
    private Object box;
    private String ret;
    private String ext;
    private String phoneNo;
    private String password;
    private String nickName;
}
