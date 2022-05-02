package com.dalbit.common.code;

public interface Status  {

    String RESULT_SUCCESS = "success";
    String RESULT_FAIL = "fail";

    String result = null;
    String messageCode = null;
    String messageKey = null;
    String desc = null;

    String getRESULT_SUCCESS();
    String getRESULT_FAIL();
    String getResult();
    String getMessageCode();
    String getMessageKey();
    String getDesc();
}
