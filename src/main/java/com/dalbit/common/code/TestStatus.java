package com.dalbit.common.code;

import lombok.Getter;

@Getter
public enum TestStatus {

    플레이리스트_수정_실패("C006", "my.playlist.edit.fail", "플레이리스트 수정 실패 시");
    final private String RESULT_SUCCESS = "success";
    final private String RESULT_FAIL = "fail";

    final private String result;
    final private String messageCode;
    final private String messageKey;
    final private String desc;

    TestStatus(String messageCode, String messageKey, String desc){
        this.result = messageKey.contains("success") ? RESULT_SUCCESS : RESULT_FAIL;
        this.messageCode = messageCode;
        this.messageKey = messageKey;
        this.desc = desc;
    }
}
