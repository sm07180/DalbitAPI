package com.dalbit.exception.conrtoller;

import com.dalbit.broadcast.vo.RoomInfoVo;
import com.dalbit.common.code.Status;
import com.dalbit.common.service.CommonService;
import com.dalbit.common.vo.DeviceVo;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.common.vo.procedure.P_ErrorLogVo;
import com.dalbit.exception.GlobalException;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.CookieUtil;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.ClientAbortException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;

@Slf4j
@ControllerAdvice
@RestController
@Scope("prototype")
public class CommonErrorController {

    @Autowired
    GsonUtil gsonUtil;

    @Autowired
    CommonService commonService;

    /**
     * 에러로그 예외처리
     * false : 로그 적재 안함
     * true : 로그 적재
     */
    public boolean isSaveLog(GlobalException globalException, HttpServletResponse response, HttpServletRequest request){
        if(globalException.getStatus() == Status.벨리데이션체크) return false;
        if(request.getRequestURL().toString().endsWith("/error/log")) return false;
        if(request.getRequestURL().toString().endsWith("/ctrl/check/service")) return false;

        //Broken pipe 처리
        if(globalException.getClass().getSimpleName().toLowerCase().equals("clientabortexception")) return false; //ClientAbortException
        if(globalException.getClass().getSimpleName().toLowerCase().contains("clientabortexception")) return false; //ClientAbortException
        if(globalException.getMessage().contains("Broken pipe")) return false;

        return true;
    }

    @ExceptionHandler(GlobalException.class)
    public String exceptionHandle(GlobalException globalException, HttpServletResponse response, HttpServletRequest request){
        DalbitUtil.setHeader(request, response);

        try {
            if(isSaveLog(globalException, response, request)) {
                DeviceVo deviceVo = new DeviceVo(request);
                P_ErrorLogVo apiData = new P_ErrorLogVo();
                apiData.setOs("API");
                apiData.setDtype(deviceVo.getOs() + "|" + globalException.getMethodName());
                apiData.setVersion(deviceVo.getAppVersion());
                apiData.setBuild(deviceVo.getAppBuild());
                apiData.setMem_no(MemberVo.getMyMemNo(request));
                apiData.setCtype(request.getRequestURL().toString());
                String desc = "";
                if (!DalbitUtil.isEmpty(globalException.getData())) {
                    desc = "Data : \n" + globalException.getData().toString() + "\n";
                }
                if (!DalbitUtil.isEmpty(globalException.getValidationMessageDetail())) {
                    desc += "Validation : \n" + globalException.getValidationMessageDetail().toString() + "\n";
                }
                desc += "DeviceVo : \n" + gsonUtil.toJson(deviceVo);
                desc += "AuthToken : \n" + request.getHeader(DalbitUtil.getProperty("sso.header.cookie.name"));
                StringWriter sw = new StringWriter();
                globalException.printStackTrace(new PrintWriter(sw));
                if (sw != null) {
                    desc += "GlobalException : \n" + sw.toString();
                }
                apiData.setDesc(desc);
                if(!desc.toLowerCase().contains("clientabortexception") && !desc.toLowerCase().contains("broken pipe")){
                    commonService.saveErrorLog(apiData, request);
                }
            }
        }catch (Exception e){}

        if(globalException.getErrorStatus() != null){
            if(request.getRequestURL().toString().endsWith("/broad/vw/create") && globalException.getData() == null){
                globalException.setData(new RoomInfoVo());
            }
            return gsonUtil.toJson(new JsonOutputVo(globalException.getErrorStatus(), globalException.getData(), globalException.getValidationMessageDetail(), globalException.getMethodName()));
        }else{
            if(globalException.isCustomMessage()){
                JsonOutputVo jsonOutputVo = new JsonOutputVo();
                jsonOutputVo.setStatus(Status.벨리데이션체크);
                String msg = "";

                try{
                    msg = ((String) globalException.getValidationMessageDetail().get(0)).split(",")[0] + "," + ((String) globalException.getValidationMessageDetail().get(0)).split(",")[2];
                }catch(ArrayIndexOutOfBoundsException ae){
                    msg = (String)globalException.getValidationMessageDetail().get(0);
                }

                jsonOutputVo.setMessage(msg);
                jsonOutputVo.setMethodName(globalException.getMethodName());
                jsonOutputVo.setTimestamp(DalbitUtil.setTimestampInJsonOutputVo());
                if(request.getRequestURL().toString().endsWith("/broad/vw/create")){
                    jsonOutputVo.setData(new RoomInfoVo());
                }else{
                    jsonOutputVo.setData("");
                }
                return gsonUtil.toJsonCustomMessage(jsonOutputVo);
            }else{
                if(request.getRequestURL().toString().endsWith("/broad/vw/create") && globalException.getData() == null){
                    globalException.setData(new RoomInfoVo());
                }
                return gsonUtil.toJson(new JsonOutputVo(globalException.getStatus(), globalException.getData(), globalException.getValidationMessageDetail(), globalException.getMethodName()));
            }

        }
    }

    @ExceptionHandler(Exception.class)
    public String exceptionHandle(Exception exception, HttpServletResponse response, HttpServletRequest request){
        DalbitUtil.setHeader(request, response);

        try {
            DeviceVo deviceVo = new DeviceVo(request);
            P_ErrorLogVo apiData = new P_ErrorLogVo();
            apiData.setOs("API");
            apiData.setDtype(deviceVo.getOs()+"|"+request.getMethod());
            apiData.setVersion(deviceVo.getAppVersion());
            apiData.setBuild(deviceVo.getAppBuild());
            apiData.setMem_no(MemberVo.getMyMemNo(request));
            apiData.setCtype(request.getRequestURL().toString());
            String desc = "";
            if(!DalbitUtil.isEmpty(request.getParameterMap())){
                desc = "Data : \n" + gsonUtil.toJson(request.getParameterMap()) + "\n";
            }
            StringWriter sw = new StringWriter();
            exception.printStackTrace(new PrintWriter(sw));
            if(sw != null){
                desc += "Exception : \n" + sw.toString();
            }
            apiData.setDesc(desc);
            commonService.saveErrorLog(apiData, request);
        }catch (Exception e){}

        HashMap map = new HashMap();
        map.put("message", exception.getMessage());
        return gsonUtil.toJson(new JsonOutputVo(Status.비즈니스로직오류, map, null, request.getMethod() + ""));
    }

    @ExceptionHandler(ClientAbortException.class)
    public String exceptionHandle(ClientAbortException exception, HttpServletResponse response, HttpServletRequest request){
        DalbitUtil.setHeader(request, response);
        return gsonUtil.toJson(new JsonOutputVo(Status.사용자요청취소));
    }

}
