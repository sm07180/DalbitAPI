package com.dalbit.common.vo;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AppVersionVo {
    private String version;
    private Long buildNo;
    private Long upBuildNo;
}
