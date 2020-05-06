package com.dalbit.exception.conrtoller;

import com.dalbit.common.code.Status;
import com.dalbit.common.service.CommonService;
import com.dalbit.common.vo.DeviceVo;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.common.vo.procedure.P_ErrorLogVo;
import com.dalbit.exception.GlobalException;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;

@Slf4j
@ControllerAdvice
@RestController
public class CommonErrorController {

    @Autowired
    GsonUtil gsonUtil;

    @Autowired
    CommonService commonService;

    @ExceptionHandler(GlobalException.class)
    public String exceptionHandle(GlobalException globalException, HttpServletResponse response, HttpServletRequest request){
        DalbitUtil.setHeader(request, response);

        try {
            DeviceVo deviceVo = new DeviceVo(request);
            P_ErrorLogVo apiData = new P_ErrorLogVo();
            apiData.setOs("API");
            apiData.setDtype(deviceVo.getOs()+"|"+globalException.getMethodName());
            apiData.setVersion(deviceVo.getAppVersion());
            apiData.setBuild(deviceVo.getAppBuild());
            apiData.setMem_no(MemberVo.getMyMemNo(request));
            apiData.setCtype(request.getRequestURL().toString());
            String desc = "";
            if(!DalbitUtil.isEmpty(globalException.getData())){
                desc = "Data : \n" + globalException.getData().toString() + "\n";
            }
            if(!DalbitUtil.isEmpty(globalException.getValidationMessageDetail())){
                desc += "Validation : \n" + globalException.getValidationMessageDetail().toString() + "\n";
            }
            StringWriter sw = new StringWriter();
            globalException.printStackTrace(new PrintWriter(sw));
            if(sw != null){
                desc += "GlobalException : \n" + sw.toString();
            }
            apiData.setDesc(desc);
            commonService.saveErrorLog(apiData);
        }catch (Exception e){}

        if(globalException.getErrorStatus() != null){
            return gsonUtil.toJson(new JsonOutputVo(globalException.getErrorStatus(), globalException.getData(), globalException.getValidationMessageDetail(), globalException.getMethodName()));
        }else{
            if(globalException.isCustomMessage()){
                JsonOutputVo jsonOutputVo = new JsonOutputVo();
                jsonOutputVo.setStatus(Status.벨리데이션체크);
                String msg = ((String) globalException.getValidationMessageDetail().get(0)).split(",")[0] + "," + ((String) globalException.getValidationMessageDetail().get(0)).split(",")[2];
                jsonOutputVo.setMessage(msg);
                jsonOutputVo.setMethodName(globalException.getMethodName());
                jsonOutputVo.setTimestamp(DalbitUtil.setTimestampInJsonOutputVo());
                return gsonUtil.toJsonCustomMessage(jsonOutputVo);
            }else{
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
            commonService.saveErrorLog(apiData);
        }catch (Exception e){}

        return gsonUtil.toJson(new JsonOutputVo(Status.비즈니스로직오류, exception.getMessage(), null, request.getMethod() + ""));
    }
}
