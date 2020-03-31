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
        if(request.getParameter("os") != null){
            setOs(Integer.parseInt(request.getParameter("os")));
        }else{
            setOs(deviceVo.getOs());
        }
        if(request.getParameter("appVer") != null) {
            setVersion(request.getParameter("appVer"));
        }else{
            setVersion(deviceVo.getAppVersion());
        }
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
