package com.dalbit.common.vo.procedure;

import com.dalbit.common.vo.DeviceVo;
import com.dalbit.common.vo.request.ErrorLogVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_ErrorLogVo {

    public P_ErrorLogVo(){}
    public P_ErrorLogVo(ErrorLogVo errorLogVo, HttpServletRequest request){
        DeviceVo deviceVo = new DeviceVo(request);
        setMem_no(MemberVo.getMyMemNo(request));
        if(DalbitUtil.isEmpty(request.getParameter("os"))){
            setOs(String.valueOf(deviceVo.getOs()));
        } else {
            setOs(request.getParameter("os"));
        }
        setVersion(deviceVo.getAppVersion());
        setBuild(deviceVo.getAppBuild());
        setDtype(errorLogVo.getDataType());
        setCtype(errorLogVo.getCommandType());
        setDesc(errorLogVo.getDesc());
    }

    private String mem_no;
    private String os;
    private String version;
    private String build;
    private String dtype;
    private String ctype;
    private String desc;

}
