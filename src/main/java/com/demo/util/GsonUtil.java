package com.demo.util;

import com.demo.common.code.ErrorStatus;
import com.demo.common.vo.JsonOutputVo;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class GsonUtil {

    @Autowired MessageUtil messageUtil;

    /**
     * 문자열중 html 태그를 excape 하여 json 형식으로 변환한다.
     *      * @param object
     *      * @return
     */
    public String toJson(Object object){
        return StringUtil.getSpclStrCnvr(new GsonBuilder().disableHtmlEscaping().create().toJson(object));
    }

    /**
     * JsonOutputVo를 json으로 변환 시 html 태그를 excape 하여 json 형식으로 변환한다.
     * @param jsonOutputVo
     * @return
     */
    public String toJson(JsonOutputVo jsonOutputVo){
        return StringUtil.getSpclStrCnvr(new GsonBuilder().disableHtmlEscaping().create().toJson(messageUtil.setJsonOutputVo(jsonOutputVo)));
    }

    /**
     * 에러메시지중 html 태그를 excape 하여 json 형식으로 변환한다.
     * @param errorStatus
     * @param data
     * @return
     */
    public String toJson(ErrorStatus errorStatus, HashMap data){
        return StringUtil.getSpclStrCnvr(new GsonBuilder().disableHtmlEscaping().create().toJson(messageUtil.setExceptionInfo(errorStatus, data)));
    }
}
