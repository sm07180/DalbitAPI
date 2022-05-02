package com.dalbit.common.code;

import lombok.Getter;

@Getter
public enum StoreStatus implements Status {


    스토어_홈_데이터_조회_회원정보없음("EM001", "store.home.data.sel.member", "스토어_홈_데이터_조회_회원정보없음"),
    스토어_홈_데이터_조회_파라미터("EP001", "store.home.data.sel.parameter", "스토어_홈_데이터_조회_파라미터"),
    스토어_홈_데이터_조회("C001", "store.home.data.sel", "스토어_홈_데이터_조회"),
    스토어_아이템_조회_파라미터("EP002", "store.item.data.list.parameter", "스토어_아이템_조회_파라미터"),
    스토어_아이템_조회_오류("EI001", "store.item.data.list.error", "스토어_아이템_조회_오류"),
    스토어_아이템_조회("C002", "store.item.data.list", "스토어_아이템_조회");

    final private String RESULT_SUCCESS = "success";
    final private String RESULT_FAIL = "fail";

    final private String result;
    final private String messageCode;
    final private String messageKey;
    final private String desc;

    StoreStatus(String messageCode, String messageKey, String desc){
        this.result = messageKey.contains("success") ? RESULT_SUCCESS : RESULT_FAIL;
        this.messageCode = messageCode;
        this.messageKey = messageKey;
        this.desc = desc;
    }
}
