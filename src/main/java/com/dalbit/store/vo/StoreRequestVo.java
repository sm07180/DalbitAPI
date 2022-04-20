package com.dalbit.store.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreRequestVo {
    /**
     * 110 - 외부결제
     * 010 - aos 인앱
     * 001 - ios 인앱
     */
    private String platform;
    private String memNo;
    private String deviceUuid;
}
