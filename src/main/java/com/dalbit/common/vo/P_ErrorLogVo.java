package com.dalbit.common.vo;

import com.dalbit.member.vo.MemberVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_ErrorLogVo {

    public P_ErrorLogVo(){}
    public P_ErrorLogVo(ErrorLogVo errorLogVo, HttpServletRequest request){
        DeviceVo deviceVo = new DeviceVo(request);
        setMem_no(MemberVo.getMyMemNo(request));
        setOs(deviceVo.getOs());
        setVersion(deviceVo.getAppVersion());
        setDtype(errorLogVo.getDataType());
        setCtype(errorLogVo.getCommandType());
        setDesc(errorLogVo.getDesc());
    }

    private String mem_no;
    private int os;
    private String version;
    private String dtype;
    private String ctype;
    private String desc;

}
