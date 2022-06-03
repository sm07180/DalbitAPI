package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class NativeShortCutVO {
    private Integer orderNo;
    private String order;
    private String text;
    private String isOn; // true, false
}
