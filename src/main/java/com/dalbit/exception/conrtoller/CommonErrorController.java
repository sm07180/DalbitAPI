package com.dalbit.exception.conrtoller;

import com.dalbit.common.service.CommonService;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.common.vo.P_ErrorLogVo;
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

@Slf4j
@ControllerAdvice
@RestController
public class CommonErrorController{

    @Autowired
    GsonUtil gsonUtil;

    @Autowired
    CommonService commonService;

    @ExceptionHandler(GlobalException.class)
    public String exceptionHandle(GlobalException globalException, HttpServletResponse response, HttpServletRequest request){
        DalbitUtil.setHeader(request, response);

        P_ErrorLogVo apiData = new P_ErrorLogVo();
        apiData.setOs("API");
        apiData.setMem_no(MemberVo.getMyMemNo(request));
        apiData.setDtype(globalException.getMethodName());
        apiData.setCtype(request.getRequestURL().toString());
        apiData.setDesc(DalbitUtil.isEmpty(globalException.getData()) ? globalException.getValidationMessageDetail().toString() : globalException.getData().toString());
        commonService.saveErrorLog(apiData);

        if(globalException.getErrorStatus() != null){
            return gsonUtil.toJson(new JsonOutputVo(globalException.getErrorStatus(), globalException.getData(), globalException.getValidationMessageDetail(), globalException.getMethodName()));
        }else{
            return gsonUtil.toJson(new JsonOutputVo(globalException.getStatus(), globalException.getData(), globalException.getValidationMessageDetail(), globalException.getMethodName()));
        }

    }
}
